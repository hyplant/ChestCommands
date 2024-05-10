/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.inventory;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class InventoryGrid extends Grid<ItemStack> {

    private final Inventory inventory;

    public InventoryGrid(final MenuInventoryHolder inventoryHolder, final int rows, final String title) {
        super(rows, 9);
        this.inventory = Bukkit.createInventory(inventoryHolder, this.getSize(), title);
    }

    public Inventory getInventory() { return this.inventory; }

    @Override
    protected @Nullable ItemStack getByIndex0(final int ordinalIndex) { return this.inventory.getItem(ordinalIndex); }

    @Override
    protected void setByIndex0(final int ordinalIndex, @Nullable final ItemStack element) { this.inventory.setItem(ordinalIndex, element); }

}
