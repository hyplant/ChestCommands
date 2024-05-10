/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.filoghost.chestcommands.api.Icon;
import me.filoghost.chestcommands.api.MenuView;
import me.filoghost.chestcommands.icon.RefreshableIcon;
import me.filoghost.chestcommands.menu.BaseMenu;
import me.filoghost.chestcommands.menu.MenuManager;

public class DefaultMenuView implements MenuView {

    private final BaseMenu menu;
    private final Player viewer;
    private final InventoryGrid bukkitInventory;

    public DefaultMenuView(@NotNull final BaseMenu menu, @NotNull final Player viewer) {
        this.menu = menu;
        this.viewer = viewer;
        this.bukkitInventory = new InventoryGrid(new MenuInventoryHolder(this), menu.getRows(), menu.getTitle());
        this.refresh();
    }

    @Override
    public void refresh() {
        for (int i = 0; i < this.menu.getIcons().getSize(); i++) {
            final Icon icon = this.menu.getIcons().getByIndex(i);

            if (icon == null) {
                this.bukkitInventory.setByIndex(i, null);
            } else if (icon instanceof RefreshableIcon) {
                final ItemStack newItemStack = ((RefreshableIcon) icon).updateRendering(this.viewer, this.bukkitInventory.getByIndex(i));
                this.bukkitInventory.setByIndex(i, newItemStack);
            } else {
                this.bukkitInventory.setByIndex(i, icon.render(this.viewer));
            }
        }
    }

    @Override
    public void close() {
        if (this.viewer.isOnline() && MenuManager.getOpenMenuView(this.viewer) == this) {
            this.viewer.closeInventory();
        }
    }

    public void open() { this.viewer.openInventory(this.bukkitInventory.getInventory()); }

    public @Nullable Icon getIcon(final int slot) {
        if (slot < 0 || slot >= this.bukkitInventory.getSize()) {
            return null;
        }

        return this.menu.getIcons().getByIndex(slot);
    }

    @Override
    public @NotNull BaseMenu getMenu() { return this.menu; }

    @Override
    public @NotNull Player getViewer() { return this.viewer; }

}
