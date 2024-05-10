/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.placeholder.scanner;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

public class PlaceholderScanner {

    private final String input;
    private final int inputLength;

    private int lastAppendIndex;
    private int placeholderStartIndex;
    private int index;
    private boolean stopExecution;

    public PlaceholderScanner(final String input) {
        this.input = input;
        this.inputLength = input.length();
    }

    public boolean containsAny() {
        final AtomicBoolean placeholderFound = new AtomicBoolean(false);

        this.scan(identifier -> {
            this.stopExecution = true;
            placeholderFound.set(true);
        });

        return placeholderFound.get();
    }

    public String replace(final Function<PlaceholderMatch, String> replaceFunction) {
        final StringBuilder output = new StringBuilder();

        this.scan(identifier -> {
            final String replacement = replaceFunction.apply(identifier);

            if (replacement != null) {
                // Append preceding text and replacement
                output.append(this.input, this.lastAppendIndex, this.placeholderStartIndex);
                output.append(replacement);
                this.lastAppendIndex = this.index + 1; // Start next append after the closing tag
            }

            // Else, if no replacement is found, ignore the placeholder replacement and
            // proceed normally
        });

        // Append trailing text
        if (this.lastAppendIndex < this.inputLength) {
            output.append(this.input, this.lastAppendIndex, this.inputLength);
        }

        return output.toString();
    }

    private void scan(final Consumer<PlaceholderMatch> matchCallback) {
        this.index = 0;
        this.placeholderStartIndex = 0;
        this.lastAppendIndex = 0;

        boolean insidePlaceholder = false;

        while (this.index < this.inputLength) {
            final char currentChar = this.input.charAt(this.index);

            if (insidePlaceholder) {
                if (currentChar == '}') {
                    // If the placeholder is "{player}" then the identifier is "player"
                    final String placeholderContent = this.input.substring(this.placeholderStartIndex + 1, this.index); // Skip the opening
                                                                                                                        // tag
                    matchCallback.accept(PlaceholderMatch.parse(placeholderContent));
                    if (this.stopExecution) {
                        return;
                    }

                    insidePlaceholder = false;
                    this.placeholderStartIndex = 0;

                } else if (currentChar == '{') {
                    // Nested placeholder, ignore wrapping placeholder and update placeholder start
                    // index
                    this.placeholderStartIndex = this.index;
                }
            } else {
                if (currentChar == '{') {
                    insidePlaceholder = true;
                    this.placeholderStartIndex = this.index;
                }
            }

            this.index++;
        }
    }

}
