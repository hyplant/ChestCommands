/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.placeholder.scanner;

import java.util.Objects;

import me.filoghost.fcommons.Strings;

public class PlaceholderMatch {

    private final String pluginNamespace;
    private final String identifier;
    private final String argument;

    private PlaceholderMatch(final String pluginNamespace, final String identifier, final String argument) {
        this.pluginNamespace = pluginNamespace;
        this.identifier = identifier;
        this.argument = argument;
    }

    public String getPluginNamespace() { return this.pluginNamespace; }

    public String getIdentifier() { return this.identifier; }

    public String getArgument() { return this.argument; }

    /*
     * Valid formats: {placeholder} {placeholder: argument} {pluginName/identifier}
     * {pluginName/identifier: argument}
     */
    public static PlaceholderMatch parse(final String placeholderContent) {
        String explicitPluginName = null;
        String identifier;
        String argument = null;

        if (placeholderContent.contains(":")) {
            final String[] parts = Strings.splitAndTrim(placeholderContent, ":", 2);
            identifier = parts[0];
            argument = parts[1];
        } else {
            identifier = placeholderContent;
        }

        if (identifier.contains("/")) {
            final String[] parts = Strings.splitAndTrim(identifier, "/", 2);
            explicitPluginName = parts[0];
            identifier = parts[1];
        }

        return new PlaceholderMatch(explicitPluginName, identifier, argument);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        final PlaceholderMatch other = (PlaceholderMatch) obj;
        return Objects.equals(this.pluginNamespace, other.pluginNamespace) && Objects.equals(this.identifier, other.identifier)
                && Objects.equals(this.argument, other.argument);
    }

    @Override
    public int hashCode() { return Objects.hash(this.pluginNamespace, this.identifier, this.argument); }

}
