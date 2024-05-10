/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.legacy.upgrade;

import me.filoghost.fcommons.config.Config;
import java.nio.file.Path;

import me.filoghost.fcommons.config.ConfigLoader;
import me.filoghost.fcommons.config.exception.ConfigLoadException;
import me.filoghost.fcommons.config.exception.ConfigSaveException;

public abstract class YamlUpgradeTask extends UpgradeTask {

    private final ConfigLoader configLoader;
    private Config updatedConfig;

    public YamlUpgradeTask(final ConfigLoader configLoader) { this.configLoader = configLoader; }

    @Override
    public final Path getOriginalFile() { return this.configLoader.getFile(); }

    @Override
    public final Path getUpgradedFile() { return this.configLoader.getFile(); }

    @Override
    public final void computeChanges() throws ConfigLoadException {
        if (!this.configLoader.fileExists()) {
            return;
        }
        final Config config = this.configLoader.load();
        this.computeYamlChanges(config);
        this.updatedConfig = config;
    }

    @Override
    public final void saveChanges() throws ConfigSaveException { this.configLoader.save(this.updatedConfig); }

    protected abstract void computeYamlChanges(Config config);

    protected void removeValue(final Config config, final String configPath) {
        if (config.contains(configPath)) {
            config.remove(configPath);
            this.setSaveRequired();
        }
    }

    protected void replaceStringValue(final Config settingsConfig, final String configPath, final String target, final String replacement) {
        final String value = settingsConfig.getString(configPath);
        if (value.contains(target)) {
            settingsConfig.setString(configPath, value.replace(target, replacement));
            this.setSaveRequired();
        }
    }

}
