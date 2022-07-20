//  Copyright (C) 2022 Unp1xelt. All rights reserved.
//
//  This Source Code Form is subject to the terms of the Mozilla Public
//  License, v. 2.0. If a copy of the MPL was not distributed with this
//  file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
//  This Source Code Form is "Incompatible With Secondary Licenses", as
//  defined by the Mozilla Public License, v. 2.0.

package de.unpixelt.locale;

/**
 * Debugger sends messages to the console when a {@code Locale} is loaded into
 * or cleared from the cache, by default this is disabled.
 * @see Locale
 */
class Debugger {

    private boolean showCaching = false;

    private void sendMessage(String msg) {
        Translate.getPlugin().getLogger().info(msg);
    }

    void loadToCache(Locale locale) {
        if (showCaching) sendMessage("'" + locale.name() + "' loaded into cache");
    }

    void unloadFromCache(Locale locale) {
        if (showCaching) sendMessage("'" + locale.name() + "' cleared from cache");
    }

    public boolean isShowCaching() {
        return showCaching;
    }

    public void setShowCaching(boolean showCaching) {
        this.showCaching = showCaching;
        sendMessage("Cache changes are " + (showCaching ? "shown" : "hidden"));
    }
}
