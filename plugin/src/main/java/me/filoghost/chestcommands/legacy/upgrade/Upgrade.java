/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.legacy.upgrade;

import java.util.List;

import me.filoghost.chestcommands.config.ConfigManager;

public class Upgrade {

    private final String id;
    private final MultiTaskSupplier upgradeTasksSupplier;

    public Upgrade(final String id, final MultiTaskSupplier taskSupplier) {
        this.id = id;
        this.upgradeTasksSupplier = taskSupplier;
    }

    public String getID() { return this.id; }

    public List<UpgradeTask> createUpgradeTasks(final ConfigManager configManager) throws UpgradeTaskException {
        return this.upgradeTasksSupplier.getTasks(configManager);
    }

    @FunctionalInterface
    public interface SingleTaskSupplier {

        UpgradeTask getTask(ConfigManager configManager) throws UpgradeTaskException;

    }

    @FunctionalInterface
    public interface MultiTaskSupplier {

        List<UpgradeTask> getTasks(ConfigManager configManager) throws UpgradeTaskException;

    }

}
