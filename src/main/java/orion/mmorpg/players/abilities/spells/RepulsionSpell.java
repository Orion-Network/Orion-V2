package orion.mmorpg.players.abilities.spells;

import org.bukkit.entity.Player;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.utils.Element;

public class RepulsionSpell extends Spell {
    //TODO Implement
    public RepulsionSpell() {
        super("REPULSION", 30, Element.ARCANE, 15*1000);
    }

    @Override
    public void launch(Mmorpg mmorpg, Player player, OrionPlayer orionPlayer) {

    }

}
