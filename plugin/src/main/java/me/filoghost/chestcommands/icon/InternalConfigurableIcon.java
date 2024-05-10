/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.icon;

import com.google.common.collect.ImmutableList;
import me.filoghost.chestcommands.action.Action;
import me.filoghost.chestcommands.action.OpenMenuAction;
import me.filoghost.chestcommands.api.MenuView;
import me.filoghost.chestcommands.config.Lang;
import me.filoghost.chestcommands.icon.requirement.RequiredExpLevel;
import me.filoghost.chestcommands.icon.requirement.RequiredMoney;
import me.filoghost.chestcommands.icon.requirement.Requirement;
import me.filoghost.chestcommands.icon.requirement.item.RequiredItem;
import me.filoghost.chestcommands.icon.requirement.item.RequiredItems;
import me.filoghost.fcommons.Preconditions;
import me.filoghost.fcommons.collection.CollectionUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InternalConfigurableIcon extends BaseConfigurableIcon implements RefreshableIcon {

    private IconPermission viewPermission;
    private IconPermission clickPermission;
    private String noClickPermissionMessage;

    private RequiredMoney requiredMoney;
    private RequiredExpLevel requiredExpLevel;
    private RequiredItems requiredItems;

    private ImmutableList<Action> clickActions;
    private ClickResult clickResult;

    public InternalConfigurableIcon(final Material material) {
        super(material);
        this.setPlaceholdersEnabled(true);
        this.clickResult = ClickResult.CLOSE;
    }

    public boolean canViewIcon(final Player player) { return IconPermission.hasPermission(player, this.viewPermission); }

    public boolean hasViewPermission() { return this.viewPermission != null && !this.viewPermission.isEmpty(); }

    public void setClickPermission(final String permission) { this.clickPermission = new IconPermission(permission); }

    public void setNoClickPermissionMessage(final String noClickPermissionMessage) {
        this.noClickPermissionMessage = noClickPermissionMessage;
    }

    public void setViewPermission(final String viewPermission) { this.viewPermission = new IconPermission(viewPermission); }

    public void setRequiredMoney(final double requiredMoney) {
        if (requiredMoney > 0.0) {
            this.requiredMoney = new RequiredMoney(requiredMoney);
        } else {
            this.requiredMoney = null;
        }
    }

    public void setRequiredExpLevel(final int requiredLevels) {
        if (requiredLevels > 0) {
            this.requiredExpLevel = new RequiredExpLevel(requiredLevels);
        } else {
            this.requiredExpLevel = null;
        }
    }

    public void setRequiredItems(final List<RequiredItem> requiredItems) {
        if (requiredItems != null) {
            this.requiredItems = new RequiredItems(requiredItems);
        } else {
            this.requiredItems = null;
        }
    }

    public void setClickActions(final List<Action> clickActions) { this.clickActions = CollectionUtils.newImmutableList(clickActions); }

    @Override
    public ItemStack render(@NotNull final Player viewer) {
        if (this.canViewIcon(viewer)) {
            return super.render(viewer);
        } else {
            return null;
        }
    }

    @Override
    protected boolean shouldCacheRendering() { return super.shouldCacheRendering() && !this.hasViewPermission(); }

    public void setClickResult(final ClickResult clickResult) {
        Preconditions.notNull(clickResult, "clickResult");
        this.clickResult = clickResult;
    }

    @Override
    public void onClick(@NotNull final MenuView menuView, @NotNull final Player player) {
        final ClickResult clickResult = this.onClickGetResult(menuView, player);
        if (clickResult == ClickResult.CLOSE) {
            menuView.close();
        }
    }

    private ClickResult onClickGetResult(@NotNull final MenuView menuView, @NotNull final Player player) {
        if (!IconPermission.hasPermission(player, this.viewPermission)) {
            return ClickResult.KEEP_OPEN;
        }

        if (!IconPermission.hasPermission(player, this.clickPermission)) {
            if (this.noClickPermissionMessage != null) {
                player.sendMessage(this.noClickPermissionMessage);
            } else {
                player.sendMessage(Lang.get().default_no_icon_permission);
            }
            return this.clickResult;
        }

        // Check all the requirements
        final Requirement[] requirements = { this.requiredMoney, this.requiredExpLevel, this.requiredItems };
        final boolean hasAllRequirements = Requirement.hasAllCosts(player, requirements);
        if (!hasAllRequirements) {
            return this.clickResult;
        }

        // If all requirements are satisfied, take their cost
        final boolean takenAllCosts = Requirement.takeAllCosts(player, requirements);
        if (!takenAllCosts) {
            return this.clickResult;
        }

        boolean hasOpenMenuAction = false;

        if (this.clickActions != null) {
            for (final Action action : this.clickActions) {
                action.execute(player);

                if (action instanceof OpenMenuAction) {
                    hasOpenMenuAction = true;
                }
            }
        }

        // Update the menu after taking requirement costs and executing all actions
        menuView.refresh();

        // Force menu to stay open if actions open another menu
        if (hasOpenMenuAction) {
            return ClickResult.KEEP_OPEN;
        } else {
            return this.clickResult;
        }
    }

    @Override
    public @Nullable ItemStack updateRendering(final Player viewer, @Nullable final ItemStack currentRendering) {
        if (currentRendering != null && this.shouldCacheRendering()) {
            // Internal icons do not change, no need to update if the item is already
            // rendered
            return currentRendering;
        }

        if (!this.canViewIcon(viewer)) {
            // Hide the current item
            return null;
        }

        if (currentRendering == null) {
            // Render item normally
            return this.render(viewer);
        } else {
            // Internal icons are loaded and then never change, we can safely update only
            // name and lore (for performance)
            final ItemMeta meta = currentRendering.getItemMeta();
            meta.setDisplayName(this.renderName(viewer));
            meta.setLore(this.renderLore(viewer));
            currentRendering.setItemMeta(meta);
            return currentRendering;
        }
    }

}
