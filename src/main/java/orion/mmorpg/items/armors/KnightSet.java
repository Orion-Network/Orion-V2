package orion.mmorpg.items.armors;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.items.sample.BasicMeleeWeapon;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.Collections;

public class KnightSet {
    public static class KnightHelmet extends BasicArmorItem {
        public KnightHelmet() {
            super(Material.IRON_HELMET, "Casque de Chevalier", "KNIGHT_HELMET", 1, null, Rarity.UNCOMMON, ArmorType.HELMET, new Stats.Modifiers.Builder().add(Stats.StatsInfo.VITALITY, 3).add(Stats.StatsInfo.DEFENCE, 4).build());
        }
    }

    public static class KnightChestplate extends BasicArmorItem {
        public KnightChestplate() {
            super(Material.IRON_CHESTPLATE, "Plastron de Chevalier", "KNIGHT_HELMET", 1, null, Rarity.UNCOMMON, ArmorType.CHESTPLATE, new Stats.Modifiers.Builder().add(Stats.StatsInfo.VITALITY, 5).add(Stats.StatsInfo.DEFENCE, 5).build());
        }
    }

    public static class KnightLeggings extends BasicArmorItem {
        public KnightLeggings() {
            super(Material.IRON_LEGGINGS, "Pantalon de Chevalier", "KNIGHT_LEGGINGS", 1, null, Rarity.UNCOMMON, ArmorType.LEGGINGS, new Stats.Modifiers.Builder().add(Stats.StatsInfo.VITALITY, 7).add(Stats.StatsInfo.DEFENCE, 5).build());
        }
    }

    public static class KnightBoots extends BasicArmorItem {
        public KnightBoots() {
            super(Material.IRON_BOOTS, "Bottes de Chevalier", "KNIGHT_BOOTS", 1, null, Rarity.UNCOMMON, ArmorType.BOOTS, new Stats.Modifiers.Builder().add(Stats.StatsInfo.VITALITY, 7).add(Stats.StatsInfo.DEFENCE, 3).build());
        }
    }

    public static class KnightSword extends BasicMeleeWeapon {
        public KnightSword() {
            super(Material.IRON_SWORD, "Epee de Chevalier", "KNIGHT_SWORD", 2, Collections.singletonList("Une épée assez robuste mais lourde"), Rarity.UNCOMMON, new Stats.Modifiers.Builder().add(Stats.StatsInfo.STRENGTH, 3).add(Stats.StatsInfo.DEFENCE, 5).build(), 7);
        }
    }

}
