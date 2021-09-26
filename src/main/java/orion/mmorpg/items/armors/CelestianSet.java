package orion.mmorpg.items.armors;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

public class CelestianSet {
    public static class CelestianWings extends BasicArmorItem {
        public CelestianWings() {
            super(Material.ELYTRA, "Ailes Célestes", "CELESTIAN_WINGS", 0, null, Rarity.COMMON, ArmorType.WINGS,
                    new Stats(0,0,0,0,5,0,2,0,0));
        }
    }
}
