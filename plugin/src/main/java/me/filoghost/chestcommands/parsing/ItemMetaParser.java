/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.parsing;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.fcommons.Strings;
import me.filoghost.fcommons.collection.EnumLookupRegistry;
import me.filoghost.fcommons.collection.LookupRegistry;

public final class ItemMetaParser {

    private static final LookupRegistry<DyeColor> DYE_COLORS_REGISTRY = EnumLookupRegistry.fromEnumValues(DyeColor.class);
    private static final LookupRegistry<PatternType> PATTERN_TYPES_REGISTRY = EnumLookupRegistry.fromEnumValues(PatternType.class);

    private ItemMetaParser() {}

    public static Color parseRGBColor(final String input) throws ParseException {
        final String[] split = Strings.splitAndTrim(input, ",");

        if (split.length != 3) {
            throw new ParseException(Errors.Parsing.invalidColorFormat);
        }

        final int red = ItemMetaParser.parseColor(split[0], "red");
        final int green = ItemMetaParser.parseColor(split[1], "green");
        final int blue = ItemMetaParser.parseColor(split[2], "blue");

        return Color.fromRGB(red, green, blue);
    }

    private static int parseColor(final String valueString, final String colorName) throws ParseException {
        int value;

        try {
            value = NumberParser.getInteger(valueString);
        } catch (final ParseException e) {
            throw new ParseException(Errors.Parsing.invalidColorNumber(valueString, colorName), e);
        }

        if (value < 0 || value > 255) {
            throw new ParseException(Errors.Parsing.invalidColorRange(valueString, colorName));
        }

        return value;
    }

    public static DyeColor parseDyeColor(final String input) throws ParseException {
        final DyeColor dyeColor = ItemMetaParser.DYE_COLORS_REGISTRY.lookup(input);
        if (dyeColor == null) {
            throw new ParseException(Errors.Parsing.unknownDyeColor(input));
        }
        return dyeColor;
    }

    public static Pattern parseBannerPattern(final String input) throws ParseException {
        final String[] split = Strings.splitAndTrim(input, ":");
        if (split.length != 2) {
            throw new ParseException(Errors.Parsing.invalidPatternFormat);
        }

        final PatternType patternType = ItemMetaParser.PATTERN_TYPES_REGISTRY.lookup(split[0]);
        if (patternType == null) {
            throw new ParseException(Errors.Parsing.unknownPatternType(split[0]));
        }
        final DyeColor patternColor = ItemMetaParser.parseDyeColor(split[1]);

        return new Pattern(patternColor, patternType);
    }
}
