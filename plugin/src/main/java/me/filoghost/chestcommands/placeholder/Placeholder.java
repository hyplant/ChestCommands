/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.placeholder;

import org.bukkit.plugin.Plugin;

import me.filoghost.chestcommands.api.PlaceholderReplacer;

public class Placeholder {

    private final Plugin plugin;
    private final PlaceholderReplacer placeholderReplacer;

    public Placeholder(final Plugin plugin, final PlaceholderReplacer placeholderReplacer) {
        this.plugin = plugin;
        this.placeholderReplacer = placeholderReplacer;
    }

    public Plugin getPlugin() { return this.plugin; }

    public PlaceholderReplacer getReplacer() { return this.placeholderReplacer; }

}
