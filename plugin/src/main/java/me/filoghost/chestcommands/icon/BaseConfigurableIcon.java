/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.icon;

import me.filoghost.chestcommands.api.Icon;
import me.filoghost.chestcommands.placeholder.PlaceholderString;
import me.filoghost.chestcommands.placeholder.PlaceholderStringList;
import me.filoghost.chestcommands.util.nbt.parser.MojangsonParseException;
import me.filoghost.chestcommands.util.nbt.parser.MojangsonParser;
import me.filoghost.fcommons.Preconditions;
import me.filoghost.fcommons.collection.CollectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseConfigurableIcon implements Icon {

    private Material material;
    private int amount;
    private short durability;

    private String nbtData;
    private PlaceholderString name;
    private PlaceholderStringList lore;
    private Map<Enchantment, Integer> enchantments;
    private Color leatherColor;
    private PlaceholderString skullOwner;
    private DyeColor bannerColor;
    private List<Pattern> bannerPatterns;
    private boolean placeholdersEnabled;

    private ItemStack cachedRendering; // Cache the rendered item when possible and if state hasn't changed

    public BaseConfigurableIcon(final Material material) {
        this.material = material;
        this.amount = 1;
    }

    protected boolean shouldCacheRendering() {
        if (this.placeholdersEnabled && this.hasDynamicPlaceholders()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean hasDynamicPlaceholders() {
        return (this.name != null && this.name.hasDynamicPlaceholders()) || (this.lore != null && this.lore.hasDynamicPlaceholders())
                || (this.skullOwner != null && this.skullOwner.hasDynamicPlaceholders());
    }

    public void setMaterial(@NotNull final Material material) {
        this.material = material;
        this.cachedRendering = null;
    }

    public @NotNull Material getMaterial() { return this.material; }

    public void setAmount(final int amount) {
        Preconditions.checkArgument(amount > 0, "amount must be greater than 0");
        this.amount = Math.min(amount, 127);
        this.cachedRendering = null;
    }

    public int getAmount() { return this.amount; }

    public void setDurability(final short durability) {
        Preconditions.checkArgument(durability >= 0, "durability must be 0 or greater");
        this.durability = durability;
        this.cachedRendering = null;
    }

    public short getDurability() { return this.durability; }

    public void setNBTData(@Nullable final String nbtData) {
        if (nbtData != null) {
            try {
                MojangsonParser.parse(nbtData);
            } catch (final MojangsonParseException e) {
                throw new IllegalArgumentException("invalid nbtData", e);
            }
        }
        this.nbtData = nbtData;
        this.cachedRendering = null;
    }

    public @Nullable String getNBTData() { return this.nbtData; }

    public void setName(@Nullable final String name) {
        this.name = PlaceholderString.of(name);
        this.cachedRendering = null;
    }

    public @Nullable String getName() {
        if (this.name != null) {
            return this.name.getOriginalValue();
        } else {
            return null;
        }
    }

    public void setLore(@Nullable final String... lore) { this.setLore(lore != null ? Arrays.asList(lore) : null); }

    public void setLore(@Nullable final List<String> lore) {
        if (lore != null) {
            this.lore = new PlaceholderStringList(CollectionUtils.toArrayList(lore, element -> (element != null ? element : "")));
        } else {
            this.lore = null;
        }
        this.cachedRendering = null;
    }

    public @Nullable List<String> getLore() {
        if (this.lore != null) {
            return new ArrayList<>(this.lore.getOriginalValue());
        } else {
            return null;
        }
    }

    public void setEnchantments(@Nullable final Map<Enchantment, Integer> enchantments) {
        this.enchantments = CollectionUtils.newHashMap(enchantments);
        this.cachedRendering = null;
    }

    public @Nullable Map<Enchantment, Integer> getEnchantments() { return CollectionUtils.newHashMap(this.enchantments); }

    public void addEnchantment(@NotNull final Enchantment enchantment) { this.addEnchantment(enchantment, 1); }

    public void addEnchantment(@NotNull final Enchantment enchantment, final int level) {
        if (this.enchantments == null) {
            this.enchantments = new HashMap<>();
        }
        this.enchantments.put(enchantment, level);
        this.cachedRendering = null;
    }

    public void removeEnchantment(@NotNull final Enchantment enchantment) {
        if (this.enchantments == null) {
            return;
        }
        this.enchantments.remove(enchantment);
        this.cachedRendering = null;
    }

    public @Nullable Color getLeatherColor() { return this.leatherColor; }

    public void setLeatherColor(@Nullable final Color leatherColor) {
        this.leatherColor = leatherColor;
        this.cachedRendering = null;
    }

    public @Nullable String getSkullOwner() {
        if (this.skullOwner != null) {
            return this.skullOwner.getOriginalValue();
        } else {
            return null;
        }
    }

    public void setSkullOwner(@Nullable final String skullOwner) {
        this.skullOwner = PlaceholderString.of(skullOwner);
        this.cachedRendering = null;
    }

    public @Nullable DyeColor getBannerColor() { return this.bannerColor; }

    public void setBannerColor(@Nullable final DyeColor bannerColor) {
        this.bannerColor = bannerColor;
        this.cachedRendering = null;
    }

    public @Nullable List<Pattern> getBannerPatterns() { return CollectionUtils.newArrayList(this.bannerPatterns); }

    public void setBannerPatterns(@Nullable final Pattern... bannerPatterns) {
        this.setBannerPatterns(bannerPatterns != null ? Arrays.asList(bannerPatterns) : null);
    }

    public void setBannerPatterns(@Nullable final List<Pattern> bannerPatterns) {
        this.bannerPatterns = CollectionUtils.newArrayList(bannerPatterns);
        this.cachedRendering = null;
    }

    public boolean isPlaceholdersEnabled() { return this.placeholdersEnabled; }

    public void setPlaceholdersEnabled(final boolean placeholdersEnabled) {
        this.placeholdersEnabled = placeholdersEnabled;
        this.cachedRendering = null;
    }

    public @Nullable String renderName(final Player viewer) {
        if (this.name == null) {
            return null;
        }
        if (!this.placeholdersEnabled) {
            return this.name.getOriginalValue();
        }

        final String name = this.name.getValue(viewer);

        if (name.isEmpty()) {
            // Add a color to display the name empty
            return ChatColor.WHITE.toString();
        } else {
            return name;
        }
    }

    public @Nullable List<String> renderLore(final Player viewer) {
        if (this.lore == null) {
            return null;
        }
        if (!this.placeholdersEnabled) {
            return this.lore.getOriginalValue();
        }

        return this.lore.getValue(viewer);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ItemStack render(@NotNull final Player viewer) {
        if (this.shouldCacheRendering() && this.cachedRendering != null) {
            // Performance: return a cached item
            return this.cachedRendering;
        }

        final ItemStack itemStack = new ItemStack(this.material, this.amount, this.durability);

        // First try to apply NBT data
        if (this.nbtData != null) {
            Bukkit.getUnsafe().modifyItemStack(itemStack, this.nbtData);
        }

        // Then apply data from config nodes, overwriting NBT data if there are
        // conflicting values
        final ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            itemMeta.setDisplayName(this.renderName(viewer));
            itemMeta.setLore(this.renderLore(viewer));

            if (this.leatherColor != null && itemMeta instanceof LeatherArmorMeta) {
                ((LeatherArmorMeta) itemMeta).setColor(this.leatherColor);
            }

            if (this.skullOwner != null && itemMeta instanceof SkullMeta) {
                final String skullOwner = this.skullOwner.getValue(viewer);
                ((SkullMeta) itemMeta).setOwner(skullOwner);
            }

            if (itemMeta instanceof BannerMeta) {
                if (this.bannerPatterns != null) {
                    ((BannerMeta) itemMeta).setPatterns(this.bannerPatterns);
                }
            }

            // Hide all text details (damage, enchantments, potions, etc,)
            if (itemMeta.getItemFlags().isEmpty()) {
                itemMeta.addItemFlags(ItemFlag.values());
            }

            itemStack.setItemMeta(itemMeta);
        }

        if (this.enchantments != null) {
            this.enchantments.forEach(itemStack::addUnsafeEnchantment);
        }

        if (this.shouldCacheRendering()) {
            this.cachedRendering = itemStack;
        }

        return itemStack;
    }

}
