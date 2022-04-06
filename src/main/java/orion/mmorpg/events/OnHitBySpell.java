package orion.mmorpg.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;

public class OnHitBySpell extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;

    Player damager, entity;
    OrionPlayer damagerStats, entityStats;
    Spell s;
    double damage;

    public OnHitBySpell(Player damager, Player entity, OrionPlayer damagerStats, OrionPlayer entityStats, Spell s, float damage) {
        this.cancelled = false;
        this.damager = damager;
        this.entity = entity;
        this.damagerStats = damagerStats;
        this.entityStats = entityStats;
        this.s = s;
        this.damage = damage;
    }



    @NotNull
    @Override
    public HandlerList getHandlers() {
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
