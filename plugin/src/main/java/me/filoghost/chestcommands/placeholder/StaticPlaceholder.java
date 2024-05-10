/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.placeholder;

public class StaticPlaceholder {

    private final String identifier;
    private final String replacement;

    public StaticPlaceholder(final String identifier, final String replacement) {
        this.identifier = identifier;
        this.replacement = replacement;
    }

    public String getIdentifier() { return this.identifier; }

    public String getReplacement() { return this.replacement; }

}
