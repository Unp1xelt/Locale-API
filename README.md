# Locale-API

With this API you can easily translate
<ul>
    <li>Biomes</li>
    <li>Materials</li>
    <li>Effects</li>
    <li>Enchantments</li>
    <li>Entities</li>
    <li>Potions</li>
    <li>Tropical fish pattern</li>
    <li>Profession of a Villager</li>
</ul>

For more translations use ``Translate.getCustomValue(...)`` and lookup for keys
<a href="https://github.com/Unp1xelt/Locale-API/blob/master/src/main/resources/lang/en_us.json">here</a>.


## Setup

Make sure to add ``depend: [Locale-API]`` to your ``plugin.yml`` file and the 
``Locale-API.jar`` to the plugins folder of your server.


## Caution

Some ``Locale's`` like ``got_de, gv_im, kab_kab, mi_nz, moh_ca, nuk, oj_ca, qya_aa, tzl_tzl`` 
have a high amount of untranslated Strings (over 100) and should not be used.</br>
If used and the key is not found, it will use the translation of ``Locale.en_us``. 

### PlayerJoinEvent

When a player joins the server the player's ``Locale`` is initialized around 2 
seconds later. Because of that you have to delay any action were you want to use
the player's ``Locale`` in this event.
</br><strong>DO NOT</strong> use a delay below 50 Ticks. 
```java
@EventHandler
private void onPlayerJoinEvent(PlayerJoinEvent e) {
    Bukkit.getScheduler().runTaskLater(YOUR_PLUGIN, () -> {
        Locale locale = Translate.getLocale(e.getPlayer());
        ...
    }, 50L); // 2.5s
}
```

### PlayerLocaleChangeEvent

This event has the same issue as the PlayerJoinEvent. The change of the ``Locale`` 
is delayed updated in the player object. To get the new locale instantly use 
``e.getLocale()`` instead. 
```java
@EventHandler
private void onPlayerLocaleChange(PlayerLocaleChangeEvent e) {
    Locale newLocale = Locale.valueOf(e.getLocale());
    
    // DO NOT USE THIS
    Locale oldLocale = Translate.getLocale(e.getPlayer());
}
```

If you want to use the old ``Locale`` of the player later you should save it in 
a HashMap. Because after around 2 seconds the locale of the player object will be 
updated to the new ``Locale``.
```java
private final HashMap<Player, Locale> playerLocale = new HashMap<>();

@EventHandler
private void onPlayerLocaleChange(PlayerLocaleChangeEvent e) {
    Locale newLocale = Locale.valueOf(e.getLocale());

    Player p = e.getPlayer();
    playerLocale.put(p, Translate.getLocale(p));
    Locale oldLocale = playerLocale.get(e.getPlayer());
}
```


## Example

In this example we create a special chest item with a lore which shows the 
Material requirements to open it in the player's language used.

First we create the lore, but we save it in the ``LocalizedName``.
We separate each line with a ``,`` but you can use any character you like to use.
```java
public ItemStack getSpecialChest() {
    ItemStack item = new ItemStack(Material.ENDER_CHEST);
    ItemMeta meta = item.getItemMeta();
    
    List<String> lore = new ArrayList<>();
    lore.add("Required to open: ");
    lore.add("- §4{0}");
    lore.add("- §a{1}");

    meta.setLocalizedName(String.join(",", lore));
    item.setItemMeta(meta);
    return item;
}
```

</br>You can also instantly write it formatted.
```java
String lore = "Required to open: ,- $4{0},- §a{1}";
meta.setLocalizedName(lore);
```

</br>After that we create a method that replaces ``{0}`` and ``{1}`` with the
required Materials to open the special chest in the locale the player is using.
```java
public void updateLore(Locale locale, ItemStack item) {
    ItemMeta meta = item.getItemMeta();

    // Replace {0} and {1} with the Materials that are required
    String format = MessageFormat.format(meta.getLocalizedName(),
        Translate.getMaterial(locale, Material.REDSTONE_BLOCK),     // {0} 
        Translate.getMaterial(locale, Material.EMERALD));           // {1}

    meta.setLore(Arrays.asList(format.split(",")));
    item.setItemMeta(meta);
}

public void updateLore(Player p, ItemStack item) {
    updateLore(Translate.getLocale(p), item);
}
```

</br>Last but not least events.
```java
// Give every player that joins a special chest.
@EventHandler
private void onPlayerSpawnEvent(PlayerJoinEvent e) {
    // Because the locale of a player is initialized later, we have to delay
    // this event. DO NOT go below 50 Ticks (2.5s) or else the locale will
    // not be initialized properly.
    Bukkit.getScheduler().runTaskLater(YOUR_PLUGIN, () -> {
        Player p = e.getPlayer();
        ItemStack specialChest = getSpecialChest();
        updateLore(p, specialChest);
        p.getInventory().addItem(specialChest);
    }, 50L);
}

// When a special chest is picked up it will update the requirements in the lore to
// the player's locale.
@EventHandler
private void onPlayerPickup(EntityPickupItemEvent e) {
    if (e.getEntity() instanceof Player) {
        Player p = (Player) e.getEntity();
        ItemStack item = e.getItem().getItemStack();

        if (item.getType() == Material.ENDER_CHEST) {
            updateLore(p, item);
        }
    }
}

// When the player change his locale it will also update the requirements in the 
// lore to the new selected locale.
@EventHandler
private void onPlayerLocaleChange(PlayerLocaleChangeEvent e) {
    for (ItemStack item : e.getPlayer().getInventory()) {
        if (item == null) continue;
        
        if (item.getType() == Material.ENDER_CHEST) {
            // Use 'e.getLocale()' because the locale of 'e.getPlayer()' is 
            // delayed updated. 
            updateLore(Locale.valueOf(e.getLocale()), item);
        }
    }
}
```
It's not the best for performance but this example is used to show what you can
do with this API.

</br><strong>Have fun coding </strong>:heart::fox_face:
