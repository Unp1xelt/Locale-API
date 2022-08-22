//  Copyright (C) 2022 Unp1xelt. All rights reserved.
//
//  This Source Code Form is subject to the terms of the Mozilla Public
//  License, v. 2.0. If a copy of the MPL was not distributed with this
//  file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
//  This Source Code Form is "Incompatible With Secondary Licenses", as
//  defined by the Mozilla Public License, v. 2.0.

package de.unpixelt.locale;

import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;

/**
 * Command for showing how many {@code Locale}'s are used.
 */
class UsedLocalesCommand implements CommandExecutor {

    final LocaleCounter counter;

    UsedLocalesCommand(final LocaleCounter counter) {
        this.counter = counter;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(counter.usedLocales());
            return true;
        }
        sender.sendMessage("Unknown command. Type \"/help\" for help.");
        return false;
    }
}
