/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.action;

import me.filoghost.chestcommands.placeholder.PlaceholderString;
import org.bukkit.entity.Player;

public class PlayerCommandAction implements Action {

    private final PlaceholderString command;

    public PlayerCommandAction(final String serializedAction) { this.command = PlaceholderString.of(serializedAction); }

    @Override
    public void execute(final Player player) { player.chat('/' + this.command.getValue(player)); }

}
