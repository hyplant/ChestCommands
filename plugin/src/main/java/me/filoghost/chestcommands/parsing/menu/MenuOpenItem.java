/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.parsing.menu;

import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import me.filoghost.fcommons.Preconditions;

public class MenuOpenItem {

    private final Material material;
    private final ClickType clickType;
    private short durability;
    private boolean isRestrictiveDurability;

    public MenuOpenItem(final Material material, final ClickType clickType) {
        Preconditions.checkArgumentNotAir(material, "material");
        Preconditions.notNull(clickType, "clickType");

        this.material = material;
        this.clickType = clickType;
    }

    public void setRestrictiveDurability(final short durability) {
        this.durability = durability;
        this.isRestrictiveDurability = true;
    }

    public boolean matches(final ItemStack item, final Action action) {
        if (item == null) {
            return false;
        }

        if (this.material != item.getType()) {
            return false;
        }
        if (item.getItemMeta() instanceof Damageable)
            if (this.isRestrictiveDurability && this.durability != ((Damageable) item.getItemMeta()).getDamage()) {
                return false;
            }

        return this.clickType.isValidInteract(action);
    }
}
