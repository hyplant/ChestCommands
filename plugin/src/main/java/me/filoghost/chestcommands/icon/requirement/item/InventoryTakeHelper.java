/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.icon.requirement.item;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import me.filoghost.fcommons.MaterialsHelper;
import me.filoghost.fcommons.Preconditions;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryTakeHelper {

    private final PlayerInventory inventory;
    private final List<RemainingItem> remainingItems;

    private boolean success;

    public InventoryTakeHelper(final PlayerInventory inventory) {
        this.inventory = inventory;
        this.remainingItems = new ArrayList<>();

        for (int slotIndex = 0; slotIndex < inventory.getSize(); slotIndex++) {
            final ItemStack item = inventory.getItem(slotIndex);
            if (item != null && !MaterialsHelper.isAir(item.getType())) {
                this.remainingItems.add(new RemainingItem(slotIndex, item));
            }
        }
    }

    public boolean prepareTakeItems(final List<RequiredItem> requiredItems) {
        final List<RequiredItem> missingItems = new ArrayList<>();

        // Sort required items: check required items with a restrictive durability first
        final List<RequiredItem> sortedRequiredItems = requiredItems.stream()
                .sorted(Comparator.comparing(RequiredItem::hasRestrictiveDurability).reversed()).collect(Collectors.toList());

        for (final RequiredItem requiredItem : sortedRequiredItems) {
            int remainingRequiredAmount = requiredItem.getAmount();

            for (final RemainingItem remainingItem : this.remainingItems) {
                if (remainingItem.getAmount() > 0 && requiredItem.isMatchingType(remainingItem)) {
                    final int takenAmount = remainingItem.subtract(remainingRequiredAmount);
                    remainingRequiredAmount -= takenAmount;
                    if (remainingRequiredAmount == 0) {
                        break;
                    }
                }
            }

            // Couldn't take the required amount of an item
            if (remainingRequiredAmount > 0) {
                missingItems.add(requiredItem);
            }
        }

        this.success = missingItems.isEmpty();
        return this.success;
    }

    public void applyTakeItems() {
        Preconditions.checkState(this.success, "items take preparation was not run or successful");

        for (final RemainingItem remainingItem : this.remainingItems) {
            final int slotIndex = remainingItem.getSlotIndex();
            final ItemStack inventoryItem = this.inventory.getItem(slotIndex);
            if (remainingItem.getAmount() != inventoryItem.getAmount()) {
                if (remainingItem.getAmount() > 0) {
                    inventoryItem.setAmount(remainingItem.getAmount());
                } else {
                    this.inventory.setItem(slotIndex, null);
                }
            }
        }
    }

}
