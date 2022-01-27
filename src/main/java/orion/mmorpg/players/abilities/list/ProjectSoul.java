package orion.mmorpg.players.abilities.list;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import orion.mmorpg.events.OnSpellSense;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.Support;
import orion.mmorpg.utils.Rarity;

public class ProjectSoul extends Support {

    public ProjectSoul() {
        super("Project:Soul", Rarity.MYTHIC, "Un project de modification humain n'ayant pas aboutit");
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {

    }

    @Override
    public void onLaunchSpell(Player player, OrionPlayer stats, Spell s) {

    }

    @Override
    public void onHit(EntityDamageByEntityEvent event, OrionPlayer stats) {

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
    public void onSupportEnd(Player p, OrionPlayer stats) {

    }
}
