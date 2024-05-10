/*
 * Copyright (C) Jan Schultke SPDX-License-Identifier: MIT
 */
package me.filoghost.chestcommands.util.nbt;

/**
 * The {@code TAG_Byte} tag.
 */
public final class NBTByte extends NBTTag implements Cloneable {

    private byte value;

    public NBTByte(final byte value) { this.value = value; }

    @Override
    public Byte getValue() { return this.value; }

    public byte getByteValue() { return this.value; }

    public void setByteValue(final byte value) { this.value = value; }

    @Override
    public NBTType getType() { return NBTType.BYTE; }

    // MISC

    @Override
    public boolean equals(final Object obj) { return obj instanceof NBTByte && this.equals((NBTByte) obj); }

    public boolean equals(final NBTByte tag) { return this.value == tag.value; }

    @Override
    public int hashCode() { return Byte.hashCode(this.value); }

    @Override
    public String toMSONString() { return Byte.toUnsignedInt(this.value) + "b"; }

    @Override
    public NBTByte clone() { return new NBTByte(this.value); }

}
