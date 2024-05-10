/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;

public class PositionAttribute implements IconAttribute {

    private final int position;

    public PositionAttribute(final int position, final AttributeErrorHandler errorHandler) { this.position = position; }

    public int getPosition() { return this.position; }

    @Override
    public void apply(final InternalConfigurableIcon icon) {
        // Position has no effect on the icon itself
    }
}
