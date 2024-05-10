/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.icon.ClickResult;
import me.filoghost.chestcommands.icon.InternalConfigurableIcon;

public class KeepOpenAttribute implements IconAttribute {

    private final ClickResult clickResult;

    public KeepOpenAttribute(final boolean keepOpen, final AttributeErrorHandler errorHandler) {
        if (keepOpen) {
            this.clickResult = ClickResult.KEEP_OPEN;
        } else {
            this.clickResult = ClickResult.CLOSE;
        }
    }

    @Override
    public void apply(final InternalConfigurableIcon icon) { icon.setClickResult(this.clickResult); }

}
