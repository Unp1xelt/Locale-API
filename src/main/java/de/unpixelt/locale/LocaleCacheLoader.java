//  Copyright (C) 2022 Unp1xelt. All rights reserved.
//
//  This Source Code Form is subject to the terms of the Mozilla Public
//  License, v. 2.0. If a copy of the MPL was not distributed with this
//  file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
//  This Source Code Form is "Incompatible With Secondary Licenses", as
//  defined by the Mozilla Public License, v. 2.0.

package de.unpixelt.locale;

import com.google.common.cache.CacheLoader;

/**
 * LocaleCacheLoader is used for loading a specific {@code LocaleReader} with
 * the provided {@code Locale} to the cache.
 *
 * @see CacheLoader
 */
class LocaleCacheLoader extends CacheLoader<Locale, LocaleReader> {

    /**
     * Loads an {@code LocaleReader} with the provided {@code Locale}.
     *
     * @param key The {@code Locale} whose {@code LocaleReader} should be loaded
     * @return {@link LocaleReader} of the provided {@code Locale}
     * @throws Exception if unable to load the result
     * @throws InterruptedException if this method is interrupted.
     *         {@code InterruptedException} is treated like any other
     *         {@code Exception} in all respects except that, when it is caught,
     *         the thread's interrupt status is set.
     */
    @Override
    public LocaleReader load(Locale key) throws Exception {
        Translate.getDebugger().loadToCache(key);
        return new LocaleReader(key);
    }
}
