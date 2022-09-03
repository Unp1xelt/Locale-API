//  Copyright (C) 2022 Unp1xelt. All rights reserved.
//
//  This Source Code Form is subject to the terms of the Mozilla Public
//  License, v. 2.0. If a copy of the MPL was not distributed with this
//  file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
//  This Source Code Form is "Incompatible With Secondary Licenses", as
//  defined by the Mozilla Public License, v. 2.0.

package de.unpixelt.locale.events;

import de.unpixelt.locale.Locale;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a player changes their locale in the client settings.
 * <br>
 * Only use this event if you want explicitly to translate something.
 */
public class LocalePlayerLocaleChangeEvent extends PlayerEvent {
    private static final HandlerList HANDLERS = new HandlerList();

    private final Locale locale;

    private final Locale oldLocale;

    public LocalePlayerLocaleChangeEvent(@NotNull final Player who, @NotNull final Locale locale,
                                         @NotNull final Locale oldLocale) {
        super(who);
        this.locale = locale;
        this.oldLocale = oldLocale;
    }

    /**
     * Gets the local of the player.
     *
     * @return The player's new locale.
     */
    @NotNull
    public Locale getLocale() {
        return locale;
    }

    /**
     * Gets the old local the player used before.
     *
     * @return The old player's locale. Null when had no locale before.
     */
    @NotNull
    public Locale getOldLocale() {
        return oldLocale;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
