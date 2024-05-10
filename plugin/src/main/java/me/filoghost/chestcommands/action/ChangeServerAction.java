/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.action;

import org.bukkit.entity.Player;

import me.filoghost.chestcommands.hook.BungeeCordHook;
import me.filoghost.chestcommands.placeholder.PlaceholderString;

public class ChangeServerAction implements Action {

    private final PlaceholderString targetServer;

    public ChangeServerAction(final String serializedAction) { this.targetServer = PlaceholderString.of(serializedAction); }

    @Override
    public void execute(final Player player) { BungeeCordHook.connect(player, this.targetServer.getValue(player)); }

}
