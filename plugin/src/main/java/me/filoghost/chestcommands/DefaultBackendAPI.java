/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import me.filoghost.chestcommands.api.ConfigurableIcon;
import me.filoghost.chestcommands.api.Menu;
import me.filoghost.chestcommands.api.PlaceholderReplacer;
import me.filoghost.chestcommands.api.StaticIcon;
import me.filoghost.chestcommands.api.internal.BackendAPI;
import me.filoghost.chestcommands.icon.APIConfigurableIcon;
import me.filoghost.chestcommands.icon.APIStaticIcon;
import me.filoghost.chestcommands.menu.APIMenu;
import me.filoghost.chestcommands.menu.InternalMenu;
import me.filoghost.chestcommands.menu.MenuManager;
import me.filoghost.chestcommands.placeholder.PlaceholderManager;
import me.filoghost.fcommons.Preconditions;

public class DefaultBackendAPI extends BackendAPI {

    @Override
    public boolean isInternalMenuLoaded(@NotNull final String menuFileName) {
        Preconditions.notNull(menuFileName, "menuFileName");

        return MenuManager.getMenuByFileName(menuFileName) != null;
    }

    @Override
    public boolean openInternalMenu(@NotNull final Player player, @NotNull final String menuFileName) {
        Preconditions.notNull(player, "player");
        Preconditions.notNull(menuFileName, "menuFileName");

        final InternalMenu menu = MenuManager.getMenuByFileName(menuFileName);

        if (menu != null) {
            menu.open(player);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public @NotNull ConfigurableIcon createConfigurableIcon(@NotNull final Material material) { return new APIConfigurableIcon(material); }

    @Override
    public @NotNull Menu createMenu(@NotNull final Plugin plugin, @NotNull final String title, final int rows) {
        return new APIMenu(plugin, title, rows);
    }

    @Override
    public @NotNull StaticIcon createStaticIcon(@NotNull final ItemStack itemStack) { return new APIStaticIcon(itemStack); }

    @Override
    public void registerPlaceholder(@NotNull final Plugin plugin, @NotNull final String identifier,
            @NotNull final PlaceholderReplacer placeholderReplacer) {
        PlaceholderManager.registerPluginPlaceholder(plugin, identifier, placeholderReplacer);
    }

    @Override
    public boolean unregisterPlaceholder(@NotNull final Plugin plugin, @NotNull final String identifier) {
        return PlaceholderManager.unregisterPluginPlaceholder(plugin, identifier);
    }

}
