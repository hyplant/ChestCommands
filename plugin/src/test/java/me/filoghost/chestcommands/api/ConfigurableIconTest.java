/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.api;

import org.assertj.core.api.Assertions;
import org.bukkit.Material;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import me.filoghost.chestcommands.icon.BaseConfigurableIcon;
import me.filoghost.chestcommands.test.BukkitMocks;

class ConfigurableIconTest {

    @BeforeAll
    static void beforeAll() {
        ChestCommandsAPI.registerPlaceholder(BukkitMocks.PLUGIN, "test", (player, argument) -> {
            if (argument != null) {
                return argument;
            } else {
                return "EMPTY";
            }
        });
    }

    @AfterAll
    static void afterAll() { ChestCommandsAPI.unregisterPlaceholder(BukkitMocks.PLUGIN, "test"); }

    @Test
    void customPlaceholderReplacements() {
        final BaseConfigurableIcon icon = (BaseConfigurableIcon) ConfigurableIcon.create(Material.STONE);
        icon.setPlaceholdersEnabled(true);
        icon.setName("{test: start} abc {test} {MockPlugin/test} {test: 1} {mOckPLuGin/tEsT: 2} 123 {test: end}");
        Assertions.assertThat(icon.renderName(BukkitMocks.PLAYER)).isEqualTo("start abc EMPTY EMPTY 1 2 123 end");
    }

    @Test
    void placeholdersEnabled() {
        final BaseConfigurableIcon icon = (BaseConfigurableIcon) ConfigurableIcon.create(Material.STONE);
        icon.setPlaceholdersEnabled(true);
        icon.setName("abc {player} {test} 123");
        Assertions.assertThat(icon.renderName(BukkitMocks.PLAYER)).isEqualTo("abc " + BukkitMocks.PLAYER.getName() + " EMPTY 123");
    }

    @Test
    void placeholdersNotEnabled() {
        final BaseConfigurableIcon icon = (BaseConfigurableIcon) ConfigurableIcon.create(Material.STONE);
        icon.setName("abc {player} {test} 123");
        Assertions.assertThat(icon.renderName(BukkitMocks.PLAYER)).isEqualTo("abc {player} {test} 123");
    }

    @Test
    void dynamicPlaceholderRegistration() {
        final BaseConfigurableIcon icon = (BaseConfigurableIcon) ConfigurableIcon.create(Material.STONE);
        icon.setPlaceholdersEnabled(true);
        icon.setName("abc {temp} 123");

        try {
            ChestCommandsAPI.registerPlaceholder(BukkitMocks.PLUGIN, "temp", (player, argument) -> "value");

            Assertions.assertThat(icon.renderName(BukkitMocks.PLAYER)).isEqualTo("abc value 123");
        } finally {
            ChestCommandsAPI.unregisterPlaceholder(BukkitMocks.PLUGIN, "temp");
        }
    }

    @Test
    void placeholderUnregistration() {
        final BaseConfigurableIcon icon = (BaseConfigurableIcon) ConfigurableIcon.create(Material.STONE);
        icon.setPlaceholdersEnabled(true);
        icon.setName("abc {temp} 123");

        try {
            ChestCommandsAPI.registerPlaceholder(BukkitMocks.PLUGIN, "temp", (player, argument) -> "value");
        } finally {
            ChestCommandsAPI.unregisterPlaceholder(BukkitMocks.PLUGIN, "temp");
        }

        Assertions.assertThat(icon.renderName(BukkitMocks.PLAYER)).isEqualTo("abc {temp} 123");
    }

}
