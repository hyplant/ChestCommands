/*
 * Copyright (C) Jan Schultke SPDX-License-Identifier: MIT
 */
package me.filoghost.chestcommands.util.nbt;

/**
 * The {@code TAG_Long} tag.
 */
public final class NBTLong extends NBTTag implements Cloneable {

    private long value;

    public NBTLong(final long value) { this.value = value; }

    @Override
    public Long getValue() { return this.value; }

    public long getLongValue() { return this.value; }

    public void setLongValue(final long value) { this.value = value; }

    @Override
    public NBTType getType() { return NBTType.LONG; }

    // MISC

    @Override
    public boolean equals(final Object obj) { return obj instanceof NBTLong && this.equals((NBTLong) obj); }

    public boolean equals(final NBTLong tag) { return this.value == tag.value; }

    @Override
    public int hashCode() { return Long.hashCode(this.value); }

    @Override
    public String toMSONString() { return this.value + "L"; }

    @Override
    public NBTLong clone() { return new NBTLong(this.value); }

}
