package orion.mmorpg.players.abilities.spells;

import org.bukkit.entity.Player;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.utils.Element;

public class Heal extends Spell {
    public Heal() {
        super("HEAL", 1, Element.WHITE, 1000*60);
    }

    @Override
    public void launch(Mmorpg mmorpg, Player player, OrionPlayer orionPlayer) {
        orionPlayer.heal(orionPlayer.getStats().getVitality()/10);
    }
}
