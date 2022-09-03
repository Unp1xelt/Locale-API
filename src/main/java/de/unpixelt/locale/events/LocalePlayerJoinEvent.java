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
import de.unpixelt.locale.Translate;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player joins a server.
 * <br>
 * Only use this event if you want explicitly to translate something.
 */
public class LocalePlayerJoinEvent extends PlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Locale locale;

    public LocalePlayerJoinEvent(@NotNull final Player playerJoined) {
        super(playerJoined);
        this.locale = Translate.getLocale(playerJoined);
    }

    /**
     * Gets the local of the player.
     *
     * @return The player's locale.
     */
    @NotNull
    public Locale getLocale() {
        return locale;
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
