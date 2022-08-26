//  Copyright (C) 2022 Unp1xelt. All rights reserved.
//
//  This Source Code Form is subject to the terms of the Mozilla Public
//  License, v. 2.0. If a copy of the MPL was not distributed with this
//  file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
//  This Source Code Form is "Incompatible With Secondary Licenses", as
//  defined by the Mozilla Public License, v. 2.0.

package de.unpixelt.locale;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.NotNull;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.HashMap;
import java.util.UUID;

class LocaleChangeListener implements Listener {

    private final HashMap<UUID, Locale> playerLocale = new HashMap<>();
    private final LocaleCounter counter;

    LocaleChangeListener(LocaleCounter countMap) {
        this.counter = countMap;
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
        Locale newLocale = Locale.valueOf(e.getLocale());
        Locale oldLocale = playerLocale.put(e.getPlayer().getUniqueId(), newLocale);

        counter.increment(newLocale);

        if (oldLocale == null) return;

        counter.decrement(oldLocale);
    }

    @EventHandler
    private void onPlayerSpawnEvent(@NotNull PlayerSpawnLocationEvent e) {
        Bukkit.getScheduler().runTaskLater(Translate.getPlugin(), () -> {
            Player p = e.getPlayer();
            Locale locale = Translate.getLocale(p);

            if (locale != Locale.en_us) return;

            playerLocale.put(p.getUniqueId(), locale);
            counter.increment(locale);
        }, 20);
    }

    @EventHandler
    private void onPlayerQuitEvent(@NotNull PlayerQuitEvent e) {
        Locale oldLocale = playerLocale.remove(e.getPlayer().getUniqueId());

        counter.decrement(oldLocale);
    }
}
