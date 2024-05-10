/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.parsing.MaterialParser;
import me.filoghost.chestcommands.parsing.ParseException;
import org.bukkit.Material;

public class MaterialAttribute implements IconAttribute {

    private final Material material;

    public MaterialAttribute(final String serializedMaterial, final AttributeErrorHandler errorHandler) throws ParseException {
        this.material = MaterialParser.parseMaterial(serializedMaterial);
    }

    @Override
    public void apply(final InternalConfigurableIcon icon) { icon.setMaterial(this.material); }

}
