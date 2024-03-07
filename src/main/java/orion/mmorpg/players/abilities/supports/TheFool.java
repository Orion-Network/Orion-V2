package orion.mmorpg.players.abilities.supports;

import orion.mmorpg.events.OnSpellSense;
import orion.mmorpg.players.abilities.Support;
import orion.mmorpg.players.abilities.effects.SpellAnnulation;
import orion.mmorpg.players.abilities.spells.Switch;
import orion.mmorpg.utils.Rarity;

public class TheFool extends Support {

    public TheFool() {
        super("The Fool", Rarity.MYTHIC, "Un pouvoir mystérieux annulant toute magie environnante.",
                new Class[] { OnSpellSense.class });
        this.getOEffects().add(SpellAnnulation.class);

        this.getSpells().add(Switch.class);
    }

}
