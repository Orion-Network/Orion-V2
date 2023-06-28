package orion.mmorpg.players.abilities.spells;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.events.OnLaunchSpell;
import orion.mmorpg.events.OnSpellSense;
import orion.mmorpg.events.PlayerDamageEntityEvent;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.OEffect;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.Support;
import orion.mmorpg.utils.Element;
import orion.mmorpg.utils.Knowledge;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Utils;

public class Surpassement extends Spell {
    public Surpassement() {
        super("BOOSTED_MODE", 5, Element.EARTH, 1000 * 60 * 5);
    }

    @Override
    public void launch(Mmorpg mmorpg, Location Location, Knowledge knowledge) {
        if(getOrionCaster() != null)
            getOrionCaster().addEffect("BOOST", new Boost((long) (1000 * 60 * 5)));
    }

    public static class Boost extends OEffect {

            public Boost(Long duration) {
                super("BOOST", "Boosted mode", PlayerDamageEntityEvent.class, duration);
            }

            @Override
            public void onEvent(Event event) {
                PlayerDamageEntityEvent e = (PlayerDamageEntityEvent) event;
                e.setDamage(e.getDamage() * 2);
            }
    }
}
