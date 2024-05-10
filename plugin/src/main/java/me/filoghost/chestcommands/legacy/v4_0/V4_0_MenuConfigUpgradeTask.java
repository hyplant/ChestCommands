/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.legacy.v4_0;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import me.filoghost.chestcommands.config.ConfigManager;
import me.filoghost.chestcommands.legacy.upgrade.YamlUpgradeTask;
import me.filoghost.chestcommands.parsing.icon.AttributeType;
import me.filoghost.chestcommands.parsing.menu.MenuSettingsPath;
import me.filoghost.fcommons.Strings;
import me.filoghost.fcommons.config.Config;
import me.filoghost.fcommons.config.ConfigPath;
import me.filoghost.fcommons.config.ConfigSection;
import me.filoghost.fcommons.config.ConfigType;

/*
 * All the changes that are not easy to make without parsing the YML file
 */
public class V4_0_MenuConfigUpgradeTask extends YamlUpgradeTask {

    private final String legacyCommandSeparator;

    public V4_0_MenuConfigUpgradeTask(final ConfigManager configManager, final Path menuFile, final String legacyCommandSeparator) {
        super(configManager.getConfigLoader(menuFile));
        this.legacyCommandSeparator = legacyCommandSeparator;
    }

    @Override
    public void computeYamlChanges(final Config menuConfig) {
        menuConfig.setHeader();

        for (final Entry<ConfigPath, ConfigSection> entry : menuConfig.toMap(ConfigType.SECTION).entrySet()) {
            final ConfigPath key = entry.getKey();
            final ConfigSection section = menuConfig.getConfigSection(key);
            if (section == null) {
                continue;
            }

            if (key.equals(MenuSettingsPath.ROOT_SECTION)) {
                this.upgradeMenuSettings(section);
            } else {
                this.upgradeIcon(section);
            }
        }
    }

    private void upgradeMenuSettings(final ConfigSection section) {
        this.expandInlineList(section, MenuSettingsPath.COMMANDS, ";");
        this.expandInlineList(section, MenuSettingsPath.OPEN_ACTIONS, this.legacyCommandSeparator);
        this.updateActionPrefixes(section, MenuSettingsPath.OPEN_ACTIONS);
    }

    private void upgradeIcon(final ConfigSection section) {
        this.expandInlineList(section, AttributeType.ENCHANTMENTS.getConfigKey(), ";");
        this.expandInlineList(section, AttributeType.ACTIONS.getConfigKey(), this.legacyCommandSeparator);
        this.updateActionPrefixes(section, AttributeType.ACTIONS.getConfigKey());
        this.expandSingletonList(section, AttributeType.REQUIRED_ITEMS.getConfigKey());
        this.expandInlineItemstack(section);
    }

    private void updateActionPrefixes(final ConfigSection config, final ConfigPath configPath) {
        final List<String> actions = config.getStringList(configPath);
        if (actions == null) {
            return;
        }

        for (int i = 0; i < actions.size(); i++) {
            final String oldAction = actions.get(i);
            String newAction = oldAction;
            newAction = this.replacePrefix(newAction, "menu:", "open:");
            newAction = this.replacePrefix(newAction, "givemoney:", "give-money:");
            newAction = this.replacePrefix(newAction, "dragonbar:", "dragon-bar:");
            newAction = this.replacePrefix(newAction, "server ", "server: ");

            if (!newAction.equals(oldAction)) {
                this.setSaveRequired();
                actions.set(i, newAction);
            }
        }

        config.setStringList(configPath, actions);
    }

    private String replacePrefix(final String action, final String oldPrefix, final String newPrefix) {
        if (action.startsWith(oldPrefix)) {
            this.setSaveRequired();
            return newPrefix + action.substring(oldPrefix.length());
        } else {
            return action;
        }
    }

    private void expandInlineItemstack(final ConfigSection section) {
        String material = section.getString(AttributeType.MATERIAL.getConfigKey());
        if (material == null) {
            return;
        }

        if (material.contains(",")) {
            final String[] parts = Strings.splitAndTrim(material, ",", 2);
            if (!section.contains(AttributeType.AMOUNT.getConfigKey())) {
                try {
                    section.setInt(AttributeType.AMOUNT.getConfigKey(), Integer.parseInt(parts[1]));
                } catch (final NumberFormatException e) {
                    section.setString(AttributeType.AMOUNT.getConfigKey(), parts[1]);
                }
            }
            material = parts[0];
            section.setString(AttributeType.MATERIAL.getConfigKey(), material);
            this.setSaveRequired();
        }

        if (material.contains(":")) {
            final String[] parts = Strings.splitAndTrim(material, ":", 2);
            if (!section.contains(AttributeType.DURABILITY.getConfigKey())) {
                try {
                    section.setInt(AttributeType.DURABILITY.getConfigKey(), Integer.parseInt(parts[1]));
                } catch (final NumberFormatException e) {
                    section.setString(AttributeType.DURABILITY.getConfigKey(), parts[1]);
                }
            }
            material = parts[0];
            section.setString(AttributeType.MATERIAL.getConfigKey(), material);
            this.setSaveRequired();
        }
    }

    private void expandInlineList(final ConfigSection config, final ConfigPath path, final String separator) {
        if (config.get(path).isPresentAs(ConfigType.STRING)) {
            config.setStringList(path, this.splitListElements(config.getString(path), separator));
            this.setSaveRequired();
        }
    }

    private void expandSingletonList(final ConfigSection config, final ConfigPath path) {
        if (config.get(path).isPresentAs(ConfigType.STRING)) {
            config.setStringList(path, Collections.singletonList(config.getString(path)));
            this.setSaveRequired();
        }
    }

    private List<String> splitListElements(final String input, String separator) {
        if (separator == null || separator.length() == 0) {
            separator = ";";
        }

        final String[] splitValues = Strings.splitAndTrim(input, separator);
        final List<String> values = new ArrayList<>();

        for (final String value : splitValues) {
            if (!value.isEmpty()) {
                values.add(value);
            }
        }

        // Return a list with an empty value to avoid displaying the empty list value
        // "[]" in the YML file
        if (values.isEmpty()) {
            values.add("");
        }

        return values;
    }

}
