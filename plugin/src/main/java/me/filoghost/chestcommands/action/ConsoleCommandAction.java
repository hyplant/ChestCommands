/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.action;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.filoghost.chestcommands.placeholder.PlaceholderString;

public class ConsoleCommandAction implements Action {

    private final PlaceholderString command;

    public ConsoleCommandAction(final String serializedAction) { this.command = PlaceholderString.of(serializedAction); }

    @Override
    public void execute(final Player player) { Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.command.getValue(player)); }

}
