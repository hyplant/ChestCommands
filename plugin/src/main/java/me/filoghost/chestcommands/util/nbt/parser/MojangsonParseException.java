/*
 * Copyright (C) Jan Schultke SPDX-License-Identifier: MIT
 */
package me.filoghost.chestcommands.util.nbt.parser;

import java.io.IOException;

public class MojangsonParseException extends IOException {

    private static final long serialVersionUID = 1L;

    public MojangsonParseException(final String msg, final String content, final int index) {
        super(msg + " at character " + index + ": " + MojangsonParseException.printErrorLoc(content, index));
    }

    private static String printErrorLoc(final String content, final int index) {
        final StringBuilder builder = new StringBuilder();
        final int i = Math.min(content.length(), index);
        if (i > 35) {
            builder.append("...");
        }
        builder.append(content, Math.max(0, i - 35), i);
        builder.append("<--[HERE]");

        return builder.toString();
    }

}
