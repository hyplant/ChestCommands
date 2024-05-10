/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.action;

import me.filoghost.chestcommands.hook.VaultEconomyHook;
import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.chestcommands.parsing.NumberParser;
import me.filoghost.chestcommands.parsing.ParseException;
import org.bukkit.entity.Player;

public class GiveMoneyAction implements Action {

    private final double moneyToGive;

    public GiveMoneyAction(final String serializedAction) throws ParseException {
        this.moneyToGive = NumberParser.getStrictlyPositiveDouble(serializedAction);
    }

    @Override
    public void execute(final Player player) {
        if (VaultEconomyHook.INSTANCE.isEnabled()) {
            VaultEconomyHook.giveMoney(player, this.moneyToGive);
        } else {
            player.sendMessage(Errors.User.configurationError("Vault with a compatible economy plugin not found"));
        }
    }

}
