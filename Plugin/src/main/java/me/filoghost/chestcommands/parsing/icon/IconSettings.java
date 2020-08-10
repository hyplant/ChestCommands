/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.chestcommands.parsing.icon;

import me.filoghost.chestcommands.attribute.AttributeErrorHandler;
import me.filoghost.chestcommands.attribute.IconAttribute;
import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.logging.ErrorMessages;
import me.filoghost.chestcommands.parsing.ParseException;
import me.filoghost.commons.config.ConfigSection;
import me.filoghost.commons.config.ConfigValue;
import me.filoghost.commons.config.exception.ConfigValueException;
import me.filoghost.commons.logging.ErrorCollector;
import org.bukkit.Material;

import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

public class IconSettings {

	private final Path menuFile;
	private final String iconName;
	private final Map<AttributeType, IconAttribute> attributes;

	public IconSettings(Path menuFile, String iconName) {
		this.menuFile = menuFile;
		this.iconName = iconName;
		this.attributes = new EnumMap<>(AttributeType.class);
	}

	public InternalConfigurableIcon createIcon() {
		InternalConfigurableIcon icon = new InternalConfigurableIcon(Material.BEDROCK);

		for (IconAttribute attribute : attributes.values()) {
			attribute.apply(icon);
		}

		return icon;
	}

	public IconAttribute getAttributeValue(AttributeType attributeType) {
		return attributes.get(attributeType);
	}

	public void loadFrom(ConfigSection config, ErrorCollector errorCollector) {
		for (String attributeName : config.getKeys()) {
			try {
				AttributeType attributeType = AttributeType.fromAttributeName(attributeName);
				if (attributeType == null) {
					throw new ParseException(ErrorMessages.Parsing.unknownAttribute);
				}

				AttributeErrorHandler errorHandler = (String listElement, ParseException e) -> {
					errorCollector.add(e, ErrorMessages.Menu.invalidAttributeListElement(this, attributeName, listElement));
				};

				ConfigValue configValue = config.get(attributeName);
				IconAttribute iconAttribute = attributeType.getParser().parse(configValue, errorHandler);
				attributes.put(attributeType, iconAttribute);

			} catch (ParseException | ConfigValueException e) {
				errorCollector.add(e, ErrorMessages.Menu.invalidAttribute(this, attributeName));
			}
		}
	}

	public Path getMenuFile() {
		return menuFile;
	}

	public String getIconName() {
		return iconName;
	}

}
