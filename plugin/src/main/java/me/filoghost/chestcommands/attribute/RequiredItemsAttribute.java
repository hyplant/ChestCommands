/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import java.util.ArrayList;
import java.util.List;
import me.filoghost.chestcommands.icon.InternalConfigurableIcon;

import me.filoghost.chestcommands.icon.requirement.item.RequiredItem;
import me.filoghost.chestcommands.parsing.ItemStackParser;
import me.filoghost.chestcommands.parsing.ParseException;

public class RequiredItemsAttribute implements IconAttribute {

    private final List<RequiredItem> requiredItems;

    public RequiredItemsAttribute(final List<String> serializedRequiredItems, final AttributeErrorHandler errorHandler) {
        this.requiredItems = new ArrayList<>();

        for (final String serializedItem : serializedRequiredItems) {
            try {
                final ItemStackParser itemReader = new ItemStackParser(serializedItem, true);
                itemReader.checkNotAir();
                final RequiredItem requiredItem = new RequiredItem(itemReader.getMaterial(), itemReader.getAmount());
                if (itemReader.hasExplicitDurability()) {
                    requiredItem.setRestrictiveDurability(itemReader.getDurability());
                }
                this.requiredItems.add(requiredItem);
            } catch (final ParseException e) {
                errorHandler.onListElementError(serializedItem, e);
            }
        }
    }

    @Override
    public void apply(final InternalConfigurableIcon icon) { icon.setRequiredItems(this.requiredItems); }

}
