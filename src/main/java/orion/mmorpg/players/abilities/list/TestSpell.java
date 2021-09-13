package orion.mmorpg.players.abilities.list;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.utils.Element;
import orion.mmorpg.utils.Rarity;

public class TestSpell extends Spell {
    public TestSpell() {
        super("TEST_SPELL", 0, Element.ARCANE, 1000);
    }

    @Override
    public void launch(Mmorpg mmorpg, Player player, OrionPlayer orionPlayer) {
        Bukkit.broadcastMessage("worked");
        super.launch(mmorpg, player, orionPlayer);
    }
}
