/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.action;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.filoghost.chestcommands.placeholder.PlaceholderString;
import me.filoghost.fcommons.Colors;

public class BroadcastAction implements Action {

    private final PlaceholderString message;

    public BroadcastAction(final String serializedAction) { this.message = PlaceholderString.of(Colors.addColors(serializedAction)); }

    @Override
    public void execute(final Player player) { Bukkit.broadcastMessage(this.message.getValue(player)); }

}
