/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.parsing.menu;

import java.util.List;

import com.google.common.collect.ImmutableList;

import me.filoghost.chestcommands.action.Action;
import me.filoghost.fcommons.collection.CollectionUtils;

public class MenuSettings {

    // Required settings
    private final String title;
    private final int rows;

    // Optional settings
    private ImmutableList<String> commands;
    private ImmutableList<Action> openActions;
    private int refreshTicks;

    private MenuOpenItem openItem;

    public MenuSettings(final String title, final int rows) {
        this.title = title;
        this.rows = rows;
    }

    public String getTitle() { return this.title; }

    public int getRows() { return this.rows; }

    public void setCommands(final List<String> commands) { this.commands = CollectionUtils.newImmutableList(commands); }

    public ImmutableList<String> getCommands() { return this.commands; }

    public ImmutableList<Action> getOpenActions() { return this.openActions; }

    public void setOpenActions(final List<Action> openAction) { this.openActions = CollectionUtils.newImmutableList(openAction); }

    public int getRefreshTicks() { return this.refreshTicks; }

    public void setRefreshTicks(final int refreshTicks) { this.refreshTicks = refreshTicks; }

    public MenuOpenItem getOpenItem() { return this.openItem; }

    public void setOpenItem(final MenuOpenItem openItem) { this.openItem = openItem; }

}
