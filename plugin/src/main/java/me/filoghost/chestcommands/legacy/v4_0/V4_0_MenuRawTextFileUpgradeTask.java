/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.legacy.v4_0;

import me.filoghost.chestcommands.legacy.upgrade.RegexUpgradeTask;
import me.filoghost.chestcommands.parsing.icon.AttributeType;

import java.nio.file.Path;

/*
 * All the changes that can be done by searching and replacing the raw text
 * inside the files
 */
public class V4_0_MenuRawTextFileUpgradeTask extends RegexUpgradeTask {

    public V4_0_MenuRawTextFileUpgradeTask(final Path menuFile) { super(menuFile); }

    @Override
    protected void computeRegexChanges() {
        this.renameInnerNode("command", "commands");
        this.renameInnerNode("open-action", "open-actions");
        this.renameInnerNode("id", "material");

        this.replaceOldAttribute("ID", AttributeType.MATERIAL);
        this.replaceOldAttribute("DATA-VALUE", AttributeType.DURABILITY);
        this.replaceOldAttribute("NBT", AttributeType.NBT_DATA);
        this.replaceOldAttribute("ENCHANTMENT", AttributeType.ENCHANTMENTS);
        this.replaceOldAttribute("COMMAND", AttributeType.ACTIONS);
        this.replaceOldAttribute("COMMANDS", AttributeType.ACTIONS);
        this.replaceOldAttribute("REQUIRED-ITEM", AttributeType.REQUIRED_ITEMS);
    }

    private void replaceOldAttribute(final String oldConfigKey, final AttributeType newAttribute) {
        this.renameInnerNode(oldConfigKey, newAttribute.getConfigKey().asRawKey());
    }

}
