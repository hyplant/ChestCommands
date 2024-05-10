/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.parsing.icon;

import java.nio.file.Path;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Material;

import me.filoghost.chestcommands.attribute.AttributeErrorHandler;
import me.filoghost.chestcommands.attribute.IconAttribute;
import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.chestcommands.parsing.ParseException;
import me.filoghost.fcommons.config.ConfigPath;
import me.filoghost.fcommons.config.ConfigSection;
import me.filoghost.fcommons.config.ConfigValue;
import me.filoghost.fcommons.config.exception.ConfigValueException;
import me.filoghost.fcommons.logging.ErrorCollector;

public class IconSettings {

    private final Path menuFile;
    private final ConfigPath configPath;
    private final Map<AttributeType, IconAttribute> validAttributes;
    private final Set<AttributeType> invalidAttributes;

    public IconSettings(final Path menuFile, final ConfigPath configPath) {
        this.menuFile = menuFile;
        this.configPath = configPath;
        this.validAttributes = new EnumMap<>(AttributeType.class);
        this.invalidAttributes = new HashSet<>();
    }

    public InternalConfigurableIcon createIcon() {
        final InternalConfigurableIcon icon = new InternalConfigurableIcon(Material.BEDROCK);

        for (final IconAttribute attribute : this.validAttributes.values()) {
            attribute.apply(icon);
        }

        return icon;
    }

    public IconAttribute getAttributeValue(final AttributeType attributeType) { return this.validAttributes.get(attributeType); }

    public boolean isMissingAttribute(final AttributeType attributeType) {
        return !this.validAttributes.containsKey(attributeType) && !this.invalidAttributes.contains(attributeType);
    }

    public void loadFrom(final ConfigSection config, final ErrorCollector errorCollector) {
        for (final Entry<ConfigPath, ConfigValue> entry : config.toMap().entrySet()) {
            final ConfigPath configKey = entry.getKey();
            AttributeType attributeType = null;
            try {
                attributeType = AttributeType.fromConfigKey(configKey);
                if (attributeType == null) {
                    throw new ParseException(Errors.Parsing.unknownAttribute);
                }

                final AttributeErrorHandler errorHandler = (final String listElement, final ParseException e) -> {
                    errorCollector.add(e, Errors.Menu.invalidAttributeListElement(this, configKey, listElement));
                };

                final ConfigValue configValue = config.get(configKey);
                final IconAttribute iconAttribute = attributeType.getParser().parse(configValue, errorHandler);
                this.validAttributes.put(attributeType, iconAttribute);

            } catch (ParseException | ConfigValueException e) {
                errorCollector.add(e, Errors.Menu.invalidAttribute(this, configKey));
                if (attributeType != null) {
                    this.invalidAttributes.add(attributeType);
                }
            }
        }
    }

    public Path getMenuFile() { return this.menuFile; }

    public ConfigPath getConfigPath() { return this.configPath; }

}
