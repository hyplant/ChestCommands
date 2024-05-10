/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.legacy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import me.filoghost.fcommons.Preconditions;

public class Backup {

    private final Path dataFolder;
    private final Path backupFolder;
    private final Path infoFile;

    public Backup(final Path dataFolder, final String backupName) {
        this.dataFolder = dataFolder;
        final Path backupsFolder = dataFolder.resolve("old_files");
        this.backupFolder = backupsFolder.resolve(backupName);
        this.infoFile = backupsFolder.resolve("readme.txt");
    }

    public static Backup newTimestampedBackup(final Path dataFolder) {
        final String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd-HH.mm"));
        final String backupName = "backup_" + date;
        return new Backup(dataFolder, backupName);
    }

    public void addFile(final Path fileToBackup) throws IOException {
        Preconditions.checkArgument(fileToBackup.startsWith(this.dataFolder), "file is not inside data folder");
        final Path destination = this.backupFolder.resolve(this.dataFolder.relativize(fileToBackup));
        Files.createDirectories(destination.getParent());

        // Add backup file if not already present
        if (!Files.isRegularFile(destination)) {
            Files.copy(fileToBackup, destination);
        }

        // Add README file if not already present
        if (!Files.isRegularFile(this.infoFile)) {
            Files.write(this.infoFile,
                    Arrays.asList("Files in this folders are copies of original configuration files that have been automatically upgraded.",
                            "", "Note: some configuration upgrades remove comments and other formatting (such as empty lines)."));
        }
    }

}
