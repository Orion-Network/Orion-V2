package orion.mmorpg.players.abilities.spells;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.utils.Element;

public class TestSpell extends Spell {
    public TestSpell() {
        super("TEST_SPELL", 0, Element.ARCANE, 1000);
    }

    @Override
    public void launch(Mmorpg mmorpg, Player player, OrionPlayer orionPlayer) {
        player.setWalkSpeed(0.2f);
        player.sendMessage(ChatColor.of("#CE9FC9") + "Test roz : " + player.getWalkSpeed());
    }
}
