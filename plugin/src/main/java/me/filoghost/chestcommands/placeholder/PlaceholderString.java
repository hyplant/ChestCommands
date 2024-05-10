/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.placeholder;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class PlaceholderString {

    private final String originalString;
    private final String stringWithStaticPlaceholders;
    private final boolean hasDynamicPlaceholders;

    public static @Nullable PlaceholderString of(final String string) {
        if (string != null) {
            return new PlaceholderString(string);
        } else {
            return null;
        }
    }

    private PlaceholderString(final String originalString) {
        this.originalString = originalString;
        this.stringWithStaticPlaceholders = PlaceholderManager.replaceStaticPlaceholders(originalString);
        this.hasDynamicPlaceholders = PlaceholderManager.hasDynamicPlaceholders(this.stringWithStaticPlaceholders);
    }

    public String getValue(final Player player) {
        if (this.hasDynamicPlaceholders) {
            return PlaceholderManager.replaceDynamicPlaceholders(this.stringWithStaticPlaceholders, player);
        } else {
            return this.stringWithStaticPlaceholders;
        }
    }

    public String getOriginalValue() { return this.originalString; }

    public boolean hasDynamicPlaceholders() { return this.hasDynamicPlaceholders; }

}
