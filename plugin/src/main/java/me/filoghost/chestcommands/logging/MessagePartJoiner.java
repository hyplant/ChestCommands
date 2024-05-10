/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.logging;

import java.util.List;

import me.filoghost.fcommons.Strings;

class MessagePartJoiner {

    private final StringBuilder output;

    private String previousMessagePart;
    private boolean appendedFirstSentenceSeparator;

    public static String join(final List<String> messageParts) {
        final int estimateLength = MessagePartJoiner.getEstimateLength(messageParts);
        final MessagePartJoiner errorMessageBuilder = new MessagePartJoiner(estimateLength);
        for (final String messagePart : messageParts) {
            errorMessageBuilder.append(messagePart);
        }
        return errorMessageBuilder.build();
    }

    private static int getEstimateLength(final List<String> messageParts) {
        int estimateLength = 0;

        // Length of message parts
        for (final String messagePart : messageParts) {
            estimateLength += messagePart.length();
        }

        // Length of separators in between
        estimateLength += (messageParts.size() - 1) * 2;

        return estimateLength;
    }

    private MessagePartJoiner(final int estimateLength) { this.output = new StringBuilder(estimateLength); }

    private void append(final String messagePart) {
        this.appendSeparator();
        this.appendMessagePart(messagePart);

        this.previousMessagePart = messagePart;
    }

    private void appendMessagePart(final String messagePart) {
        if (this.previousMessagePart == null || this.previousMessagePart.endsWith(".")) {
            this.output.append(Strings.capitalizeFirst(messagePart));
        } else {
            this.output.append(messagePart);
        }
    }

    private void appendSeparator() {
        if (this.previousMessagePart != null) {
            if (this.previousMessagePart.endsWith(".")) {
                this.output.append(" ");
                this.appendedFirstSentenceSeparator = false;

            } else if (!this.appendedFirstSentenceSeparator) {
                this.output.append(": ");
                this.appendedFirstSentenceSeparator = true;

            } else {
                this.output.append(", ");
            }
        }
    }

    private String build() { return this.output.toString(); }

}
