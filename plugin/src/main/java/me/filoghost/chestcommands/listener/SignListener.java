/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.listener;

import org.bukkit.ChatColor;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.filoghost.chestcommands.Permissions;
import me.filoghost.chestcommands.config.Lang;
import me.filoghost.chestcommands.menu.InternalMenu;
import me.filoghost.chestcommands.menu.MenuManager;
import me.filoghost.chestcommands.util.Utils;

public class SignListener implements Listener {

    private static final int HEADER_LINE = 0;
    private static final int FILENAME_LINE = 1;

    private static final String SIGN_CREATION_TRIGGER = "[menu]";

    private static final ChatColor VALID_SIGN_COLOR = ChatColor.DARK_BLUE;
    private static final String VALID_SIGN_HEADER = SignListener.VALID_SIGN_COLOR + SignListener.SIGN_CREATION_TRIGGER;

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onSignClick(final PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        final BlockState clickedBlockState = event.getClickedBlock().getState();

        if (!(clickedBlockState instanceof final Sign sign)) {
            return;
        }

        if (!(sign.getSide(Side.FRONT).getLine(SignListener.HEADER_LINE).equalsIgnoreCase(SignListener.VALID_SIGN_HEADER))) {
            return;
        }

        final String menuFileName = Utils.addYamlExtension(sign.getSide(Side.FRONT).getLine(SignListener.FILENAME_LINE).trim());
        final InternalMenu menu = MenuManager.getMenuByFileName(menuFileName);

        if (menu == null) {
            event.getPlayer().sendMessage(Lang.get().menu_not_found);
            return;
        }

        menu.openCheckingPermission(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCreateMenuSign(final SignChangeEvent event) {
        final Player player = event.getPlayer();

        if (this.isCreatingMenuSign(event.getLine(SignListener.HEADER_LINE)) && this.canCreateMenuSign(player)) {
            String menuFileName = event.getLine(SignListener.FILENAME_LINE).trim();

            if (menuFileName.isEmpty()) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You must write a menu name in the second line.");
                return;
            }

            menuFileName = Utils.addYamlExtension(menuFileName);

            final InternalMenu menu = MenuManager.getMenuByFileName(menuFileName);
            if (menu == null) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "Menu \"" + menuFileName + "\" was not found.");
                return;
            }

            event.setLine(SignListener.HEADER_LINE, SignListener.VALID_SIGN_COLOR + event.getLine(SignListener.HEADER_LINE));
            player.sendMessage(ChatColor.GREEN + "Successfully created a sign for the menu " + menuFileName + ".");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSignChangeMonitor(final SignChangeEvent event) {
        // Prevent players without permissions from creating menu signs
        if (this.isValidMenuSign(event.getLine(SignListener.HEADER_LINE)) && !this.canCreateMenuSign(event.getPlayer())) {
            event.setLine(SignListener.HEADER_LINE, ChatColor.stripColor(event.getLine(SignListener.HEADER_LINE)));
        }
    }

    private boolean isCreatingMenuSign(final String headerLine) { return headerLine.equalsIgnoreCase(SignListener.SIGN_CREATION_TRIGGER); }

    private boolean isValidMenuSign(final String headerLine) { return headerLine.equalsIgnoreCase(SignListener.VALID_SIGN_HEADER); }

    private boolean canCreateMenuSign(final Player player) { return player.hasPermission(Permissions.SIGN_CREATE); }

}
