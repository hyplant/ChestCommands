/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.action;

import me.filoghost.chestcommands.parsing.ItemStackParser;
import me.filoghost.chestcommands.parsing.ParseException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveItemAction implements Action {

    private final ItemStack itemToGive;

    public GiveItemAction(final String serializedAction) throws ParseException {
        final ItemStackParser reader = new ItemStackParser(serializedAction, true);
        reader.checkNotAir();
        this.itemToGive = reader.createStack();
    }

    @Override
    public void execute(final Player player) { player.getInventory().addItem(this.itemToGive.clone()); }

}
