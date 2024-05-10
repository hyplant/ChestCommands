/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;

public class ClickPermissionAttribute implements IconAttribute {

    private final String clickPermission;

    public ClickPermissionAttribute(final String clickPermission, final AttributeErrorHandler errorHandler) {
        this.clickPermission = clickPermission;
    }

    @Override
    public void apply(final InternalConfigurableIcon icon) { icon.setClickPermission(this.clickPermission); }

}
