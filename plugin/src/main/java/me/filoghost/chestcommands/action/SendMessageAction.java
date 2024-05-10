/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.action;

import org.bukkit.entity.Player;

import me.filoghost.chestcommands.placeholder.PlaceholderString;
import me.filoghost.fcommons.Colors;

public class SendMessageAction implements Action {

    private final PlaceholderString message;

    public SendMessageAction(final String serializedAction) { this.message = PlaceholderString.of(Colors.addColors(serializedAction)); }

    @Override
    public void execute(final Player player) { player.sendMessage(this.message.getValue(player)); }

}
