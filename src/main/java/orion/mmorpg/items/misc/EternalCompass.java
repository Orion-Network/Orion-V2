package orion.mmorpg.items.misc;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.CompassMeta;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.PortalStone;
import orion.mmorpg.utils.Rarity;

public class EternalCompass extends BasicItem {
    private static Location location = PortalStone.SPAWN_STONE.getLocation();
    public EternalCompass() {
        super(Material.COMPASS, "Eternal Compass", "ETERNAL_COMPASS", 1, "Un compas magique", Rarity.RARE);
    }

    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        CompassMeta meta = (CompassMeta) e.getItem().getItemMeta();
        meta.setLodestone(location);
        meta.setLodestoneTracked(false);
        e.getItem().setItemMeta(meta);
    }
}
