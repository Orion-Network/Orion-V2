package orion.mmorpg.players.abilities;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.jetbrains.annotations.NotNull;
import orion.mmorpg.events.OnGetEffect;
import orion.mmorpg.events.OnLaunchSpell;
import orion.mmorpg.events.OnSpellSense;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Knowledge;
import orion.mmorpg.utils.Rarity;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Support {

    protected String name;
    protected Rarity rarity;
    protected String description;
    Class<? extends Event>[] events;
    @Getter
    ArrayList<Class<? extends OEffect>> oEffects = new ArrayList<>();
    @Getter
    ArrayList<Class<? extends Spell>> spells = new ArrayList<>();
    protected HashMap<String, Object> dataMap;

    public Support(String name, Rarity rarity, String description, Class<? extends Event>[] events) {
        this.name = name;
        this.rarity = rarity;
        this.description = description;
        this.dataMap = new HashMap<>();
        this.events = events;
    }

    public Support(String name, Rarity rarity, String description, HashMap<String, Object> dataMap,
            Class<? extends Event>[] events) {
        this.name = name;
        this.rarity = rarity;
        this.description = description;
        this.dataMap = dataMap;
        this.events = events;
    }

    public void applyEffects(OrionPlayer player, long duration) {
        for (Class<? extends OEffect> oEffect : oEffects) {
            try {
                OEffect effect = oEffect.getConstructor(Long.class).newInstance(duration);
                Bukkit.getServer().getPluginManager()
                        .callEvent(new OnGetEffect(player, effect, duration, this.getName()));
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
                    | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeEffects(OrionPlayer player) {
        for (Class<? extends Event> event : events) {
            player.removeEffect(this.getName(), event);
        }
    }

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
