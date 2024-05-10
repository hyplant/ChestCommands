/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.api;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import me.filoghost.chestcommands.api.internal.BackendAPI;

/**
 * An {@link Icon} which statically displays a given {@link ItemStack}.
 *
 * @since 1
 */
public interface StaticIcon extends ClickableIcon {

    /**
     * Creates a new static icon with the given item stack.
     *
     * @param itemStack the item stack to display
     * @return the created icon
     * @since 1
     */
    static @NotNull StaticIcon create(@NotNull final ItemStack itemStack) {
        return BackendAPI.getImplementation().createStaticIcon(itemStack);
    }

    /**
     * Sets the item stack to display.
     *
     * @param itemStack the new displayed item stack
     * @since 1
     */
    void setItemStack(@NotNull ItemStack itemStack);

    /**
     * Returns the displayed item stack.
     *
     * @return the current displayed item stack
     * @since 1
     */
    @NotNull
    ItemStack getItemStack();

}
