package orion.mmorpg.items.armors;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

public class StartingSet {
    public static class StartingHelmet extends BasicArmorItem {
        public StartingHelmet(ItemStack item) {
            super(item);
        }

        public StartingHelmet() {
            super(Material.LEATHER_HELMET, "Starting Helmet", "STARTING_HELMET", 0, null, Rarity.COMMON, ArmorType.HELMET,
                    new Stats.Modifiers.Builder().add(Stats.StatsInfo.VITALITY, 3).add(Stats.StatsInfo.DEFENCE, 1).add(Stats.StatsInfo.INTELLIGENCE, 2).build());
        }
    }

    public static class StartingChestplate extends BasicArmorItem {
        public StartingChestplate() {
            super(Material.LEATHER_CHESTPLATE, "Starting Chestplate", "STARTING_CHESTPLATE", 0, null, Rarity.COMMON, ArmorType.CHESTPLATE,
                    new Stats.Modifiers.Builder().add(Stats.StatsInfo.VITALITY, 3).add(Stats.StatsInfo.DEFENCE, 2).build());
        }
    }

    public static class StartingLeggings extends BasicArmorItem {
        public StartingLeggings() {
            super(Material.LEATHER_LEGGINGS, "Starting Leggings", "STARTING_LEGGINGS", 0, null, Rarity.COMMON, ArmorType.LEGGINGS,
                    new Stats.Modifiers.Builder().add(Stats.StatsInfo.VITALITY, 4).add(Stats.StatsInfo.DEFENCE, 3).add(Stats.StatsInfo.SPEED, 2).build());
        }
    }

    public static class StartingBoots extends BasicArmorItem {
        public StartingBoots() {
            super(Material.LEATHER_BOOTS, "Starting Boots", "STARTING_BOOTS", 0, null, Rarity.COMMON, ArmorType.BOOTS,
                    new Stats.Modifiers.Builder().add(Stats.StatsInfo.VITALITY, 2).add(Stats.StatsInfo.DEFENCE, 1).add(Stats.StatsInfo.SPEED, 3).build());
        }
    }
}
