package orion.mmorpg.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;

public class OnLaunchSpell extends Event implements Cancellable {

        private static final HandlerList HANDLERS = new HandlerList();
        private boolean cancelled;
        @Getter
        private Player player;
        @Getter
        private OrionPlayer stats;
        @Getter
        private Spell spell;

        public OnLaunchSpell(Player player, OrionPlayer stats, Spell spell) {
                this.player = player;
                this.stats = stats;
                this.spell = spell;
        }


    public static HandlerList getHandlerList() {
        return HANDLERS;
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
