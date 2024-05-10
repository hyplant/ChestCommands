/*
 * Copyright (C) Jan Schultke SPDX-License-Identifier: MIT
 */
package me.filoghost.chestcommands.util.nbt;

/**
 * The {@code TAG_Short} tag.
 */
public final class NBTShort extends NBTTag implements Cloneable {

    private short value;

    public NBTShort(final short value) { this.value = value; }

    @Override
    public Short getValue() { return this.value; }

    public short getShortValue() { return this.value; }

    public void setShortValue(final short value) { this.value = value; }

    @Override
    public NBTType getType() { return NBTType.SHORT; }

    // MISC

    @Override
    public boolean equals(final Object obj) { return obj instanceof NBTShort && this.equals((NBTShort) obj); }

    public boolean equals(final NBTShort tag) { return this.value == tag.value; }

    @Override
    public int hashCode() { return Short.hashCode(this.value); }

    @Override
    public String toMSONString() { return this.value + "s"; }

    @Override
    public NBTShort clone() { return new NBTShort(this.value); }

}
