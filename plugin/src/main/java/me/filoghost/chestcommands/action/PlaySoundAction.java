/*
 * Copyright (C) filoghost and contributors SPDX-License-Identifier:
 * GPL-3.0-or-later
 */
package me.filoghost.chestcommands.action;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.chestcommands.parsing.NumberParser;
import me.filoghost.chestcommands.parsing.ParseException;
import me.filoghost.fcommons.Strings;
import me.filoghost.fcommons.collection.EnumLookupRegistry;
import me.filoghost.fcommons.collection.LookupRegistry;

public class PlaySoundAction implements Action {

    private static final LookupRegistry<Sound> SOUNDS_REGISTRY = EnumLookupRegistry.fromEnumValues(Sound.class);

    private final Sound sound;
    private final float pitch;
    private final float volume;

    public PlaySoundAction(final String serializedAction) throws ParseException {
        final String[] split = Strings.splitAndTrim(serializedAction, ",", 3);

        this.sound = PlaySoundAction.SOUNDS_REGISTRY.lookup(split[0]);
        if (this.sound == null) {
            throw new ParseException(Errors.Parsing.unknownSound(split[0]));
        }

        if (split.length > 1) {
            try {
                this.pitch = NumberParser.getFloat(split[1]);
            } catch (final ParseException e) {
                throw new ParseException(Errors.Parsing.invalidSoundPitch(split[1]), e);
            }
        } else {
            this.pitch = 1.0f;
        }

        if (split.length > 2) {
            try {
                this.volume = NumberParser.getFloat(split[2]);
            } catch (final ParseException e) {
                throw new ParseException(Errors.Parsing.invalidSoundVolume(split[2]), e);
            }
        } else {
            this.volume = 1.0f;
        }
    }

    @Override
    public void execute(final Player player) { player.playSound(player.getLocation(), this.sound, this.volume, this.pitch); }

}
