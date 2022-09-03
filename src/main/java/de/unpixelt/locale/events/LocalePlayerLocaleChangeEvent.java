package de.unpixelt.locale.events;

import de.unpixelt.locale.Locale;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a player changes their locale in the client settings.
 * <br>
 * Only use this event if you want explicitly to translate something.
 */
public class LocalePlayerLocaleChangeEvent extends PlayerEvent {
    private static final HandlerList HANDLERS = new HandlerList();

    private final Locale locale;

    private final Locale oldLocale;

    public LocalePlayerLocaleChangeEvent(@NotNull final Player who, @NotNull final Locale locale,
                                         @NotNull final Locale oldLocale) {
        super(who);
        this.locale = locale;
        this.oldLocale = oldLocale;
    }

    /**
     * Gets the local of the player.
     *
     * @return The player's new locale.
     */
    @NotNull
    public Locale getLocale() {
        return locale;
    }

    /**
     * Gets the old local the player used before.
     *
     * @return The old player's locale. Null when had no locale before.
     */
    @Nullable
    public Locale getOldLocale() {
        return oldLocale;
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
