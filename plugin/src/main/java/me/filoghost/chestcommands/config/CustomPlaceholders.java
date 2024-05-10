/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.chestcommands.placeholder.StaticPlaceholder;
import me.filoghost.fcommons.Colors;
import me.filoghost.fcommons.config.ConfigPath;
import me.filoghost.fcommons.config.ConfigSection;
import me.filoghost.fcommons.config.ConfigType;
import me.filoghost.fcommons.config.FileConfig;
import me.filoghost.fcommons.logging.ErrorCollector;

public class CustomPlaceholders {

    private final List<StaticPlaceholder> placeholders = new ArrayList<>();

    public void load(final FileConfig config, final ErrorCollector errorCollector) {
        this.placeholders.clear();

        final ConfigSection placeholdersSection = config.getConfigSection("placeholders");
        if (placeholdersSection == null) {
            return;
        }

        for (final Entry<ConfigPath, String> entry : placeholdersSection.toMap(ConfigType.STRING).entrySet()) {
            final String placeholder = entry.getKey().asRawKey();
            final String replacement = Colors.addColors(entry.getValue());

            if (placeholder == null || placeholder.length() == 0) {
                errorCollector.add(Errors.Config.emptyPlaceholder(config.getSourceFile()));
                continue;
            }

            if (placeholder.length() > 100) {
                errorCollector.add(Errors.Config.tooLongPlaceholder(config.getSourceFile(), placeholder));
                continue;
            }

            this.placeholders.add(new StaticPlaceholder(placeholder, replacement));
        }
    }

    public List<StaticPlaceholder> getPlaceholders() { return this.placeholders; }

}
