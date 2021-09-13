package orion.mmorpg.players.abilities.list;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.utils.Element;
import orion.mmorpg.utils.Rarity;

public class EternalFrost extends Spell {
    public EternalFrost() {
        super("ETERNAL_FROST", 100, Element.ICE, 60*60*1000);
    }

    @Override
    public void launch(Mmorpg mmorpg, Player player, OrionPlayer orionPlayer) {
        super.launch(mmorpg, player, orionPlayer);
    }
}
