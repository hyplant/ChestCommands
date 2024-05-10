/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.parsing;

import org.bukkit.Material;

import me.filoghost.chestcommands.logging.Errors.Parsing;
import me.filoghost.fcommons.MaterialsHelper;

public class MaterialParser {

    public static Material parseMaterial(final String materialName) throws ParseException {
        final Material material = MaterialsHelper.matchMaterial(materialName);
        if (material == null) {
            throw new ParseException(Parsing.unknownMaterial(materialName));
        }
        return material;
    }

}
