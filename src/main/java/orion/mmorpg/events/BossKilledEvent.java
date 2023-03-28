package orion.mmorpg.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import orion.mmorpg.entities.samples.AbstractBoss;

public class BossKilledEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private AbstractBoss boss;
    private Player player;

    public BossKilledEvent(AbstractBoss boss, Player player) {
        this.boss = boss;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public AbstractBoss getBoss() {
        return boss;
    }

    public Player getPlayer() {
        return player;
    }
}
