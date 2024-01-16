package orion.mmorpg.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import orion.mmorpg.players.OrionPlayer;

public class RespawnEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    @Getter
    Player player;

    @Getter
    OrionPlayer orionPlayer;

    public RespawnEvent(Player player, OrionPlayer orionPlayer) {
        this.player = player;
        this.orionPlayer = orionPlayer;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
