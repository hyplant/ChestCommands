/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.legacy.upgrade;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import me.filoghost.fcommons.config.ConfigErrors;
import me.filoghost.fcommons.config.exception.ConfigLoadException;
import me.filoghost.fcommons.config.exception.ConfigSaveException;

public abstract class RegexUpgradeTask extends UpgradeTask {

    private final Path file;
    private List<String> newContents;
    private Stream<String> linesStream;

    public RegexUpgradeTask(final Path file) { this.file = file; }

    protected abstract void computeRegexChanges();

    @Override
    public final Path getOriginalFile() { return this.file; }

    @Override
    public final Path getUpgradedFile() { return this.file; }

    @Override
    public final void computeChanges() throws ConfigLoadException {
        if (!Files.isRegularFile(this.file)) {
            return;
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(this.file);
        } catch (final IOException e) {
            throw new ConfigLoadException(ConfigErrors.readIOException, e);
        }

        this.linesStream = lines.stream();
        this.computeRegexChanges();

        this.newContents = this.linesStream.collect(Collectors.toList());

        if (!this.newContents.equals(lines)) {
            this.setSaveRequired();
        }
    }

    @Override
    public final void saveChanges() throws ConfigSaveException {
        try {
            Files.write(this.file, this.newContents);
        } catch (final IOException e) {
            throw new ConfigSaveException(ConfigErrors.writeDataIOException, e);
        }
    }

    protected void renameInnerNode(final String oldNode, final String newNode) {
        this.replaceRegex(Pattern.compile("(^\\s+)" + Pattern.quote(oldNode) + "(:)"),
                matcher -> matcher.group(1) + newNode + matcher.group(2));
    }

    protected void replaceRegex(final Pattern regex, final Function<Matcher, String> replaceCallback) {
        this.linesStream = this.linesStream.map(new RegexReplacer(regex, replaceCallback));
    }

}
