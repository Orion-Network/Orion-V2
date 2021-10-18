package orion.mmorpg.players.abilities.list;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.utils.Element;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class TestSpell extends Spell {
    public TestSpell() {
        super("TEST_SPELL", 0, Element.ARCANE, 1000);
    }

    @Override
    public void launch(Mmorpg mmorpg, Player player, OrionPlayer orionPlayer) {
        if(player.isSneaking()) {
            mmorpg.getMongoClient().savePlayer(orionPlayer);
            Utils.sendInfoMessage(player, "Data saved !");
        } else {
            try {
                mmorpg.getMongoClient().loadPlayer(player.getUniqueId());
                Utils.sendInfoMessage(player, "Data loaded !");
            } catch (IOException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                Utils.sendErrorMessage(player, "Error in loading");
            }
        }

    }
}
