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
            super(Material.IRON_HELMET, "Casque de Chevalier", "KNIGHT_HELMET", 1, null, Rarity.UNCOMMON, ArmorType.HELMET, new Stats.Builder().withVitality(7).withDefence(5).build());
        }
    }

    public static class KnightChestplate extends BasicArmorItem {
        public KnightChestplate() {
            super(Material.IRON_CHESTPLATE, "Plastron de Chevalier", "KNIGHT_HELMET", 1, null, Rarity.UNCOMMON, ArmorType.CHESTPLATE, new Stats.Builder().withVitality(5).withDefence(5).build());
        }
    }

    public static class KnightLeggings extends BasicArmorItem {
        public KnightLeggings() {
            super(Material.IRON_LEGGINGS, "Pantalon de Chevalier", "KNIGHT_LEGGINGS", 1, null, Rarity.UNCOMMON, ArmorType.LEGGINGS, new Stats.Builder().withVitality(7).withDefence(5).build());
        }
    }

    public static class KnightBoots extends BasicArmorItem {
        public KnightBoots() {
            super(Material.IRON_BOOTS, "Bottes de Chevalier", "KNIGHT_BOOTS", 1, null, Rarity.UNCOMMON, ArmorType.BOOTS, new Stats.Builder().withVitality(7).withDefence(5).build());
        }
    }

    public static class KnightSword extends BasicMeleeWeapon {
        public KnightSword() {
            super(Material.IRON_SWORD, "Epee de Chevalier", "KNIGHT_SWORD", 2, Collections.singletonList("Une épée assez robuste mais lourde"), Rarity.UNCOMMON, new Stats.Builder().withStrength(3).build(), 7);
        }
    }

}
