/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.config.Settings;
import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.fcommons.Colors;
import me.filoghost.fcommons.collection.CollectionUtils;

import java.util.List;

public class LoreAttribute implements IconAttribute {

    private final List<String> lore;

    public LoreAttribute(final List<String> lore, final AttributeErrorHandler errorHandler) { this.lore = this.colorLore(lore); }

    private List<String> colorLore(final List<String> input) {
        return CollectionUtils.toArrayList(input, line -> {
            if (!line.isEmpty()) {
                return Settings.get().default_color__lore + Colors.addColors(line);
            } else {
                return line;
            }
        });
    }

    @Override
    public void apply(final InternalConfigurableIcon icon) { icon.setLore(this.lore); }

}
