/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.placeholder;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.api.PlaceholderReplacer;
import me.filoghost.chestcommands.placeholder.scanner.PlaceholderMatch;
import me.filoghost.fcommons.collection.CaseInsensitiveHashMap;
import me.filoghost.fcommons.collection.CaseInsensitiveLinkedHashMap;
import me.filoghost.fcommons.collection.CaseInsensitiveMap;

public class PlaceholderRegistry {

    // <identifier, placeholder>
    private final CaseInsensitiveMap<Placeholder> internalPlaceholders = new CaseInsensitiveHashMap<>();

    // <identifier, <pluginName, placeholder>>
    private final CaseInsensitiveMap<CaseInsensitiveMap<Placeholder>> externalPlaceholders = new CaseInsensitiveHashMap<>();

    public void registerInternalPlaceholder(final String identifier, final PlaceholderReplacer replacer) {
        this.internalPlaceholders.put(identifier, new Placeholder(ChestCommands.getInstance(), replacer));
    }

    public void registerExternalPlaceholder(final Plugin plugin, final String identifier, final PlaceholderReplacer placeholderReplacer) {
        this.externalPlaceholders.computeIfAbsent(identifier, CaseInsensitiveLinkedHashMap::new).put(plugin.getName(),
                new Placeholder(plugin, placeholderReplacer));
    }

    public boolean unregisterExternalPlaceholder(final Plugin plugin, final String identifier) {
        final CaseInsensitiveMap<Placeholder> externalPlaceholdersByPlugin = this.externalPlaceholders.get(identifier);

        if (externalPlaceholdersByPlugin == null) {
            return false;
        }

        final boolean removed = externalPlaceholdersByPlugin.remove(plugin.getName()) != null;

        if (externalPlaceholdersByPlugin.isEmpty()) {
            this.externalPlaceholders.remove(identifier);
        }

        return removed;
    }

    public @Nullable Placeholder getPlaceholder(final PlaceholderMatch placeholderMatch) {
        final String identifier = placeholderMatch.getIdentifier();

        if (placeholderMatch.getPluginNamespace() == null) {
            final Placeholder internalPlaceholder = this.internalPlaceholders.get(identifier);
            if (internalPlaceholder != null) {
                return internalPlaceholder;
            }
        }

        final CaseInsensitiveMap<Placeholder> externalPlaceholdersByPlugin = this.externalPlaceholders.get(identifier);
        if (externalPlaceholdersByPlugin == null) {
            return null;
        }

        // Find exact replacer if plugin name is specified
        if (placeholderMatch.getPluginNamespace() != null) {
            return externalPlaceholdersByPlugin.get(placeholderMatch.getPluginNamespace());
        }

        // Otherwise try find the first one registered
        if (!externalPlaceholdersByPlugin.isEmpty()) {
            return externalPlaceholdersByPlugin.values().iterator().next();
        }

        return null;
    }

}
