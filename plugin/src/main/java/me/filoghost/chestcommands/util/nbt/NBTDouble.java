/*
 * Copyright (C) Jan Schultke SPDX-License-Identifier: MIT
 */
package me.filoghost.chestcommands.util.nbt;

/**
 * The {@code TAG_Double} tag.
 */
public final class NBTDouble extends NBTTag implements Cloneable {

    private double value;

    public NBTDouble(final double value) { this.value = value; }

    @Override
    public Double getValue() { return this.value; }

    public double getDoubleValue() { return this.value; }

    public void setDoubleValue(final double value) { this.value = value; }

    @Override
    public NBTType getType() { return NBTType.DOUBLE; }

    // MISC

    @Override
    public boolean equals(final Object obj) { return obj instanceof NBTDouble && this.equals((NBTDouble) obj); }

    public boolean equals(final NBTDouble tag) { return this.value == tag.value; }

    @Override
    public int hashCode() { return Double.hashCode(this.value); }

    @Override
    public String toMSONString() { return this.value + "d"; }

    @Override
    public NBTDouble clone() { return new NBTDouble(this.value); }

}