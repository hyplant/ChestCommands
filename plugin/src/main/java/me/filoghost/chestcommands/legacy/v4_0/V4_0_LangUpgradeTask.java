/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.legacy.v4_0;

import me.filoghost.chestcommands.config.ConfigManager;
import me.filoghost.chestcommands.legacy.upgrade.YamlUpgradeTask;
import me.filoghost.fcommons.config.Config;

public class V4_0_LangUpgradeTask extends YamlUpgradeTask {

    public V4_0_LangUpgradeTask(final ConfigManager configManager) { super(configManager.getConfigLoader("lang.yml")); }

    @Override
    public void computeYamlChanges(final Config settingsConfig) {
        this.removeValue(settingsConfig, "open-menu");
        this.removeValue(settingsConfig, "open-menu-others");
        this.replaceStringValue(settingsConfig, "no-required-item", "{datavalue}", "{durability}");
    }

}
