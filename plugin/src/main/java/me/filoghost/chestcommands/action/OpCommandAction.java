/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.action;

import org.bukkit.entity.Player;

import me.filoghost.chestcommands.placeholder.PlaceholderString;

public class OpCommandAction implements Action {

    private final PlaceholderString command;

    public OpCommandAction(final String serializedAction) { this.command = PlaceholderString.of(serializedAction); }

    @Override
    public void execute(final Player player) {
        if (player.isOp()) {
            player.chat("/" + this.command.getValue(player));
        } else {
            player.setOp(true);
            player.chat("/" + this.command.getValue(player));
            player.setOp(false);
        }
    }

}
