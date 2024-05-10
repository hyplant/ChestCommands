/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.inventory;

import org.jetbrains.annotations.Nullable;

import me.filoghost.fcommons.Preconditions;

/*
 * Example: There 3 rows and 9 columns. The number inside the cells is the
 * index. <--- Column ---> 0 1 2 3 4 5 6 7 8 ^
 * +--------------------------------------------+ | 0 | 0 | 1 | 2 | 3 | 4 | 5 |
 * 6 | 7 | 8 | |----+----+----+----+----+----+----+----+----| Row 1 | 9 | 10 |
 * 11 | 12 | 13 | 14 | 15 | 16 | 17 |
 * |----+----+----+----+----+----+----+----+----| | 2 | 18 | 19 | 20 | 21 | 22 |
 * 23 | 24 | 25 | 26 | v +--------------------------------------------+
 */
public abstract class Grid<T> {

    private final int rows, columns, size;

    public Grid(final int rows, final int columns) {
        this.rows = rows;
        this.columns = columns;
        this.size = rows * columns;
    }

    public final void set(final int row, final int column, @Nullable final T element) {
        this.setByIndex(this.toOrdinalIndex(row, column), element);
    }

    public final @Nullable T get(final int row, final int column) { return this.getByIndex(this.toOrdinalIndex(row, column)); }

    public final @Nullable T getByIndex(final int ordinalIndex) {
        Preconditions.checkIndex(ordinalIndex, this.getSize(), "ordinalIndex");
        return this.getByIndex0(ordinalIndex);
    }

    protected abstract @Nullable T getByIndex0(int ordinalIndex);

    public final void setByIndex(final int ordinalIndex, @Nullable final T element) {
        Preconditions.checkIndex(ordinalIndex, this.getSize(), "ordinalIndex");
        this.setByIndex0(ordinalIndex, element);
    }

    protected abstract void setByIndex0(int ordinalIndex, @Nullable T element);

    private int toOrdinalIndex(final int row, final int column) {
        Preconditions.checkIndex(row, this.getRows(), "row");
        Preconditions.checkIndex(column, this.getColumns(), "column");

        final int ordinalIndex = row * this.getColumns() + column;
        Preconditions.checkIndex(ordinalIndex, this.getSize(), "ordinalIndex");
        return ordinalIndex;
    }

    public int getRows() { return this.rows; }

    public int getColumns() { return this.columns; }

    public int getSize() { return this.size; }

}
