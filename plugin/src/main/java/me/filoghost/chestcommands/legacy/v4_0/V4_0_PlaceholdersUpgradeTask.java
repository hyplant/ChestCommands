/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.legacy.v4_0;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.text.StringEscapeUtils;

import me.filoghost.chestcommands.config.ConfigManager;
import me.filoghost.chestcommands.legacy.upgrade.UpgradeTask;
import me.filoghost.fcommons.Strings;
import me.filoghost.fcommons.config.Config;
import me.filoghost.fcommons.config.ConfigErrors;
import me.filoghost.fcommons.config.ConfigLoader;
import me.filoghost.fcommons.config.exception.ConfigLoadException;
import me.filoghost.fcommons.config.exception.ConfigSaveException;

public class V4_0_PlaceholdersUpgradeTask extends UpgradeTask {

    private final Path oldPlaceholdersFile;
    private final ConfigLoader newPlaceholdersConfigLoader;
    private Config updatedConfig;

    public V4_0_PlaceholdersUpgradeTask(final ConfigManager configManager) {
        this.oldPlaceholdersFile = configManager.getRootDataFolder().resolve("placeholders.yml");
        this.newPlaceholdersConfigLoader = configManager.getConfigLoader("custom-placeholders.yml");
    }

    @Override
    public Path getOriginalFile() { return this.oldPlaceholdersFile; }

    @Override
    public Path getUpgradedFile() { return this.newPlaceholdersConfigLoader.getFile(); }

    @Override
    public void computeChanges() throws ConfigLoadException {
        if (!Files.isRegularFile(this.getOriginalFile()) || Files.isRegularFile(this.getUpgradedFile())) {
            return;
        }

        // Do NOT load the new placeholder configuration from disk, as it should only
        // contain placeholders imported from the old file
        final Config newPlaceholdersConfig = new Config();
        List<String> lines;
        try {
            lines = Files.readAllLines(this.oldPlaceholdersFile);
        } catch (final IOException e) {
            throw new ConfigLoadException(ConfigErrors.readIOException, e);
        }

        for (final String line : lines) {
            // Comment or empty line
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            // Ignore bad line
            if (!line.contains(":")) {
                continue;
            }

            final String[] parts = Strings.splitAndTrim(line, ":", 2);
            final String placeholder = V4_0_PlaceholdersUpgradeTask.unquote(parts[0]);
            final String replacement = StringEscapeUtils.unescapeJava(V4_0_PlaceholdersUpgradeTask.unquote(parts[1]));

            newPlaceholdersConfig.setString("placeholders." + placeholder, replacement);
            this.setSaveRequired();
        }

        this.updatedConfig = newPlaceholdersConfig;
    }

    @Override
    public void saveChanges() throws ConfigSaveException {
        try {
            Files.deleteIfExists(this.oldPlaceholdersFile);
        } catch (final IOException ignored) {
        }
        this.newPlaceholdersConfigLoader.save(this.updatedConfig);
    }

    private static String unquote(final String input) {
        if (input.length() < 2) {
            // Too short, cannot be a quoted string
            return input;
        }
        if (input.startsWith("'") && input.endsWith("'")) {
            return input.substring(1, input.length() - 1);
        } else if (input.startsWith("\"") && input.endsWith("\"")) {
            return input.substring(1, input.length() - 1);
        }

        return input;
    }

}
