package orion.mmorpg.items.ressources;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.utils.Rarity;

public class BronzeIngot extends BasicItem {
    public BronzeIngot() {
        super(Material.IRON_INGOT, "Bronze", "BRONZE_INGOT", 0, null, Rarity.COMMON);
    }
}
