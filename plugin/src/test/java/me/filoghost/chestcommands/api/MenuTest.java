/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.api;

import org.assertj.core.api.Assertions;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import me.filoghost.chestcommands.test.BukkitMocks;

class MenuTest {

    @Test
    void setIcon() {
        final Menu menu = this.createMenu(1);
        menu.setIcon(0, 0, StaticIcon.create(new ItemStack(Material.APPLE)));

        Assertions.assertThat(menu.getIcon(0, 0)).isNotNull();
    }

    @Test
    void unsetIcon() {
        final Menu menu = this.createMenu(1);
        menu.setIcon(0, 0, StaticIcon.create(new ItemStack(Material.APPLE)));
        menu.setIcon(0, 0, null);

        Assertions.assertThat(menu.getIcon(0, 0)).isNull();
    }

    @Test
    void iterationRowsColumns() {
        final Menu menu = this.createMenu(3);

        for (int row = 0; row < menu.getRows(); row++) {
            for (int column = 0; column < menu.getColumns(); column++) {
                menu.setIcon(row, column, StaticIcon.create(new ItemStack(Material.APPLE)));
            }
        }
    }

    @Test
    void invalidEmptyMenu() {
        Assertions.assertThatIllegalArgumentException().isThrownBy(() -> {
            this.createMenu(0);
        });
    }

    @ParameterizedTest
    @CsvSource({ "9, 0", "0, 9", "-1, 0", "0, -1", })
    void iconOutOfBounds(final int row, final int column) {
        final Menu menu = this.createMenu(1);
        Assertions.assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> {
            menu.getIcon(row, column);
        });
    }

    private Menu createMenu(final int rowCount) { return Menu.create(BukkitMocks.PLUGIN, "Test menu", rowCount); }

}
