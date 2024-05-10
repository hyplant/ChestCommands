/*
 * Copyright (C) Jan Schultke SPDX-License-Identifier: MIT
 */
package me.filoghost.chestcommands.util.nbt;

/**
 * The {@code TAG_Int} tag.
 */
public final class NBTInt extends NBTTag implements Cloneable {

    private int value;

    public NBTInt(final int value) { this.value = value; }

    @Override
    public Integer getValue() { return this.value; }

    public int getIntValue() { return this.value; }

    public void setIntValue(final int value) { this.value = value; }

    @Override
    public NBTType getType() { return NBTType.INT; }

    // MISC

    @Override
    public boolean equals(final Object obj) { return obj instanceof NBTInt && this.equals((NBTInt) obj); }

    public boolean equals(final NBTInt tag) { return this.value == tag.value; }

    @Override
    public int hashCode() { return Integer.hashCode(this.value); }

    @Override
    public String toMSONString() { return Integer.toString(this.value); }

    @Override
    public NBTInt clone() { return new NBTInt(this.value); }

}
