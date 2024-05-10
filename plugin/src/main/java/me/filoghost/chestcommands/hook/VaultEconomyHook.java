/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.hook;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import me.filoghost.fcommons.Preconditions;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public enum VaultEconomyHook implements PluginHook {

    INSTANCE;

    private Economy economy;

    @Override
    public void setup() {
        this.economy = null;

        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return;
        }

        final RegisteredServiceProvider<Economy> economyServiceProvider = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (economyServiceProvider == null) {
            return;
        }

        this.economy = economyServiceProvider.getProvider();
    }

    @Override
    public boolean isEnabled() { return this.economy != null; }

    public static double getMoney(final Player player) {
        INSTANCE.checkEnabledState();
        return INSTANCE.economy.getBalance(player, player.getWorld().getName());
    }

    public static boolean hasMoney(final Player player, final double minimum) {
        INSTANCE.checkEnabledState();
        VaultEconomyHook.checkPositiveAmount(minimum);

        final double balance = INSTANCE.economy.getBalance(player, player.getWorld().getName());
        return balance >= minimum;
    }

    /*
     * Returns true if the operation was successful.
     */
    public static boolean takeMoney(final Player player, final double amount) {
        INSTANCE.checkEnabledState();
        VaultEconomyHook.checkPositiveAmount(amount);

        final EconomyResponse response = INSTANCE.economy.withdrawPlayer(player, player.getWorld().getName(), amount);
        return response.transactionSuccess();
    }

    public static boolean giveMoney(final Player player, final double amount) {
        INSTANCE.checkEnabledState();
        VaultEconomyHook.checkPositiveAmount(amount);

        final EconomyResponse response = INSTANCE.economy.depositPlayer(player, player.getWorld().getName(), amount);
        return response.transactionSuccess();
    }

    private static void checkPositiveAmount(final double amount) {
        Preconditions.checkArgument(amount >= 0.0, "amount cannot be negative");
    }

    public static String formatMoney(final double amount) {
        if (INSTANCE.economy != null) {
            return INSTANCE.economy.format(amount);
        } else {
            return Double.toString(amount);
        }
    }
}
