package orion.mmorpg.players.abilities.supports;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.events.OnSpellSense;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.Support;
import orion.mmorpg.utils.Rarity;

public class Soleil extends Support {
    public Soleil() {
        super("Soleil", Rarity.MYTHIC, "Augmente votre force selon l'heure de la journée");
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {

    }

    @Override
    public void onLaunchSpell(Player player, OrionPlayer stats, Spell s) {

    }

    @Override
    public void onHit(EntityDamageByEntityEvent event, OrionPlayer stats) {
        event.setDamage(event.getDamage() * getMultiplier(event.getDamager().getWorld().getTime()));
    }

    @Override
    public void onShootArrow(EntityShootBowEvent event) {
        Long time = event.getProjectile().getWorld().getTime();
        float damages = event.getProjectile().getMetadata("damage").get(0).asFloat();
        event.getProjectile().setMetadata("damage", new FixedMetadataValue(Mmorpg.INSTANCE, damages * getMultiplier(time)));
    }

    @Override
    public void onHitByEntity(EntityDamageByEntityEvent event) {
        event.setDamage(event.getDamage() / getMultiplier(event.getDamager().getWorld().getTime()));
    }

    @Override
    public void onSpellSense(OnSpellSense e) {

    }

    @Override
    public void onTick() {

    }

    @Override
    public void onDie() {

    }

    @Override
    public void onSupportEnd(Player p, OrionPlayer stats) {

    }

    protected float getMultiplier(Long time) {
        if (time > 0 && time < 12000) {
            if (time >= 6000 && time <= 6020) {
                return 100;
            }
            return (time < 6000 ? time / 1000 : (12000 - time) / 1000);
        }
        return 1;
    }
}
