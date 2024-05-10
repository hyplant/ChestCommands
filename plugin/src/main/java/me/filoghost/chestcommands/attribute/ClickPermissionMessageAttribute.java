/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.fcommons.Colors;

public class ClickPermissionMessageAttribute implements IconAttribute {

    private final String message;

    public ClickPermissionMessageAttribute(final String serializedMessage, final AttributeErrorHandler errorHandler) {
        this.message = Colors.addColors(serializedMessage);
    }

    @Override
    public void apply(final InternalConfigurableIcon icon) { icon.setNoClickPermissionMessage(this.message); }

}
