package orion.mmorpg.events;

import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.OEffect;

public class OnGetEffect extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;

    @Getter
    private final OrionPlayer player;
    @Getter
    private final OEffect effect;
    @Getter
    private final long duration;
    @Getter
    private String cause = "Unknown";

    public OnGetEffect(OrionPlayer player, OEffect effect, long duration) {
        this.cancelled = false;
        this.player = player;
        this.effect = effect;
        this.duration = duration;
    }

    public OnGetEffect(OrionPlayer player, OEffect effect, long duration, String cause) {
        this.cancelled = false;
        this.player = player;
        this.effect = effect;
        this.duration = duration;
        this.cause = cause;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
