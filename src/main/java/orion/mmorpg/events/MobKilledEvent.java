package orion.mmorpg.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import orion.mmorpg.entities.samples.AbstractMob;

public class MobKilledEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final AbstractMob mob;
    private final Player killer;

    public MobKilledEvent(AbstractMob mob, Player killer) {
        this.mob = mob;
        this.killer = killer;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public AbstractMob getMob() {
        return mob;
    }

    public Player getKiller() {
        return killer;
    }
}
