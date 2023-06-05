package orion.mmorpg.items.money;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicCurrency;
import orion.mmorpg.utils.Rarity;

import java.util.List;

public class BronzeCoin extends BasicCurrency {
    public BronzeCoin() {
        super(Material.COPPER_INGOT, "Pièce de bronze", "BRONZE_COIN", 0, null, Rarity.COMMON);
    }
}
