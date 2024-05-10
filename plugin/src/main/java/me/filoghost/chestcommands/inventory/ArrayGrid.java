/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.inventory;

import org.jetbrains.annotations.Nullable;

public class ArrayGrid<T> extends Grid<T> {

    private final T[] elements;

    @SuppressWarnings("unchecked")
    public ArrayGrid(final int rows, final int columns) {
        super(rows, columns);
        this.elements = (T[]) new Object[this.getSize()];
    }

    @Override
    protected @Nullable T getByIndex0(final int ordinalIndex) { return this.elements[ordinalIndex]; }

    @Override
    protected void setByIndex0(final int ordinalIndex, @Nullable final T element) { this.elements[ordinalIndex] = element; }

}
