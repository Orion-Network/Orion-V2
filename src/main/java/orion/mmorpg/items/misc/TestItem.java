package orion.mmorpg.items.misc;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Utils;

public class TestItem extends BasicItem {
    Player p;

    public TestItem() {
        super(Material.NETHER_STAR, "Test Item", "TEST_ITEM", 0, null, Rarity.UNOBTAINABLE);
    }

    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        Utils.sendDebugMessage(e.getPlayer(), "Item used");
        super.onUseItem(mmorpg, data, e);
    }
}
