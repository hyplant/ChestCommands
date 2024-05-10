/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.icon.requirement.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class RemainingItem {

    private final int slotIndex;
    private final Material material;
    private final int durability;
    private int amount;

    public RemainingItem(final int slotIndex, final ItemStack item) {
        this.slotIndex = slotIndex;
        this.material = item.getType();
        if (item.getItemMeta() instanceof Damageable)
            this.durability = ((Damageable) item.getItemMeta()).getDamage();
        else
            this.durability = 0;
        this.amount = item.getAmount();
    }

    public int getSlotIndex() { return this.slotIndex; }

    public Material getMaterial() { return this.material; }

    public int getDurability() { return this.durability; }

    public int getAmount() { return this.amount; }

    public int subtract(final int minusAmount) {
        final int subtractedAmount = Math.min(minusAmount, this.amount);

        this.amount -= subtractedAmount;
        return subtractedAmount;
    }

}
