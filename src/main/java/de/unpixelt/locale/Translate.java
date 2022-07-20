//  Copyright (C) 2022 Unp1xelt. All rights reserved.
//
//  This Source Code Form is subject to the terms of the Mozilla Public
//  License, v. 2.0. If a copy of the MPL was not distributed with this
//  file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
//  This Source Code Form is "Incompatible With Secondary Licenses", as
//  defined by the Mozilla Public License, v. 2.0.

package de.unpixelt.locale;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * With {@code Translate} you can translate
 * <ul>
 *     <li>{@link Translate#getBiome(Player, Biome) Biomes}
 *     <li>{@link Translate#getMaterial(Player, Material) Materials}
 *     <li>{@link Translate#getPotion(Player, PotionType, PotionSort) Potions}
 *     <li>{@link Translate#getEffect(Player, PotionEffectType) PotionEffects}
 *     <li>{@link Translate#getEnchantment(Player, Enchantment) Enchantments}
 *     <li>{@link Translate#getEntity(Player, EntityType) Entities}
 *     <li>{@link Translate#getTropicalFish(Player, TropicalFish.Pattern) TropicalFishes}
 *     <li>{@link Translate#getVillager(Player, Villager.Profession) Villager}
 * </ul>
 * with the giving {@link Locale} or {@link Player}(uses {@link Translate#getLocale(Player)})
 * <p>
 * When a translation is requested the specific {@code Locale} is cached for two
 * minutes. If in that two minutes no further requests are taken it will be
 * automatically cleared from the cache.
 */
public final class Translate extends JavaPlugin {

    private static final LoadingCache<Locale, LocaleReader> CACHE =
            CacheBuilder.newBuilder().expireAfterAccess(2, TimeUnit.MINUTES).build(new LocaleCacheLoader());
    private static final Debugger DEBUGGER = new Debugger();

    private static Plugin plugin;

    static final String COMMAND_NAME = "usedlocales";

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEnable() {
        plugin = this;

        final LocaleCounter counter = new LocaleCounter(CACHE);
        final UsedLocalesCommand command = new UsedLocalesCommand(counter);

        getServer().getPluginManager().registerEvents(new LocaleChangeListener(counter), this);
        getCommand(COMMAND_NAME).setExecutor(command);

        DEBUGGER.setShowCaching(true);
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    /**
     * Returns the debugger instance.
     * @return {@link Debugger}
     */
    public static Debugger getDebugger() {
        return DEBUGGER;
    }

    /**
     * Returns the current local the player is using.
     * @param p the player
     * @return {@link Locale} the player's locale
     */
    public static Locale getLocale(Player p) {
        return Locale.valueOf(p.getLocale());
    }

    /**
     * Returns the translation of the key's value.
     * @param key the key to translate
     * @param locale the locale to translate
     * @return Translation of the key as {@link String}
     */
    public static String getCustomKey(String key, Locale locale) {
        return CACHE.getUnchecked(locale).getValue(key);
    }

    /**
     * Returns all keys for the locale.
     *
     * @return An unmodifiable {@code List} containing all keys for this locale
     */
    public static List<String> getAllKey(Locale locale) {
        return CACHE.getUnchecked(locale).getKeys();
    }

    public static String getBiome(Player p, Biome biome) {
        return getBiome(getLocale(p), biome);
    }

    public static String getBiome(Locale locale, Biome biome) {
        if (biome == Biome.CUSTOM) return null;
        return CACHE.getUnchecked(locale).getValue("biome.minecraft." + biome.name());
    }

    public static Map<Biome, String> getBiomes(Locale locale) {
        Map<Biome, String> biomes = new HashMap<>();

        for(Biome biome : Biome.values()) {
            biomes.put(biome, getBiome(locale, biome));
        }

        return biomes;
    }


    public static String getMaterial(Player p, Material mat) {
        return getMaterial(getLocale(p), mat);
    }

    public static String getMaterial(Locale locale, Material mat) {
        LocaleReader reader = CACHE.getUnchecked(locale);
        String name = mat.name();
        if (name.contains("WALL_")) name = name.replace("WALL_", "");

        if (mat.isBlock()) {
            return reader.getValue("block.minecraft." + name);
        } else {
            return reader.getValue("item.minecraft." + name);
        }
    }

    public static Map<Material, String> getMaterials(Locale locale) {
        Map<Material, String> materials = new HashMap<>();

        for(Material mat : Material.values()) {
            materials.put(mat, getMaterial(locale, mat));
        }

        return materials;
    }


    /**
     * An enum of all potion types.
     */
    public enum PotionSort {
        POTION,
        SPLASH_POTION,
        LINGERING_POTION,
    }

    public static String getPotion(Player p, PotionType type, PotionSort sort) {
        return getPotion(getLocale(p), type, sort);
    }

    public static String getPotion(Locale locale, PotionType type, PotionSort sort) {
        String name = type.name();
        switch (type) {
            case REGEN:
                name = type.getEffectType().getName();
                break;
            case INSTANT_HEAL:
                name = "HEALING";
                break;
            case INSTANT_DAMAGE:
                name = "HARMING";
                break;
            case JUMP:
                name = "LEAPING";
                break;
            case UNCRAFTABLE:
                name = "EMPTY";
                break;
            case SPEED:
                name = "SWIFTNESS";
                break;
        }

        return CACHE.getUnchecked(locale).getValue("item.minecraft." + sort.name() + ".effect." + name);
    }

    public static Map<PotionType, String> getPotions(Locale locale, PotionSort sort) {
        Map<PotionType, String> potions = new HashMap<>();

        for(PotionType type : PotionType.values()) {
            potions.put(type, getPotion(locale, type, sort));
        }

        return potions;
    }


    public static String getEffect(Player p, PotionEffectType type) {
        return getEffect(getLocale(p), type);
    }

    public static String getEffect(Locale locale, PotionEffectType type) {
        String name = type.getName();
        switch (type.getId()) {
            case 2:
                name = "SLOWNESS";
                break;
            case 3:
                name = "HASTE";
                break;
            case 4:
                name = "MINING_FATIGUE";
                break;
            case 5:
                name = PotionType.STRENGTH.name();
                break;
            case 6:
                name = "INSTANT_HEALTH";
                break;
            case 7:
                name = PotionType.INSTANT_DAMAGE.name();
                break;
            case 8:
                name = "JUMP_BOOST";
                break;
            case 9:
                name = "NAUSEA";
                break;
            case 11:
                name = "RESISTANCE";
                break;
        }

        return CACHE.getUnchecked(locale).getValue("effect.minecraft." + name);
    }

    public static Map<PotionEffectType, String> getEffects(Locale locale) {
        Map<PotionEffectType, String> effects = new HashMap<>();

        for(PotionEffectType type : PotionEffectType.values()) {
            effects.put(type, getEffect(locale, type));
        }

        return effects;
    }


    public static String getEnchantment(Player p, Enchantment ench) {
        return getEnchantment(getLocale(p), ench);
    }

    public static String getEnchantment(Locale locale, Enchantment ench) {
        return CACHE.getUnchecked(locale).getValue("enchantment.minecraft." + ench.getKey().getKey());
    }

    public static Map<Enchantment, String> getEnchantments(Locale locale) {
        Map<Enchantment, String> enchantments = new HashMap<>();

        for(Enchantment ench : EnchantmentWrapper.values()) {
            enchantments.put(ench, getEnchantment(locale, ench));
        }

        return enchantments;
    }


    public static String getEntity(Player p, EntityType type) {
        return getEntity(getLocale(p), type);
    }

    public static String getEntity(Locale locale, EntityType type) {
        if (type == EntityType.UNKNOWN) return CACHE.getUnchecked(locale).getValue("entity.notFound");
        return CACHE.getUnchecked(locale).getValue("entity.minecraft." + type.getKey().getKey());
    }

    public static Map<EntityType, String> getEntities(Locale locale) {
        Map<EntityType, String> enchantments = new HashMap<>();

        for(EntityType type : EntityType.values()) {
            enchantments.put(type, getEntity(locale, type));
        }

        return enchantments;
    }


    public static String getVillager(Player p, Villager.Profession type) {
        return getVillager(getLocale(p), type);
    }

    public static String getVillager(Locale locale, Villager.Profession type) {
        return CACHE.getUnchecked(locale).getValue("entity.minecraft.villager." + type.name());
    }

    public static Map<Villager.Profession, String> getVillagers(Locale locale) {
        Map<Villager.Profession, String> profession = new HashMap<>();

        for(Villager.Profession type : Villager.Profession.values()) {
            profession.put(type, getVillager(locale, type));
        }

        return profession;
    }


    public static String getTropicalFish(Player p, TropicalFish.Pattern type) {
        return getTropicalFish(getLocale(p), type);
    }

    public static String getTropicalFish(Locale locale, TropicalFish.Pattern type) {
        return CACHE.getUnchecked(locale).getValue("entity.minecraft.tropical_fish.type." + type.name());
    }

    public static Map<TropicalFish.Pattern, String> getTropicalFishes(Locale locale) {
        Map<TropicalFish.Pattern, String> pattern = new HashMap<>();

        for(TropicalFish.Pattern type : TropicalFish.Pattern.values()) {
            pattern.put(type, getTropicalFish(locale, type));
        }

        return pattern;
    }
}
