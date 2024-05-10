/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.config;

import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.chestcommands.parsing.menu.LoadedMenu;
import me.filoghost.chestcommands.parsing.menu.MenuParser;
import me.filoghost.fcommons.Preconditions;
import me.filoghost.fcommons.config.BaseConfigManager;
import me.filoghost.fcommons.config.ConfigLoader;
import me.filoghost.fcommons.config.FileConfig;
import me.filoghost.fcommons.config.exception.ConfigException;
import me.filoghost.fcommons.config.mapped.MappedConfigLoader;
import me.filoghost.fcommons.logging.ErrorCollector;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigManager extends BaseConfigManager {

    private final MappedConfigLoader<Settings> settingsConfigLoader;
    private final ConfigLoader placeholdersConfigLoader;
    private final MappedConfigLoader<Lang> langConfigLoader;

    public ConfigManager(final Path rootDataFolder) {
        super(rootDataFolder);

        this.settingsConfigLoader = this.getMappedConfigLoader("config.yml", Settings.class);
        this.placeholdersConfigLoader = this.getConfigLoader("custom-placeholders.yml");
        this.langConfigLoader = this.getMappedConfigLoader("lang.yml", Lang.class);
    }

    public void tryLoadSettings(final ErrorCollector errorCollector) {
        Settings settings;
        try {
            settings = this.settingsConfigLoader.init();
        } catch (final ConfigException e) {
            this.logConfigInitException(errorCollector, this.settingsConfigLoader.getFile(), e);
            settings = new Settings();
        }
        Settings.setInstance(settings);
    }

    public void tryLoadLang(final ErrorCollector errorCollector) {
        Lang lang;
        try {
            lang = this.langConfigLoader.init();
        } catch (final ConfigException e) {
            this.logConfigInitException(errorCollector, this.langConfigLoader.getFile(), e);
            lang = new Lang();
        }
        Lang.setInstance(lang);
    }

    public CustomPlaceholders tryLoadCustomPlaceholders(final ErrorCollector errorCollector) {
        final CustomPlaceholders placeholders = new CustomPlaceholders();

        try {
            final FileConfig placeholdersConfig = this.placeholdersConfigLoader.init();
            placeholders.load(placeholdersConfig, errorCollector);
        } catch (final ConfigException t) {
            this.logConfigInitException(errorCollector, this.placeholdersConfigLoader.getFile(), t);
        }

        return placeholders;
    }

    public void tryCreateDefault(final ErrorCollector errorCollector, final ConfigLoader configLoader) {
        try {
            configLoader.createDefault();
        } catch (final ConfigException e) {
            this.logConfigInitException(errorCollector, configLoader.getFile(), e);
        }
    }

    public Path getMenusFolder() { return this.rootDataFolder.resolve("menu"); }

    public List<Path> getMenuFiles() throws IOException {
        Preconditions.checkState(Files.isDirectory(this.getMenusFolder()), "menus folder doesn't exist");

        try (Stream<Path> paths = Files.walk(this.getMenusFolder(), FileVisitOption.FOLLOW_LINKS)) {
            return paths.filter(this::isYamlFile).collect(Collectors.toList());
        }
    }

    private void logConfigInitException(final ErrorCollector errorCollector, final Path file, final ConfigException e) {
        errorCollector.add(e, Errors.Config.initException(file));
    }

    public List<LoadedMenu> tryLoadMenus(final ErrorCollector errorCollector) {
        final List<LoadedMenu> loadedMenus = new ArrayList<>();
        List<Path> menuFiles;

        try {
            menuFiles = this.getMenuFiles();
        } catch (final IOException e) {
            errorCollector.add(e, Errors.Config.menuListIOException(this.getMenusFolder()));
            return Collections.emptyList();
        }

        for (final Path menuFile : menuFiles) {
            final ConfigLoader menuConfigLoader = new ConfigLoader(this.rootDataFolder, menuFile);

            try {
                final FileConfig menuConfig = menuConfigLoader.load();
                loadedMenus.add(MenuParser.loadMenu(menuConfig, errorCollector));
            } catch (final ConfigException e) {
                this.logConfigInitException(errorCollector, menuConfigLoader.getFile(), e);
            }
        }

        return loadedMenus;
    }

    private boolean isYamlFile(final Path path) {
        return Files.isRegularFile(path) && path.getFileName().toString().toLowerCase().endsWith(".yml");
    }

}
