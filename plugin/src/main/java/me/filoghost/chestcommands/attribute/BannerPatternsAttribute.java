/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.banner.Pattern;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.parsing.ItemMetaParser;
import me.filoghost.chestcommands.parsing.ParseException;

public class BannerPatternsAttribute implements IconAttribute {

    private final List<Pattern> patterns;

    public BannerPatternsAttribute(final List<String> serializedPatterns, final AttributeErrorHandler errorHandler) {
        this.patterns = new ArrayList<>();

        for (final String serializedPattern : serializedPatterns) {
            if (serializedPattern == null || serializedPattern.isEmpty()) {
                continue; // Skip
            }

            try {
                final Pattern pattern = ItemMetaParser.parseBannerPattern(serializedPattern);
                this.patterns.add(pattern);
            } catch (final ParseException e) {
                errorHandler.onListElementError(serializedPattern, e);
            }
        }

    }

    @Override
    public void apply(final InternalConfigurableIcon icon) { icon.setBannerPatterns(this.patterns); }

}
