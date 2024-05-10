/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.parsing.EnchantmentParser;
import me.filoghost.chestcommands.parsing.ParseException;

public class EnchantmentsAttribute implements IconAttribute {

    private final Map<Enchantment, Integer> enchantments;

    public EnchantmentsAttribute(final List<String> serializedEnchantments, final AttributeErrorHandler errorHandler) {
        this.enchantments = new HashMap<>();

        for (final String serializedEnchantment : serializedEnchantments) {
            if (serializedEnchantment == null || serializedEnchantment.isEmpty()) {
                continue; // Skip
            }

            try {
                final EnchantmentParser.EnchantmentDetails enchantment = EnchantmentParser.parseEnchantment(serializedEnchantment);
                this.enchantments.put(enchantment.getEnchantment(), enchantment.getLevel());
            } catch (final ParseException e) {
                errorHandler.onListElementError(serializedEnchantment, e);
            }
        }
    }

    @Override
    public void apply(final InternalConfigurableIcon icon) { icon.setEnchantments(this.enchantments); }

}
