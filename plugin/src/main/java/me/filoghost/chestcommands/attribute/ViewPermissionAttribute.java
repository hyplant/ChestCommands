/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;

public class ViewPermissionAttribute implements IconAttribute {

    private final String viewPermission;

    public ViewPermissionAttribute(final String viewPermission, final AttributeErrorHandler errorHandler) {
        this.viewPermission = viewPermission;
    }

    @Override
    public void apply(final InternalConfigurableIcon icon) { icon.setViewPermission(this.viewPermission); }

}
