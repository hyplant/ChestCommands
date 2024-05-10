/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import org.bukkit.Color;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.parsing.ItemMetaParser;
import me.filoghost.chestcommands.parsing.ParseException;

public class LeatherColorAttribute implements IconAttribute {

    private final Color color;

    public LeatherColorAttribute(final String serializedColor, final AttributeErrorHandler errorHandler) throws ParseException {
        this.color = ItemMetaParser.parseRGBColor(serializedColor);
    }

    @Override
    public void apply(final InternalConfigurableIcon icon) { icon.setLeatherColor(this.color); }

}
