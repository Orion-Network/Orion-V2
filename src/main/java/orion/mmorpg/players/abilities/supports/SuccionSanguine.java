package orion.mmorpg.players.abilities.supports;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import orion.mmorpg.events.OnSpellSense;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.Support;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Utils;

public class SuccionSanguine extends Support {
    public SuccionSanguine() {
        super("Succion Sanguine", Rarity.RARE, "Vous soigne pour 2% de vos dégats infligés.");
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {

    }

    @Override
    public void onLaunchSpell(Player player, OrionPlayer stats, Spell s) {

    }

    @Override
    public void onHit(EntityDamageByEntityEvent event, OrionPlayer stats) {
        float totalDamage = Utils.calculateDamage(stats.getStats(), (float) event.getDamage()) * ((Player) event.getDamager()).getAttackCooldown();
        stats.heal(totalDamage / 50);
    }

    @Override
    public void onShootArrow(EntityShootBowEvent event) {

    }

    @Override
    public void onHitByEntity(EntityDamageByEntityEvent entityEvent) {
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
    public void onSwitchSlot(Player player, OrionPlayer stats, PlayerItemHeldEvent event) {

    }

    @Override
    public void onSupportEnd(Player p, OrionPlayer stats) {

    }
}
