package orion.mmorpg.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.SpellList;

public class OnSpellSense extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    Player sender, target;
    Spell s;
    SpellList type;
    Target targetType;

    public OnSpellSense(boolean isAsync, Player sender, Player target, SpellList type, Spell s, Target targetType) {
        super(isAsync);
        this.sender = sender;
        this.target = target;
        this.type = type;
        this.s = s;
        this.targetType = targetType;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Player getSender() {
        return sender;
    }

    public Player getTarget() {
        return target;
    }

    public Spell getSpell() {
        return s;
    }

    public SpellList getType() {
        return type;
    }

    public Target getTargetType() {
        return targetType;
    }

    public enum Target {
        DIRECT, INDIRECT, ZONE, GLOBAL
    }
}
