/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.icon.requirement.item;

import org.bukkit.Material;

import me.filoghost.fcommons.Preconditions;

public class RequiredItem {

    private final Material material;
    private final int amount;
    private short durability;
    private boolean isDurabilityRestrictive = false;

    public RequiredItem(final Material material, final int amount) {
        Preconditions.checkArgumentNotAir(material, "material");

        this.material = material;
        this.amount = amount;
    }

    public Material getMaterial() { return this.material; }

    public int getAmount() { return this.amount; }

    public short getDurability() { return this.durability; }

    public void setRestrictiveDurability(final short durability) {
        Preconditions.checkArgument(durability >= 0, "durability must be 0 or greater");

        this.durability = durability;
        this.isDurabilityRestrictive = true;
    }

    public boolean hasRestrictiveDurability() { return this.isDurabilityRestrictive; }

    public boolean isMatchingType(final RemainingItem item) {
        return item != null && item.getMaterial() == this.material && this.isMatchingDurability(item.getDurability());
    }

    private boolean isMatchingDurability(final int i) {
        if (this.isDurabilityRestrictive) {
            return this.durability == i;
        } else {
            return true;
        }
    }

}
