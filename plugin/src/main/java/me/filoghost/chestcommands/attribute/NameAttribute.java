/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.config.Settings;
import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.fcommons.Colors;

public class NameAttribute implements IconAttribute {

    private final String name;

    public NameAttribute(final String name, final AttributeErrorHandler errorHandler) { this.name = this.colorName(name); }

    private String colorName(final String name) {
        if (!name.isEmpty()) {
            return Settings.get().default_color__name + Colors.addColors(name);
        } else {
            return name;
        }
    }

    @Override
    public void apply(final InternalConfigurableIcon icon) { icon.setName(this.name); }

}
