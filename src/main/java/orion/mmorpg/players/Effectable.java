package orion.mmorpg.players;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import orion.mmorpg.events.OnGetEffect;
import orion.mmorpg.players.abilities.OEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public interface Effectable {
    /**
     * A map of all the effects of the player.
     * <p>
     * The first key is the event name The second key is a map of all the effects of the event The key is the id of the
     * effect The value is the effect
     */
    LinkedHashMap<String, LinkedHashMap<String, OEffect>> orionEffects = new LinkedHashMap<>();

    void addEffect(String id, OEffect effect);

    default void removeEffect(String id, Class<? extends Event> event) {
        if (!orionEffects.containsKey(event.getSimpleName()))
            return;
        orionEffects.get(event.getSimpleName()).remove(id);
    }

    default LinkedHashMap<String, LinkedHashMap<String, OEffect>> getOrionEffects() {
        return orionEffects;
    }

    default void executeOEffect(Event event) {
        if (!orionEffects.containsKey(event.getClass().getSimpleName()))
            return;
        ArrayList<String> toRemove = new ArrayList<>();
        this.getOrionEffects().get(event.getEventName()).forEach((id, effect) -> {
            boolean remove = effect.execute(event);
            if (!remove)
                toRemove.add(id);
        });
        toRemove.forEach(id -> this.getOrionEffects().get(event.getEventName()).remove(id));
    }
}
