/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.parsing;

import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;

import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.chestcommands.util.LookupRegistry;
import me.filoghost.fcommons.Strings;

public class EnchantmentParser {

    private static final LookupRegistry<Enchantment> ENCHANTMENTS_REGISTRY = LookupRegistry.fromValues(Registry.ENCHANTMENT.iterator());

    static {

        // Add aliases
        EnchantmentParser.ENCHANTMENTS_REGISTRY.put("Protection", Enchantment.PROTECTION);
        EnchantmentParser.ENCHANTMENTS_REGISTRY.put("Fire Protection", Enchantment.FIRE_PROTECTION);
        EnchantmentParser.ENCHANTMENTS_REGISTRY.put("Feather Falling", Enchantment.FEATHER_FALLING);
        EnchantmentParser.ENCHANTMENTS_REGISTRY.put("Blast Protection", Enchantment.BLAST_PROTECTION);
        EnchantmentParser.ENCHANTMENTS_REGISTRY.put("Projectile Protection", Enchantment.PROJECTILE_PROTECTION);
        EnchantmentParser.ENCHANTMENTS_REGISTRY.put("Respiration", Enchantment.RESPIRATION);
        EnchantmentParser.ENCHANTMENTS_REGISTRY.put("Aqua Affinity", Enchantment.AQUA_AFFINITY);
        EnchantmentParser.ENCHANTMENTS_REGISTRY.put("Thorns", Enchantment.THORNS);
        EnchantmentParser.ENCHANTMENTS_REGISTRY.put("Sharpness", Enchantment.SHARPNESS);
        EnchantmentParser.ENCHANTMENTS_REGISTRY.put("Smite", Enchantment.SMITE);
        EnchantmentParser.ENCHANTMENTS_REGISTRY.put("Bane Of Arthropods", Enchantment.BANE_OF_ARTHROPODS);
        EnchantmentParser.ENCHANTMENTS_REGISTRY.put("Knockback", Enchantment.KNOCKBACK);
        EnchantmentParser.ENCHANTMENTS_REGISTRY.put("Fire Aspect", Enchantment.FIRE_ASPECT);
        EnchantmentParser.ENCHANTMENTS_REGISTRY.put("Looting", Enchantment.LOOTING);
        EnchantmentParser.ENCHANTMENTS_REGISTRY.put("Efficiency", Enchantment.EFFICIENCY);
        EnchantmentParser.ENCHANTMENTS_REGISTRY.put("Silk Touch", Enchantment.SILK_TOUCH);
        EnchantmentParser.ENCHANTMENTS_REGISTRY.put("Unbreaking", Enchantment.UNBREAKING);
        EnchantmentParser.ENCHANTMENTS_REGISTRY.put("Fortune", Enchantment.FORTUNE);
        EnchantmentParser.ENCHANTMENTS_REGISTRY.put("Power", Enchantment.POWER);
        EnchantmentParser.ENCHANTMENTS_REGISTRY.put("Punch", Enchantment.PUNCH);
        EnchantmentParser.ENCHANTMENTS_REGISTRY.put("Flame", Enchantment.FLAME);
        EnchantmentParser.ENCHANTMENTS_REGISTRY.put("Infinity", Enchantment.INFINITY);
        EnchantmentParser.ENCHANTMENTS_REGISTRY.put("Lure", Enchantment.LURE);
        EnchantmentParser.ENCHANTMENTS_REGISTRY.put("Luck Of The Sea", Enchantment.LUCK_OF_THE_SEA);
    }

    public static EnchantmentDetails parseEnchantment(String input) throws ParseException {
        int level = 1;

        if (input.contains(",")) {
            final String[] levelSplit = Strings.splitAndTrim(input, ",", 2);

            try {
                level = NumberParser.getStrictlyPositiveInteger(levelSplit[1]);
            } catch (final ParseException e) {
                throw new ParseException(Errors.Parsing.invalidEnchantmentLevel(levelSplit[1]), e);
            }
            input = levelSplit[0];
        }

        final Enchantment enchantment = EnchantmentParser.ENCHANTMENTS_REGISTRY.lookup(input);

        if (enchantment != null) {
            return new EnchantmentDetails(enchantment, level);
        } else {
            throw new ParseException(Errors.Parsing.unknownEnchantmentType(input));
        }
    }

    public static class EnchantmentDetails {

        private final Enchantment enchantment;
        private final int level;

        private EnchantmentDetails(final Enchantment enchantment, final int level) {
            this.enchantment = enchantment;
            this.level = level;
        }

        public Enchantment getEnchantment() { return this.enchantment; }

        public int getLevel() { return this.level; }

    }

}
