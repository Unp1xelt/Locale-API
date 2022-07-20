//  Copyright (C) 2022 Unp1xelt. All rights reserved.
//
//  This Source Code Form is subject to the terms of the Mozilla Public
//  License, v. 2.0. If a copy of the MPL was not distributed with this
//  file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
//  This Source Code Form is "Incompatible With Secondary Licenses", as
//  defined by the Mozilla Public License, v. 2.0.

package de.unpixelt.locale;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * LocalReader is used for loading a {@code Locale} from the resources folder and
 * accessing their keys and values.
 *
 * <p> All keys are in American English and lowercase. Their represent
 * <ul>
 *     <li>*Advancements
 *     <li>*Arguments for Commands
 *     <li>*Attributes
 *     <li>Biomes <b>({@link org.bukkit.block.Biome})</b>
 *     <li>Blocks and Items <b>({@link org.bukkit.Material})</b>
 *     <li>*Commands
 *     <li>Effects <b>({@link org.bukkit.potion.PotionEffectType})</b>
 *     <li>Enchantments <b>({@link org.bukkit.enchantments.Enchantment})</b>
 *     <li>Entities <b>({@link org.bukkit.entity.EntityType})</b>
 *     <li>*GameRules
 *     <li>*GUI of player client
 *     <li>{@code PotionSort} <b>({@link org.bukkit.potion.PotionType})</b>
 *     <li>*Subtitles
 *     <li>TropicalFishes <b>({@link org.bukkit.entity.TropicalFish.Pattern})</b>
 *     <li>Villager <b>({@link org.bukkit.entity.Villager.Profession})</b>
 * </ul>
 *
 * <p> The values are the translation of the {@code Locale} for the specific
 * object that the key represents.
 *
 * @see Translate.PotionSort
 * @see java.util.Locale
 */
class LocaleReader {

    private final JsonObject json;

    /**
     * Constructs an LocaleReader for the given locale.
     *
     * @param locale the locale used
     */
    LocaleReader(@NotNull Locale locale) {
        String fileName = locale.name() + ".json";
        InputStream inputStream = getFileFromResourceAsStream( "lang/" + fileName);
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        Gson gson = new Gson();

        this.json = gson.fromJson(reader, JsonObject.class);
    }

    /**
     * Returns the value with the specific key. This key will be lowercase.
     *
     * @param key name of the key that is requested.
     * @return Value as {@code String}. If this key does not exist {@code null}
     *         is returned.
     */
    String getValue(@NotNull String key) {
        return json.get(key.toLowerCase()).getAsString();
    }

    /**
     * Returns all keys for the locale.
     *
     * @return An unmodifiable {@code List} containing all keys for this locale
     * @see Collections#unmodifiableList(List)
     */
    List<String> getKeys() {
        List<String> keys = new ArrayList<>();

        for(Map.Entry<String, JsonElement> entry : json.entrySet()) {
            keys.add(entry.getKey());
        }
        return Collections.unmodifiableList(keys);
    }

    /**
     * Returns an {@code InputStream} from the resource.
     *
     * @param path the resource filepath
     * @return {@link InputStream} from the resource file
     * @throws IllegalArgumentException If this path to the resource does not
     *         exist
     */
    @NotNull
    private InputStream getFileFromResourceAsStream(String path) {
        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(path);

        // The stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("Filepath not found! " + path);
        } else {
            return inputStream;
        }
    }
}
