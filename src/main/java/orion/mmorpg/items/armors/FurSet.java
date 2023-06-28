package orion.mmorpg.items.armors;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.Collections;

public class FurSet {
    public static class FurHelmet extends BasicArmorItem {

        public FurHelmet() {
            super(Material.LEATHER_HELMET, "Casque en Fourrure", "FUR_HELMET", 1, Collections.singletonList("Un casque robuste fait en peau de loup"), Rarity.UNCOMMON, ArmorType.HELMET,
                    new Stats.Builder().withDefence(7).withVitality(5).withIntel(3).build());
        }
    }

    public static class FurChestplate extends BasicArmorItem {
        public FurChestplate() {
            super(Material.LEATHER_CHESTPLATE, "Plastron en Fourrure", "FUR_CHESTPLATE", 1, Collections.singletonList("Un plastron robuste fait en peau de loup"), Rarity.UNCOMMON, ArmorType.CHESTPLATE,
                    new Stats.Builder().withDefence(9).withVitality(7).withIntel(4).build());
        }
    }

    public static class FurLeggings extends BasicArmorItem {
        public FurLeggings() {
            super(Material.LEATHER_LEGGINGS, "Jambieres en Fourrure", "FUR_LEGGINGS", 1, Collections.singletonList("Des jambieres robustes fait en peau de loup"), Rarity.UNCOMMON, ArmorType.LEGGINGS,
                    new Stats.Builder().withDefence(7).withVitality(6).withIntel(1).build());
        }
    }

    public static class FurBoots extends BasicArmorItem {
        public FurBoots() {
            super(Material.LEATHER_BOOTS, "Bottes en Fourrure", "FUR_BOOTS", 1, Collections.singletonList("Des bottes robustes fait en peau de loup"), Rarity.UNCOMMON, ArmorType.BOOTS,
                    new Stats.Builder().withDefence(4).withVitality(3).withSpeed(5).build());
        }
    }
}
