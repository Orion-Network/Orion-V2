package orion.mmorpg.items.armors;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.Collections;

public class FurSet {
    public static class FurHelmet extends BasicArmorItem {

        public FurHelmet() {
            super(Material.LEATHER_HELMET, "Casque en Fourrure", "FUR_HELMET", 1,
                    Collections.singletonList("Un casque robuste fait en peau de loup"), Rarity.UNCOMMON,
                    ArmorType.HELMET, new Stats.Modifiers.Builder().add(Stats.StatsInfo.DEFENCE, 7)
                            .add(Stats.StatsInfo.VITALITY, 5).add(Stats.StatsInfo.INTELLIGENCE, 3).build());
        }
    }

    public static class FurChestplate extends BasicArmorItem {
        public FurChestplate() {
            super(Material.LEATHER_CHESTPLATE, "Plastron en Fourrure", "FUR_CHESTPLATE", 1,
                    Collections.singletonList("Un plastron robuste fait en peau de loup"), Rarity.UNCOMMON,
                    ArmorType.CHESTPLATE, new Stats.Modifiers.Builder().add(Stats.StatsInfo.DEFENCE, 9)
                            .add(Stats.StatsInfo.VITALITY, 7).add(Stats.StatsInfo.INTELLIGENCE, 4).build());
        }
    }

    public static class FurLeggings extends BasicArmorItem {
        public FurLeggings() {
            super(Material.LEATHER_LEGGINGS, "Jambieres en Fourrure", "FUR_LEGGINGS", 1,
                    Collections.singletonList("Des jambieres robustes fait en peau de loup"), Rarity.UNCOMMON,
                    ArmorType.LEGGINGS, new Stats.Modifiers.Builder().add(Stats.StatsInfo.DEFENCE, 7)
                            .add(Stats.StatsInfo.VITALITY, 6).add(Stats.StatsInfo.INTELLIGENCE, 1).build());
        }
    }

    public static class FurBoots extends BasicArmorItem {
        public FurBoots() {
            super(Material.LEATHER_BOOTS, "Bottes en Fourrure", "FUR_BOOTS", 1,
                    Collections.singletonList("Des bottes robustes fait en peau de loup"), Rarity.UNCOMMON,
                    ArmorType.BOOTS, new Stats.Modifiers.Builder().add(Stats.StatsInfo.DEFENCE, 4)
                            .add(Stats.StatsInfo.VITALITY, 3).add(Stats.StatsInfo.SPEED, 5).build());
        }
    }
}
