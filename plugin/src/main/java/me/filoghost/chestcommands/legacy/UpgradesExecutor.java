/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.legacy;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import me.filoghost.chestcommands.config.ConfigManager;
import me.filoghost.chestcommands.legacy.upgrade.Upgrade;
import me.filoghost.chestcommands.legacy.upgrade.UpgradeTask;
import me.filoghost.chestcommands.legacy.upgrade.UpgradeTaskException;
import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.fcommons.logging.ErrorCollector;
import me.filoghost.fcommons.logging.Log;

public class UpgradesExecutor {

    private final ConfigManager configManager;
    private boolean allUpgradesSuccessful;
    private UpgradesDoneRegistry upgradesDoneRegistry;

    public UpgradesExecutor(final ConfigManager configManager) { this.configManager = configManager; }

    public boolean run(final boolean isFreshInstall, final ErrorCollector errorCollector) throws UpgradeExecutorException {
        this.allUpgradesSuccessful = true;
        final Path upgradesDoneFile = this.configManager.getRootDataFolder().resolve(".upgrades-done");

        try {
            this.upgradesDoneRegistry = new UpgradesDoneRegistry(upgradesDoneFile);
        } catch (final IOException e) {
            // Upgrades can't proceed if metadata file is not read correctly
            throw new UpgradeExecutorException(Errors.Upgrade.metadataReadError(upgradesDoneFile), e);
        }

        if (isFreshInstall) {
            // Mark all currently existing upgrades as already done, assuming default
            // configuration files are up to date
            this.upgradesDoneRegistry.setAllDone();
        } else {
            // Run missing upgrades
            final Backup backup = Backup.newTimestampedBackup(this.configManager.getRootDataFolder());
            this.runMissingUpgrades(backup, errorCollector);
        }

        try {
            this.upgradesDoneRegistry.save();
        } catch (final IOException e) {
            // Upgrades can't proceed if metadata file is not saved correctly
            throw new UpgradeExecutorException(Errors.Upgrade.metadataSaveError(upgradesDoneFile), e);
        }

        return this.allUpgradesSuccessful;
    }

    private void runMissingUpgrades(final Backup backup, final ErrorCollector errorCollector) {
        for (final Upgrade upgrade : UpgradeList.getOrderedUpgrades()) {
            if (!this.upgradesDoneRegistry.isDone(upgrade)) {
                final boolean allTasksSuccessful = this.tryRunUpgradeTasks(upgrade, backup, errorCollector);

                // Consider an upgrade "done" if all its tasks were completed successfully
                if (allTasksSuccessful) {
                    this.upgradesDoneRegistry.setDone(upgrade);
                } else {
                    this.allUpgradesSuccessful = false;
                }
            }
        }
    }

    private boolean tryRunUpgradeTasks(final Upgrade upgrade, final Backup backup, final ErrorCollector errorCollector) {
        boolean allTasksSuccessful = true;

        List<UpgradeTask> upgradeTasks;
        try {
            upgradeTasks = upgrade.createUpgradeTasks(this.configManager);
        } catch (final UpgradeTaskException e) {
            errorCollector.add(e, Errors.Upgrade.failedToPrepareUpgradeTasks);
            return false;
        }

        for (final UpgradeTask upgradeTask : upgradeTasks) {
            try {
                final boolean modified = upgradeTask.runAndBackupIfNecessary(backup);
                if (modified) {
                    Log.info("Automatically upgraded configuration file \"" + upgradeTask.getUpgradedFile() + "\". "
                            + "A backup of the old file has been saved.");
                }
            } catch (final UpgradeTaskException e) {
                allTasksSuccessful = false;
                errorCollector.add(e, Errors.Upgrade.failedSingleUpgrade(upgradeTask.getOriginalFile()));
            }
        }

        return allTasksSuccessful;
    }

}
