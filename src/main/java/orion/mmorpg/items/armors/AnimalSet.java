package orion.mmorpg.items.armors;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

public class AnimalSet {
    public static class ChickenRing extends BasicArmorItem {
        public ChickenRing() {
            super(Material.BLUE_DYE, "Anneau de Poulet", "CHICKEN_RING", 3, null, Rarity.RARE, ArmorType.RING,
                    new Stats.Modifiers.Builder().multiply(Stats.StatsInfo.SPEED, 10).add(Stats.StatsInfo.STRENGTH, 1)
                            .build());
        }
    }

    public static class ChickenBelt extends BasicArmorItem {

        public ChickenBelt() {
            super(Material.BLUE_DYE, "Ceinture de Poulet", "CHICKEN_BELT", 3, null, Rarity.RARE, ArmorType.BELT,
                    new Stats.Modifiers.Builder().add(Stats.StatsInfo.LUCK, 5).add(Stats.StatsInfo.CRITICAL_CHANCE, 3)
                            .build());
        }
    }

    public static class SheepChestplate extends BasicArmorItem {
        public SheepChestplate() {
            super(Material.LEATHER_CHESTPLATE, "Plastron du Mouton", "SHEEP_CHESTPLATE", 0, null, Rarity.RARE,
                    ArmorType.CHESTPLATE, new Stats.Modifiers.Builder().add(Stats.StatsInfo.VITALITY, 7)
                            .add(Stats.StatsInfo.DEFENCE, 4).build());
        }
    }

    public static class SheepLeggings extends BasicArmorItem {
        public SheepLeggings() {
            super(Material.LEATHER_LEGGINGS, "Jambières du Mouton", "SHEEP_LEGGINGS", 0, null, Rarity.RARE,
                    ArmorType.LEGGINGS, new Stats.Modifiers.Builder().add(Stats.StatsInfo.VITALITY, 6)
                            .add(Stats.StatsInfo.DEFENCE, 2).build());
        }
    }

    public static class SheepBoots extends BasicArmorItem {
        public SheepBoots() {
            super(Material.LEATHER_BOOTS, "Bottes du Mouton", "SHEEP_BOOTS", 0, null, Rarity.RARE, ArmorType.BOOTS,
                    new Stats.Modifiers.Builder().add(Stats.StatsInfo.VITALITY, 3).add(Stats.StatsInfo.DEFENCE, 1)
                            .add(Stats.StatsInfo.SPEED, 5).build());
        }
    }
}
