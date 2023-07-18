package orion.mmorpg.items.misc;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.CompassMeta;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Rarity;

import java.util.Collections;

public class EternalCompass extends BasicItem {
    private static Location location = Mmorpg.INSTANCE.getWorldManager().getPortalStone("CAMPEMENT").getLocation();
    public EternalCompass() {
        super(Material.COMPASS, "Eternal Compass", "ETERNAL_COMPASS", 1, Collections.singletonList("Un compas magique"), Rarity.RARE);
    }

    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        CompassMeta meta = (CompassMeta) e.getItem().getItemMeta();
        meta.setLodestone(location);
        meta.setLodestoneTracked(false);
        e.getItem().setItemMeta(meta);
    }
}
