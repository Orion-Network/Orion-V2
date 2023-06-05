package orion.mmorpg.items.ressources;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.items.sample.BasicRessource;
import orion.mmorpg.utils.Rarity;

public class BronzeIngot extends BasicRessource {

    public BronzeIngot(ItemStack itemStack) {
        super(itemStack);
    }
    public BronzeIngot() {
        super(Material.COPPER_INGOT, "Bronze", "BRONZE_INGOT", 0, null, Rarity.COMMON);
    }
}
