package orion.mmorpg.players.abilities.effects;

import org.bukkit.event.Event;
import orion.mmorpg.events.OnSpellSense;
import orion.mmorpg.players.abilities.OEffect;
import orion.mmorpg.utils.Utils;

public class SpellAnnulation extends OEffect {

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
