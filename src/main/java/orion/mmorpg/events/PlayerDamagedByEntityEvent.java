package orion.mmorpg.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;

public class PlayerDamagedByEntityEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    EntityDamageByEntityEvent event;

    public PlayerDamagedByEntityEvent(EntityDamageByEntityEvent event) {
        this.event = event;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return (Player) event.getEntity();
    }

    public OrionPlayer getOrionPlayer() {
        return OrionPlayer.getOrionPlayer(getPlayer());
    }

    public Entity getDamager() {
        return event.getDamager();
    }

    public double getDamage() {
        return event.getDamage();
    }

    public void setDamage(double damage) {
        event.setDamage(damage);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancelled) {
        event.setCancelled(cancelled);
    }
}
