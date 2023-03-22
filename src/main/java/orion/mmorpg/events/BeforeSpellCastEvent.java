package orion.mmorpg.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.utils.Knowledge;

public class BeforeSpellCastEvent extends Event {
    public static final HandlerList HANDLERS = new HandlerList();
    private final Spell spell;
    public BeforeSpellCastEvent(Spell spell) {
        this.spell = spell;
    }

    public Spell getSpell() {
        return spell;
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
