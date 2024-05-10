/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import org.bukkit.DyeColor;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.parsing.ItemMetaParser;
import me.filoghost.chestcommands.parsing.ParseException;

public class BannerColorAttribute implements IconAttribute {

    private final DyeColor dyeColor;

    public BannerColorAttribute(final String serializedDyeColor, final AttributeErrorHandler errorHandler) throws ParseException {
        this.dyeColor = ItemMetaParser.parseDyeColor(serializedDyeColor);
    }

    @Override
    public void apply(final InternalConfigurableIcon icon) { icon.setBannerColor(this.dyeColor); }

}
