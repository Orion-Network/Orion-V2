package orion.mmorpg.players.abilities;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import orion.mmorpg.events.OnSpellSense;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Rarity;

import java.util.HashMap;

public abstract class Support {
    String name;
    Rarity rarity;
    String description;
    HashMap<String, Object> dataMap;

    public Support(String name, Rarity rarity, String description) {
        this.name = name;
        this.rarity = rarity;
        this.description = description;
        this.dataMap = new HashMap<>();
    }

    public Support(String name, Rarity rarity, String description, HashMap<String, Object> dataMap) {
        this.name = name;
        this.rarity = rarity;
        this.description = description;
        this.dataMap = dataMap;

    }

    public abstract void onInteract(PlayerInteractEvent event);

    public abstract void onLaunchSpell(Player player, OrionPlayer stats, Spell s);

    public abstract void onHit(EntityDamageByEntityEvent event, OrionPlayer stats);

    public abstract void onShootArrow(EntityShootBowEvent event);

    public abstract void onHitByEntity(EntityDamageByEntityEvent entityEvent);

    /*public void onHitBySpell(onHitBySpell e, Player player, PlayerStats stats, Entity caster, SpellType type, Spell s) {};*/
    public abstract void onSpellSense(OnSpellSense e);

    public abstract void onTick();

    public abstract void onDie();

    //TODO Implementing
    public abstract void onSupportEnd(Player p, OrionPlayer stats);

    public String getName() {
        return name;
    }

    public Rarity getRank() {
        return rarity;
    }

    public String getDescription() {
        return description;
    }

    public Support addData(String key, Object value) {
        this.setDataMap(key, value);
        return this;
    }


    public void setDataMap(@NotNull String s, @NotNull Object value) {
        dataMap.put(s, value);
    }


    public @NotNull Object getData(@NotNull String s) {
        return dataMap.get(s);
    }

    public boolean hasData(@NotNull String s) {
        return dataMap.containsKey(s);
    }

    public void removeMetadata(@NotNull String s) {
        dataMap.remove(s);
    }
}
