package orion.mmorpg.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;
import orion.mmorpg.players.OrionPlayer;

public class PlayerDamageEntityEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private EntityDamageByEntityEvent event;

    public PlayerDamageEntityEvent(EntityDamageByEntityEvent event) {
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
        return (Player) event.getDamager();
    }

    public OrionPlayer getOrionPlayer() {
        return OrionPlayer.getOrionPlayer(getPlayer());
    }

    public Entity getTarget() {
        return event.getEntity();
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
