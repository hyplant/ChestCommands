/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.util;

import org.jetbrains.annotations.NotNull;

import me.filoghost.fcommons.Strings;

public class Utils {

    public static String formatEnum(@NotNull final Enum<?> enumValue) {
        return Strings.capitalizeFully(enumValue.name().replace("_", " "));
    }

    public static String addYamlExtension(@NotNull final String fileName) {
        if (fileName.toLowerCase().endsWith(".yml")) {
            return fileName;
        } else {
            return fileName + ".yml";
        }
    }

}
