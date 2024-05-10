/*
 * Copyright (C) Jan Schultke SPDX-License-Identifier: MIT
 */
package me.filoghost.chestcommands.util.nbt;

/**
 * The {@code TAG_Float} tag.
 */
public final class NBTFloat extends NBTTag implements Cloneable {

    private float value;

    public NBTFloat(final float value) { this.value = value; }

    @Override
    public Float getValue() { return this.value; }

    public float getFloatValue() { return this.value; }

    public void setFloatValue(final float value) { this.value = value; }

    @Override
    public NBTType getType() { return NBTType.FLOAT; }

    // MISC

    @Override
    public boolean equals(final Object obj) { return obj instanceof NBTFloat && this.equals((NBTFloat) obj); }

    public boolean equals(final NBTFloat tag) { return this.value == tag.value; }

    @Override
    public int hashCode() { return Float.hashCode(this.value); }

    @Override
    public String toMSONString() { return this.value + "f"; }

    @Override
    public NBTFloat clone() { return new NBTFloat(this.value); }

}
