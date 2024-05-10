/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.icon;

import org.bukkit.entity.Player;

import me.filoghost.fcommons.Strings;

public class IconPermission {

    private final String permission;
    private final boolean negated;

    public IconPermission(String permission) {
        if (permission != null) {
            permission = permission.trim();
        }

        if (Strings.isEmpty(permission)) {
            this.permission = null;
            this.negated = false;
        } else {
            if (permission.startsWith("-")) {
                this.permission = permission.substring(1);
                this.negated = true;
            } else {
                this.permission = permission;
                this.negated = false;
            }
        }
    }

    private boolean hasPermission(final Player player) {
        if (this.isEmpty()) {
            return true;
        }

        if (this.negated) {
            return !player.hasPermission(this.permission);
        } else {
            return player.hasPermission(this.permission);
        }
    }

    public boolean isEmpty() { return this.permission == null; }

    public static boolean hasPermission(final Player player, final IconPermission permission) {
        return permission == null || permission.hasPermission(player);
    }

}
