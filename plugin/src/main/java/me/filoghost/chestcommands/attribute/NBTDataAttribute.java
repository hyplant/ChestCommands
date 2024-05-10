/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.parsing.ParseException;
import me.filoghost.chestcommands.util.nbt.parser.MojangsonParseException;
import me.filoghost.chestcommands.util.nbt.parser.MojangsonParser;

public class NBTDataAttribute implements IconAttribute {

    private final String nbtData;

    public NBTDataAttribute(final String nbtData, final AttributeErrorHandler errorHandler) throws ParseException {
        try {
            // Check that NBT syntax is valid before applying it to the icon
            MojangsonParser.parse(nbtData);
        } catch (final MojangsonParseException e) {
            throw new ParseException(e.getMessage());
        }

        this.nbtData = nbtData;
    }

    @Override
    public void apply(final InternalConfigurableIcon icon) { icon.setNBTData(this.nbtData); }

}
