/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.parsing;

public class ParseException extends Exception {

    private static final long serialVersionUID = 1L;

    public ParseException(final String message) { super(message); }

    public ParseException(final String message, final ParseException cause) { super(message, cause); }

}
