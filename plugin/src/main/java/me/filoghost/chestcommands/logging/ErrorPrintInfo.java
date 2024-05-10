/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.logging;

import java.util.List;

class ErrorPrintInfo {

    private final int index;
    private final List<String> message;
    private final String details;
    private final Throwable cause;

    protected ErrorPrintInfo(final int index, final List<String> message, final String details, final Throwable cause) {
        this.index = index;
        this.message = message;
        this.details = details;
        this.cause = cause;
    }

    public int getIndex() { return this.index; }

    public List<String> getMessage() { return this.message; }

    public String getDetails() { return this.details; }

    public Throwable getCause() { return this.cause; }

}
