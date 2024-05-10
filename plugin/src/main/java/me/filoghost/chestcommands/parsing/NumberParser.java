/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.parsing;

import me.filoghost.chestcommands.logging.Errors;

public class NumberParser {

    public static double getStrictlyPositiveDouble(final String input) throws ParseException {
        final double value = NumberParser.getDouble(input);
        NumberParser.check(value > 0.0, Errors.Parsing.strictlyPositive);
        return value;
    }

    private static double getDouble(final String input) throws ParseException {
        try {
            return Double.parseDouble(input);
        } catch (final NumberFormatException ex) {
            throw new ParseException(Errors.Parsing.invalidDecimal);
        }
    }

    public static float getFloat(final String input) throws ParseException {
        try {
            return Float.parseFloat(input);
        } catch (final NumberFormatException ex) {
            throw new ParseException(Errors.Parsing.invalidDecimal);
        }
    }

    public static short getPositiveShort(final String input) throws ParseException {
        final short value = NumberParser.getShort(input);
        NumberParser.check(value >= 0, Errors.Parsing.zeroOrPositive);
        return value;
    }

    private static short getShort(final String input) throws ParseException {
        try {
            return Short.parseShort(input);
        } catch (final NumberFormatException ex) {
            throw new ParseException(Errors.Parsing.invalidShort);
        }
    }

    public static int getStrictlyPositiveInteger(final String input) throws ParseException {
        final int value = NumberParser.getInteger(input);
        NumberParser.check(value > 0, Errors.Parsing.strictlyPositive);
        return value;
    }

    public static int getInteger(final String input) throws ParseException {
        try {
            return Integer.parseInt(input);
        } catch (final NumberFormatException ex) {
            throw new ParseException(Errors.Parsing.invalidInteger);
        }
    }

    private static void check(final boolean expression, final String errorMessage) throws ParseException {
        if (!expression) {
            throw new ParseException(errorMessage);
        }
    }
}
