//  Copyright (C) 2022 Unp1xelt. All rights reserved.
//
//  This Source Code Form is subject to the terms of the Mozilla Public
//  License, v. 2.0. If a copy of the MPL was not distributed with this
//  file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
//  This Source Code Form is "Incompatible With Secondary Licenses", as
//  defined by the Mozilla Public License, v. 2.0.

package de.unpixelt.locale;

import de.unpixelt.locale.events.LocalePlayerJoinEvent;
import de.unpixelt.locale.events.LocalePlayerLocaleChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

class LocaleListener implements Listener {

    private final HashMap<UUID, Locale> playerLocale;
    private final LocaleCounter counter;

    LocaleListener(LocaleCounter countMap) {
        this.counter = countMap;
        this.playerLocale = new HashMap<>();

        for(Player p : Bukkit.getOnlinePlayers()) {
            Locale locale = Translate.getLocale(p);

            playerLocale.put(p.getUniqueId(), locale);
            counter.increment(locale);
        }
    }

    @EventHandler
    private void onListCommands(@NotNull PlayerCommandSendEvent e) {
        e.getCommands().remove(Translate.COMMAND_NAME);
        e.getCommands().remove(Translate.getPlugin().getName().toLowerCase() + ":" + Translate.COMMAND_NAME);
    }

    @EventHandler
    private void onPlayerLocaleChange(@NotNull PlayerLocaleChangeEvent e) {
        Player p = e.getPlayer();
        Locale newLocale = Locale.valueOf(e.getLocale());
        Locale oldLocale = playerLocale.put(p.getUniqueId(), newLocale);

        counter.increment(newLocale);

        if (oldLocale == null) return;

        Bukkit.getPluginManager().callEvent(new LocalePlayerLocaleChangeEvent(p, newLocale, oldLocale));

        counter.decrement(oldLocale);
    }

    @EventHandler
    private void onPlayerJoinEvent(@NotNull PlayerJoinEvent e) {
        // This event is called after PlayerSpawnLocationEvent, although the
        // documentation says otherwise.
        // Locale is initialized around to seconds after this event.
        Bukkit.getScheduler().runTaskLater(Translate.getPlugin(), () -> {
            Player p = e.getPlayer();
            LocalePlayerJoinEvent event = new LocalePlayerJoinEvent(p);
            Locale locale = event.getLocale();

            Bukkit.getPluginManager().callEvent(event);

            // By default, the locale is set to 'en_us'. PlayerLocaleChangeEvent
            // is called, if the player's locale isn't 'en_us'.
            if (locale != Locale.en_us) return;

            playerLocale.put(p.getUniqueId(), locale);
            counter.increment(locale);
        }, 45L);
    }

    @EventHandler
    private void onPlayerQuitEvent(@NotNull PlayerQuitEvent e) {
        Locale oldLocale = playerLocale.remove(e.getPlayer().getUniqueId());

        counter.decrement(oldLocale);
    }
}
