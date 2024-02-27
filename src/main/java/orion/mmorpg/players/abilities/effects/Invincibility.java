package orion.mmorpg.players.abilities.effects;

import org.bukkit.event.Event;
import orion.mmorpg.events.PlayerDamagedByEntityEvent;
import orion.mmorpg.players.abilities.OEffect;

public class Invincibility extends OEffect {

    public Invincibility(long duration) {
        super("Invincibility", "Makes the player invincible for a short period of time.",
                PlayerDamagedByEntityEvent.class, duration);
    }

    @Override
    public void onEvent(Event event) {
        PlayerDamagedByEntityEvent e = (PlayerDamagedByEntityEvent) event;
        e.setCancelled(true);
    }
}
