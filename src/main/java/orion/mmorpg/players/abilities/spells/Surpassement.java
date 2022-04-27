package orion.mmorpg.players.abilities.spells;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.events.OnSpellSense;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.Support;
import orion.mmorpg.utils.Element;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Utils;

public class Surpassement extends Spell {
    public Surpassement() {
        super("BOOSTED_MODE", 5, Element.EARTH, 1000 * 60 * 5);
    }

    @Override
    public void launch(Mmorpg mmorpg, Location Location) {
        if(getOrionCaster() != null)
            getOrionCaster().getSupportMap().put(new BoostedMode(), (1000 * 60) + System.currentTimeMillis());
    }

    class BoostedMode extends Support {
        public BoostedMode() {
            super("Boosted Mode", Rarity.UNCOMMON, "Vous fais entrer dans une rage augmentant vos dégats et réduisant ceux reçus");
        }

        @Override
        public void onInteract(PlayerInteractEvent event) {

        }

        @Override
        public void onLaunchSpell(Player player, OrionPlayer stats, Spell s) {

        }

        @Override
        public void onHit(EntityDamageByEntityEvent event, OrionPlayer stats) {
            event.setDamage(event.getDamage() * 1.5);
            Utils.sendDebugMessage(event.getDamager(), event.getDamage() + " f damages");
        }

        @Override
        public void onShootArrow(EntityShootBowEvent event) {

        }

        @Override
        public void onHitByEntity(EntityDamageByEntityEvent entityEvent) {
            entityEvent.setDamage(entityEvent.getDamage() / 0.3);
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
}
