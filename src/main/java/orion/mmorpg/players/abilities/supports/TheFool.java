package orion.mmorpg.players.abilities.supports;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import orion.mmorpg.events.OnLaunchSpell;
import orion.mmorpg.events.OnSpellSense;
import orion.mmorpg.events.PlayerDamageEntityEvent;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.OEffect;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.Support;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Utils;

public class TheFool extends Support {

    public TheFool() {
        super("The Fool", Rarity.MYTHIC, "Un pouvoir mystérieux annulant toute magie environnante.", new Class[]{OnSpellSense.class});
        this.getOEffects().add(SpellAnnulation.class);
    }

    public static class SpellAnnulation extends OEffect {

            public SpellAnnulation(Long duration) {
                super("Spell Annulation", "Annule les sorts", OnSpellSense.class, duration);
            }

            @Override
            public void onEvent(Event event) {
                OnSpellSense onLaunchSpell = (OnSpellSense) event;
                onLaunchSpell.getSpell().setCanceled(true);
                Utils.sendInfoMessage(onLaunchSpell.getSender(), "Votre sort a été annulé.");
            }
    }

}
