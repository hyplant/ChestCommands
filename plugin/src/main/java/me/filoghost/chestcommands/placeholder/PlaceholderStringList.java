/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.placeholder;

import java.util.List;

import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.fcommons.collection.CollectionUtils;

public class PlaceholderStringList {

    private final ImmutableList<String> originalList;
    private final ImmutableList<String> listWithStaticPlaceholders;
    private final ImmutableList<PlaceholderString> placeholderStringList;
    private final boolean hasDynamicPlaceholders;

    public PlaceholderStringList(final List<String> list) {
        Preconditions.notNull(list, "list");
        this.originalList = ImmutableList.copyOf(list);

        // Replace static placeholders only once, if present
        if (PlaceholderManager.hasStaticPlaceholders(this.originalList)) {
            this.listWithStaticPlaceholders = CollectionUtils.toImmutableList(this.originalList,
                    PlaceholderManager::replaceStaticPlaceholders);
        } else {
            this.listWithStaticPlaceholders = this.originalList;
        }

        this.hasDynamicPlaceholders = PlaceholderManager.hasDynamicPlaceholders(this.listWithStaticPlaceholders);
        if (this.hasDynamicPlaceholders) {
            this.placeholderStringList = CollectionUtils.toImmutableList(this.listWithStaticPlaceholders, PlaceholderString::of);
        } else {
            this.placeholderStringList = null;
        }
    }

    public ImmutableList<String> getOriginalValue() { return this.originalList; }

    public ImmutableList<String> getValue(final Player player) {
        if (this.hasDynamicPlaceholders) {
            return CollectionUtils.toImmutableList(this.placeholderStringList, element -> element.getValue(player));
        } else {
            return this.listWithStaticPlaceholders;
        }
    }

    public boolean hasDynamicPlaceholders() { return this.hasDynamicPlaceholders; }

}
