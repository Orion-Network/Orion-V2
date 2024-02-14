package orion.mmorpg.items.misc;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Rarity;

import java.util.Collections;

public class InfiniteFirework extends BasicItem {
    public InfiniteFirework() {
        super(Material.FIREWORK_ROCKET, "InfiniWork", "INFINITE_FIREWORK", 0,
                Collections.singletonList("A rare firework given for christmas"), Rarity.LEGENDARY);
    }

    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        super.onUseItem(mmorpg, data, e);
    }
}
