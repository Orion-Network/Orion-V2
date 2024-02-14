package orion.mmorpg.items.armors;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.Collections;

public class WhiteWalkerSet {
    public static class WhiteWalkerHelmet extends BasicArmorItem {

        public WhiteWalkerHelmet() {
            super(Material.DIAMOND_HELMET, "Casque de Marcheur Blanc", "WHITE_WALKER_HELMET", 1,
                    Collections.singletonList("Un casque solide dégageant une sombre aura"), Rarity.SUPERRARE,
                    ArmorType.HELMET, new Stats.Modifiers.Builder().add(Stats.StatsInfo.DEFENCE, 12)
                            .add(Stats.StatsInfo.VITALITY, 12).add(Stats.StatsInfo.INTELLIGENCE, 3).build());
        }
    }

    public static class WhiteWalkerChestplate extends BasicArmorItem {
        public WhiteWalkerChestplate() {
            super(Material.DIAMOND_CHESTPLATE, "Plastron de Marcheur Blanc", "WHITE_WALKER_CHESTPLATE", 1,
                    Collections.singletonList("Un plastron solide dégageant une sombre aura"), Rarity.SUPERRARE,
                    ArmorType.CHESTPLATE, new Stats.Modifiers.Builder().add(Stats.StatsInfo.DEFENCE, 14)
                            .add(Stats.StatsInfo.VITALITY, 11).add(Stats.StatsInfo.INTELLIGENCE, 4).build());
        }
    }

    public static class WhiteWalkerLeggings extends BasicArmorItem {
        public WhiteWalkerLeggings() {
            super(Material.DIAMOND_LEGGINGS, "Jambières de Marcheur Blanc", "WHITE_WALKER_LEGGINGS", 1,
                    Collections.singletonList("De jambières solides dégageant une sombre aura"), Rarity.SUPERRARE,
                    ArmorType.LEGGINGS, new Stats.Modifiers.Builder().add(Stats.StatsInfo.DEFENCE, 12)
                            .add(Stats.StatsInfo.VITALITY, 11).add(Stats.StatsInfo.INTELLIGENCE, 1).build());
        }
    }

    public static class WhiteWalkerBoots extends BasicArmorItem {
        public WhiteWalkerBoots() {
            super(Material.DIAMOND_BOOTS, "Bottes de Marcheur Blanc", "WHITE_WALKER_BOOTS", 1,
                    Collections.singletonList("De bottes solides dégageant une sombre aura"), Rarity.SUPERRARE,
                    ArmorType.BOOTS, new Stats.Modifiers.Builder().add(Stats.StatsInfo.DEFENCE, 12)
                            .add(Stats.StatsInfo.VITALITY, 12).add(Stats.StatsInfo.SPEED, 3).build());
        }
    }

    public static class WhiteWalkerRing extends BasicArmorItem {
        public WhiteWalkerRing() {
            super(Material.BLUE_DYE, "Anneau de Marcheur Blanc", "WHITE_WALKER_RING", 2,
                    Collections.singletonList("Un anneau solide dégageant une sombre aura"), Rarity.SUPERRARE,
                    ArmorType.RING, new Stats.Modifiers.Builder().add(Stats.StatsInfo.DEFENCE, 11)
                            .add(Stats.StatsInfo.VITALITY, 10).add(Stats.StatsInfo.STRENGTH, 7).build());
        }
    }

    public static class WhiteWalkerBelt extends BasicArmorItem {
        public WhiteWalkerBelt() {
            super(Material.MAGENTA_DYE, "Ceinture de Marcheur Blanc", "WHITE_WALKER_BELT", 2,
                    Collections.singletonList("Une ceinture solide dégageant une sombre aura"), Rarity.SUPERRARE,
                    ArmorType.BELT, new Stats.Modifiers.Builder().add(Stats.StatsInfo.DEFENCE, 17)
                            .add(Stats.StatsInfo.VITALITY, 6).add(Stats.StatsInfo.SPEED, 5).build());
        }
    }
}
