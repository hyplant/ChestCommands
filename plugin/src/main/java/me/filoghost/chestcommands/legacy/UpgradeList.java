/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.legacy;

import com.google.common.collect.ImmutableList;
import me.filoghost.chestcommands.config.ConfigManager;
import me.filoghost.chestcommands.legacy.upgrade.Upgrade;
import me.filoghost.chestcommands.legacy.upgrade.UpgradeTask;
import me.filoghost.chestcommands.legacy.upgrade.UpgradeTaskException;
import me.filoghost.chestcommands.legacy.v4_0.V4_0_LangUpgradeTask;
import me.filoghost.chestcommands.legacy.v4_0.V4_0_MenuConfigUpgradeTask;
import me.filoghost.chestcommands.legacy.v4_0.V4_0_MenuRawTextFileUpgradeTask;
import me.filoghost.chestcommands.legacy.v4_0.V4_0_PlaceholdersUpgradeTask;
import me.filoghost.chestcommands.legacy.v4_0.V4_0_SettingsUpgradeTask;
import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.fcommons.collection.CollectionUtils;
import me.filoghost.fcommons.config.ConfigLoader;
import me.filoghost.fcommons.config.exception.ConfigException;
import me.filoghost.fcommons.logging.Log;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class UpgradeList {

    /*
     * Note: order of declaration determines order of execution
     */
    private static final ImmutableList<Upgrade> orderedUpgrades = ImmutableList.of(
            // Edit the raw text first
            UpgradeList.multiTaskUpgrade("v4.0-menus-rename",
                    configManager -> UpgradeList.createMenuTasks(configManager, V4_0_MenuRawTextFileUpgradeTask::new)),

            // Manipulate the configuration after editing the raw text
            UpgradeList.multiTaskUpgrade("v4.0-menus-reformat", configManager -> {
                final String legacyCommandSeparator = UpgradeList.readLegacyCommandSeparator(configManager);
                return UpgradeList.createMenuTasks(configManager,
                        file -> new V4_0_MenuConfigUpgradeTask(configManager, file, legacyCommandSeparator));
            }),

            // Upgrade config after reading the command separator for menus
            UpgradeList.singleTaskUpgrade("v4.0-config", V4_0_SettingsUpgradeTask::new),
            UpgradeList.singleTaskUpgrade("v4.0-placeholders", V4_0_PlaceholdersUpgradeTask::new),
            UpgradeList.singleTaskUpgrade("v4.0-lang", V4_0_LangUpgradeTask::new));

    private static Upgrade singleTaskUpgrade(final String id, final Upgrade.SingleTaskSupplier upgradeTaskSupplier) {
        return new Upgrade(id, configManager -> Collections.singletonList(upgradeTaskSupplier.getTask(configManager)));
    }

    private static Upgrade multiTaskUpgrade(final String id, final Upgrade.MultiTaskSupplier upgradeTasksSupplier) {
        return new Upgrade(id, upgradeTasksSupplier);
    }

    private static List<UpgradeTask> createMenuTasks(final ConfigManager configManager, final Function<Path, UpgradeTask> menuTaskSupplier)
            throws UpgradeTaskException {
        final List<Path> menuFiles = UpgradeList.getMenuFiles(configManager);
        return CollectionUtils.toArrayList(menuFiles, menuTaskSupplier);
    }

    private static List<Path> getMenuFiles(final ConfigManager configManager) throws UpgradeTaskException {
        try {
            return configManager.getMenuFiles();
        } catch (final IOException e) {
            throw new UpgradeTaskException(Errors.Upgrade.menuListIOException, e);
        }
    }

    private static @Nullable String readLegacyCommandSeparator(final ConfigManager configManager) {
        final ConfigLoader settingsConfigLoader = configManager.getConfigLoader("config.yml");

        if (!settingsConfigLoader.fileExists()) {
            return null;
        }

        try {
            return settingsConfigLoader.load().getString("multiple-commands-separator");
        } catch (final ConfigException e) {
            Log.warning("Failed to load \"" + settingsConfigLoader.getFile() + "\", assuming default command separator \";\".");
            return null;
        }
    }

    public static ImmutableList<Upgrade> getOrderedUpgrades() { return UpgradeList.orderedUpgrades; }
}
