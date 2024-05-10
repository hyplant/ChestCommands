/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.filoghost.chestcommands.inventory.DefaultMenuView;
import me.filoghost.chestcommands.menu.InternalMenu;
import me.filoghost.chestcommands.menu.MenuManager;
import me.filoghost.chestcommands.placeholder.PlaceholderManager;

public class TickingTask implements Runnable {

    private long currentTick;

    @Override
    public void run() {
        this.updateMenus();
        PlaceholderManager.onTick();

        this.currentTick++;
    }

    private void updateMenus() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            final DefaultMenuView menuView = MenuManager.getOpenMenuView(player);

            if (menuView == null || !(menuView.getMenu() instanceof InternalMenu)) {
                continue;
            }

            final int refreshTicks = ((InternalMenu) menuView.getMenu()).getRefreshTicks();

            if (refreshTicks > 0 && this.currentTick % refreshTicks == 0) {
                menuView.refresh();
            }
        }
    }

}
