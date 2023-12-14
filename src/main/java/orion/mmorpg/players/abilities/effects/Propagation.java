package orion.mmorpg.players.abilities.effects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.events.OnGetEffect;
import orion.mmorpg.events.PlayerDamagedByEntityEvent;
import orion.mmorpg.players.abilities.OEffect;
import orion.mmorpg.utils.Utils;

public class Propagation extends OEffect {
    public Propagation(Long duration) {
        super("Propagation", "Propagate effects to nearby players.", OnGetEffect.class, duration);
    }


    @Override
    public void onEvent(Event event) {
        OnGetEffect e = (OnGetEffect) event;
        if(((OnGetEffect) event).getEffect() == this) return;
        Player player = Bukkit.getPlayer(e.getPlayer().getUniqueId());

        for(Entity entity : player.getNearbyEntities(5, 5, 5)) {
            if(entity instanceof Player nearbyPlayer) {
                if(nearbyPlayer.getUniqueId() != player.getUniqueId()) {
                    Bukkit.getPluginManager().callEvent(
                            new OnGetEffect(Mmorpg.INSTANCE.getPlayerManager().getPlayer(nearbyPlayer.getUniqueId()), e.getEffect(), e.getDuration(), "Propagation")
                    );
                }
            }
        }
    }
}
