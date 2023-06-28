package orion.mmorpg.items.armors;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

public class AnimalSet {
    public static class ChickenRing extends BasicArmorItem {
        public ChickenRing() {
            super(Material.BLUE_DYE, "Anneau de Poulet", "CHICKEN_RING", 3, null, Rarity.RARE, ArmorType.RING,
                    new Stats.Modifiers.Builder()
                            .multiply(Stats.StatsInfo.SPEED, 10)
                            .add(Stats.StatsInfo.STRENGTH, 1)
                            .build());
        }
    }
}
