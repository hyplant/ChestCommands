/*
 * Copyright (C) Jan Schultke SPDX-License-Identifier: MIT
 */
package me.filoghost.chestcommands.util.nbt;

/**
 * The {@code TAG_String} tag.
 */
public final class NBTString extends NBTTag implements Cloneable {

    private String value;

    public NBTString(final String value) { this.setValue(value); }

    @Override
    public String getValue() { return this.value; }

    public void setValue(final String value) { this.value = value; }

    @Override
    public NBTType getType() { return NBTType.STRING; }

    // MISC

    @Override
    public int hashCode() { return this.value.hashCode(); }

    @Override
    public String toMSONString() { return NBTString.toMSONString(this.value); }

    @Override
    public NBTString clone() { return new NBTString(this.value); }

    // UTIL

    /**
     * Converts a regular string into a Mojangson string by surrounding it with
     * quotes and escaping backslashes and quotes inside it.
     *
     * @param str the string
     * @return the Mojangson string
     */
    public static String toMSONString(final String str) {
        final StringBuilder builder = new StringBuilder("\"");
        final char[] chars = str.toCharArray();
        for (final char c : chars) {
            if ((c == '\\') || (c == '"')) {
                builder.append('\\');
            }
            builder.append(c);
        }
        return builder.append('\"').toString();
    }

}
