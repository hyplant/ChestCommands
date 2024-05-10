/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.placeholder;

import me.filoghost.chestcommands.api.PlaceholderReplacer;
import me.filoghost.chestcommands.hook.PlaceholderAPIHook;
import me.filoghost.chestcommands.placeholder.scanner.PlaceholderMatch;
import me.filoghost.chestcommands.placeholder.scanner.PlaceholderScanner;
import me.filoghost.fcommons.Preconditions;
import me.filoghost.fcommons.logging.Log;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderManager {

    private static final List<StaticPlaceholder> staticPlaceholders = new ArrayList<>();
    private static final PlaceholderRegistry dynamicPlaceholderRegistry = new PlaceholderRegistry();
    private static final PlaceholderCache placeholderCache = new PlaceholderCache();
    static {
        for (final DefaultPlaceholder placeholder : DefaultPlaceholder.values()) {
            PlaceholderManager.dynamicPlaceholderRegistry.registerInternalPlaceholder(placeholder.getIdentifier(),
                    placeholder.getReplacer());
        }
    }

    public static boolean hasDynamicPlaceholders(final List<String> list) {
        for (final String element : list) {
            if (PlaceholderManager.hasDynamicPlaceholders(element)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasDynamicPlaceholders(final String text) {
        if (new PlaceholderScanner(text).containsAny()) {
            return true;
        }

        if (PlaceholderAPIHook.INSTANCE.isEnabled() && PlaceholderAPIHook.hasPlaceholders(text)) {
            return true;
        }

        return false;
    }

    public static String replaceDynamicPlaceholders(String text, final Player player) {
        text = new PlaceholderScanner(text).replace(match -> PlaceholderManager.getReplacement(match, player));

        if (PlaceholderAPIHook.INSTANCE.isEnabled()) {
            text = PlaceholderAPIHook.setPlaceholders(text, player);
        }

        return text;
    }

    private static @Nullable String getReplacement(final PlaceholderMatch placeholderMatch, final Player player) {
        final Placeholder placeholder = PlaceholderManager.dynamicPlaceholderRegistry.getPlaceholder(placeholderMatch);

        if (placeholder == null) {
            return null; // Placeholder not found
        }

        return PlaceholderManager.placeholderCache.computeIfAbsent(placeholderMatch, player, () -> {
            try {
                return placeholder.getReplacer().getReplacement(player, placeholderMatch.getArgument());
            } catch (final Throwable t) {
                Log.severe("Encountered an exception while replacing the placeholder \"" + placeholderMatch.getIdentifier()
                        + "\" registered by the plugin \"" + placeholder.getPlugin().getName() + "\"", t);
                return "[PLACEHOLDER ERROR]";
            }
        });
    }

    public static void setStaticPlaceholders(final List<StaticPlaceholder> staticPlaceholders) {
        PlaceholderManager.staticPlaceholders.clear();
        PlaceholderManager.staticPlaceholders.addAll(staticPlaceholders);
    }

    public static boolean hasStaticPlaceholders(final List<String> list) {
        for (final String element : list) {
            if (PlaceholderManager.hasStaticPlaceholders(element)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasStaticPlaceholders(final String text) {
        for (final StaticPlaceholder staticPlaceholder : PlaceholderManager.staticPlaceholders) {
            if (text.contains(staticPlaceholder.getIdentifier())) {
                return true;
            }
        }
        return false;
    }

    public static String replaceStaticPlaceholders(String text) {
        for (final StaticPlaceholder staticPlaceholder : PlaceholderManager.staticPlaceholders) {
            text = text.replace(staticPlaceholder.getIdentifier(), staticPlaceholder.getReplacement());
        }
        return text;
    }

    public static void registerPluginPlaceholder(final Plugin plugin, final String identifier,
            final PlaceholderReplacer placeholderReplacer) {
        Preconditions.notNull(plugin, "plugin");
        PlaceholderManager.checkIdentifierArgument(identifier);
        Preconditions.notNull(placeholderReplacer, "placeholderReplacer");

        PlaceholderManager.dynamicPlaceholderRegistry.registerExternalPlaceholder(plugin, identifier, placeholderReplacer);
    }

    public static boolean unregisterPluginPlaceholder(final Plugin plugin, final String identifier) {
        Preconditions.notNull(plugin, "plugin");
        PlaceholderManager.checkIdentifierArgument(identifier);

        return PlaceholderManager.dynamicPlaceholderRegistry.unregisterExternalPlaceholder(plugin, identifier);
    }

    private static void checkIdentifierArgument(final String identifier) {
        Preconditions.notNull(identifier, "identifier");
        Preconditions.checkArgument(1 <= identifier.length() && identifier.length() <= 30, "identifier length must be between 1 and 30");
        Preconditions.checkArgument(identifier.matches("[a-zA-Z0-9_]+"), "identifier must contain only letters, numbers and underscores");
    }

    public static void onTick() { PlaceholderManager.placeholderCache.onTick(); }

}
