//  Copyright (C) 2022 Unp1xelt. All rights reserved.
//
//  This Source Code Form is subject to the terms of the Mozilla Public
//  License, v. 2.0. If a copy of the MPL was not distributed with this
//  file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
//  This Source Code Form is "Incompatible With Secondary Licenses", as
//  defined by the Mozilla Public License, v. 2.0.

package de.unpixelt.locale;

import com.google.common.cache.LoadingCache;

import java.util.HashMap;
import java.util.Map;

/**
 * LocaleCounter is counting how many players are using a {@code Locale}. If one
 * {@code Locale} counts down to {@code 0} then it is also cleared from the cache.
 *
 * @see LocaleChangeListener
 * @see Locale
 */
class LocaleCounter {

    private final LoadingCache<Locale, LocaleReader> cache;
    private final HashMap<Locale, Short> counts = new HashMap<>();;

    LocaleCounter(final LoadingCache<Locale, LocaleReader> cache) {
        this.cache = cache;
    }

    String usedLocales() {
        StringBuilder builder = new StringBuilder("\nLocale  :  Used \n");
        for (Map.Entry<Locale, Short> entry : counts.entrySet()) {
            byte keyLength = (byte) (7 - entry.getKey().name().length());
            builder.append(entry.getKey());
            for (; keyLength > 0; keyLength--) {
                builder.append(" ");
            }
            builder.append(" :  " + entry.getValue() + "\n");
        }
        return builder.toString();
    }

    /**
     * Increment the count of the locale by one.
     * @param locale the locale to increment
     */
    void increment(Locale locale) {
        short count = (short) (counts.getOrDefault(locale, (short) 0) + 1);
        counts.put(locale, count);
    }

    /**
     * Decrement the count of the locale by one.
     * <br>
     * If locale counts down to {@code 0} then it's cleared from cache.
     * @param locale the locale to decrement
     */
    void decrement(Locale locale) {
        short count = (short) (counts.getOrDefault(locale, (short) 0) - 1);

        if (count == 0) {
            counts.remove(locale);

            if (cache.getIfPresent(locale) == null) return;
            Translate.getDebugger().unloadFromCache(locale);
            cache.invalidate(locale);
            return;
        }

        counts.put(locale, count);
    }
}
