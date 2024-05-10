/*
 * Copyright (C) Jan Schultke SPDX-License-Identifier: MIT
 */
package me.filoghost.chestcommands.util.nbt;

/**
 * An abstract NBT-Tag.
 */
public abstract class NBTTag {

    /**
     * Gets the value of this tag.
     *
     * @return the value of this tag
     */
    public abstract Object getValue();

    /**
     * Returns the type of this tag.
     *
     * @return the type of this tag
     */
    public abstract NBTType getType();

    /**
     * Convenience method for getting the id of this tag's type.
     *
     * @return the type id
     */
    public byte getTypeId() { return this.getType().getId(); }

    /**
     * Returns a Mojangson string depicting this NBT tag.
     *
     * @return a Mojangson string depicting this NBT tag
     */
    public abstract String toMSONString();

    // MISC

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof final NBTTag tag) {
            return this.getType() == tag.getType() && this.getValue().equals(tag.getValue());
        }
        return false;
    }

    @Override
    public int hashCode() { return this.getValue().hashCode(); }

    @Override
    public String toString() { return this.toMSONString(); }

}
