/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.icon.requirement;

import org.bukkit.entity.Player;

public interface Requirement {

    boolean hasCost(Player player);

    boolean takeCost(Player player);

    static boolean hasAllCosts(final Player player, final Requirement... requirements) {
        for (final Requirement requirement : requirements) {
            if (requirement != null && !requirement.hasCost(player)) {
                return false;
            }
        }

        return true;
    }

    static boolean takeAllCosts(final Player player, final Requirement... requirements) {
        for (final Requirement requirement : requirements) {
            if (requirement != null) {
                final boolean success = requirement.takeCost(player);
                if (!success) {
                    return false;
                }
            }
        }

        return true;
    }

}
