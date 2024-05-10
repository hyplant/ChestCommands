/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.parsing;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.fcommons.MaterialsHelper;
import me.filoghost.fcommons.Preconditions;
import me.filoghost.fcommons.Strings;

public class ItemStackParser {

    private final Material material;
    private int amount = 1;
    private short durability = 0;
    private boolean hasExplicitDurability = false;

    /*
     * Reads item in the format "material:durability, amount".
     */
    public ItemStackParser(String input, final boolean parseAmount) throws ParseException {
        Preconditions.notNull(input, "input");

        if (parseAmount) {
            // Read the optional amount
            final String[] splitAmount = Strings.splitAndTrim(input, ",", 2);

            if (splitAmount.length > 1) {
                try {
                    this.amount = NumberParser.getStrictlyPositiveInteger(splitAmount[1]);
                } catch (final ParseException e) {
                    throw new ParseException(Errors.Parsing.invalidAmount(splitAmount[1]), e);
                }

                // Only keep the first part as input
                input = splitAmount[0];
            }
        }

        // Read the optional durability
        final String[] splitByColons = Strings.splitAndTrim(input, ":", 2);

        if (splitByColons.length > 1) {
            try {
                this.durability = NumberParser.getPositiveShort(splitByColons[1]);
            } catch (final ParseException e) {
                throw new ParseException(Errors.Parsing.invalidDurability(splitByColons[1]), e);
            }

            this.hasExplicitDurability = true;

            // Only keep the first part as input
            input = splitByColons[0];
        }

        this.material = MaterialParser.parseMaterial(input);
    }

    public void checkNotAir() throws ParseException {
        if (MaterialsHelper.isAir(this.material)) {
            throw new ParseException(Errors.Parsing.materialCannotBeAir);
        }
    }

    public Material getMaterial() { return this.material; }

    public int getAmount() { return this.amount; }

    public short getDurability() { return this.durability; }

    public boolean hasExplicitDurability() { return this.hasExplicitDurability; }

    public ItemStack createStack() {
        final ItemStack item = new ItemStack(this.material, this.amount);
        if (item.getItemMeta() instanceof Damageable)
            ((Damageable) item.getItemMeta()).setDamage(this.durability);
        return item;
    }

}
