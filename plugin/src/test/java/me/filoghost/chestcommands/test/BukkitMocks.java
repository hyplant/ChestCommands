/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.test;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.mockito.Mockito;

import me.filoghost.chestcommands.DefaultBackendAPI;
import me.filoghost.chestcommands.api.internal.BackendAPI;

public final class BukkitMocks {

    public static final Plugin PLUGIN;
    public static final Player PLAYER;

    static {
        // Server server = mock(Server.class, RETURNS_DEEP_STUBS);
        // Bukkit.setServer(server);
        PLUGIN = Mockito.mock(Plugin.class);
        Mockito.when(BukkitMocks.PLUGIN.getName()).thenReturn("MockPlugin");
        PLAYER = Mockito.mock(Player.class);
        Mockito.when(BukkitMocks.PLAYER.getName()).thenReturn("filoghost");
        BackendAPI.setImplementation(new DefaultBackendAPI());
    }

}