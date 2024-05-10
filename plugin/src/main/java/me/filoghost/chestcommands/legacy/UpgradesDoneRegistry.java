/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.legacy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import me.filoghost.chestcommands.legacy.upgrade.Upgrade;

public class UpgradesDoneRegistry {

    private final Path saveFile;
    private final Set<String> upgradesDone;
    private boolean needSave;

    public UpgradesDoneRegistry(final Path saveFile) throws IOException {
        this.saveFile = saveFile;
        this.upgradesDone = new HashSet<>();

        if (Files.isRegularFile(saveFile)) {
            try (Stream<String> lines = Files.lines(saveFile)) {
                lines.filter(s -> !s.startsWith("#")).forEach(this.upgradesDone::add);
            }
        }
    }

    public void setAllDone() {
        for (final Upgrade upgrade : UpgradeList.getOrderedUpgrades()) {
            this.setDone(upgrade);
        }
    }

    public void setDone(final Upgrade upgrade) {
        if (this.upgradesDone.add(upgrade.getID())) {
            this.needSave = true;
        }
    }

    public boolean isDone(final Upgrade upgrade) { return this.upgradesDone.contains(upgrade.getID()); }

    public void save() throws IOException {
        if (this.needSave) {
            final List<String> lines = new ArrayList<>();
            lines.add("#");
            lines.add("# WARNING: manually editing this file is not recommended");
            lines.add("#");
            lines.addAll(this.upgradesDone);
            Files.createDirectories(this.saveFile.getParent());
            Files.write(this.saveFile, lines);
            this.needSave = false;
        }
    }

}
