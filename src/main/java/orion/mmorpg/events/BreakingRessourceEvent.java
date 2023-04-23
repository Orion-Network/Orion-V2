package orion.mmorpg.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import orion.mmorpg.items.sample.BasicTool;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.world.Ressource;

public class BreakingRessourceEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();


    @Getter
    BasicTool tool;

    @Getter
    Player player;
    @Getter
    OrionPlayer orionPlayer;

    @Getter
    Ressource ressource;

    public BreakingRessourceEvent(BasicTool tool, Player player, OrionPlayer orionPlayer, Ressource ressource) {
        this.tool = tool;
        this.player = player;
        this.orionPlayer = orionPlayer;
        this.ressource = ressource;
    }


    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
