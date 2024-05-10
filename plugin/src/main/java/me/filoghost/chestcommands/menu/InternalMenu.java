/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.menu;

import java.nio.file.Path;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.Permissions;
import me.filoghost.chestcommands.action.Action;
import me.filoghost.chestcommands.api.MenuView;
import me.filoghost.chestcommands.config.Lang;
import me.filoghost.fcommons.collection.CollectionUtils;

public class InternalMenu extends BaseMenu {

    private final Path sourceFile;
    private final String openPermission;

    private ImmutableList<Action> openActions;
    private int refreshTicks;

    public InternalMenu(@NotNull final String title, final int rows, @NotNull final Path sourceFile) {
        super(title, rows);
        this.sourceFile = sourceFile;
        this.openPermission = Permissions.OPEN_MENU_PREFIX + sourceFile.getFileName();
    }

    public @NotNull Path getSourceFile() { return this.sourceFile; }

    public void setOpenActions(final List<Action> openAction) { this.openActions = CollectionUtils.newImmutableList(openAction); }

    public String getOpenPermission() { return this.openPermission; }

    public int getRefreshTicks() { return this.refreshTicks; }

    public void setRefreshTicks(final int refreshTicks) { this.refreshTicks = refreshTicks; }

    @Override
    public @NotNull MenuView open(@NotNull final Player player) {
        if (this.openActions != null) {
            for (final Action openAction : this.openActions) {
                openAction.execute(player);
            }
        }

        return super.open(player);
    }

    @Override
    public Plugin getPlugin() { return ChestCommands.getInstance(); }

    public void openCheckingPermission(final Player player) {
        if (player.hasPermission(this.openPermission)) {
            this.open(player);
        } else {
            this.sendNoOpenPermissionMessage(player);
        }
    }

    public void sendNoOpenPermissionMessage(final CommandSender sender) {
        final String noPermMessage = Lang.get().no_open_permission;
        if (noPermMessage != null && !noPermMessage.isEmpty()) {
            sender.sendMessage(noPermMessage.replace("{permission}", this.openPermission));
        }
    }

}
