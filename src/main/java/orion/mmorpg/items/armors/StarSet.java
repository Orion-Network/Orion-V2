package orion.mmorpg.items.armors;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

public class StarSet {
    public static class StarsetHelmet extends BasicArmorItem {

        public StarsetHelmet() {
            super(Material.FLOWERING_AZALEA, "Starset Helmet", "STARSET_HELMET", 0, "Don't", Rarity.UNOBTAINABLE, ArmorType.HELMET,
                    new Stats.Builder().build());
        }

    }

    public static class StarsetChestplate extends BasicArmorItem {
        public StarsetChestplate() {
            super(Material.NETHER_STAR, "Starset Heart", "STARSET_CHESTPLATE", 0, "It's", Rarity.UNOBTAINABLE, ArmorType.CHESTPLATE,
                    new Stats.Builder().withAttack_speed(300).withStrength(999).withCritical_chance(300).withCritical_damage(700).build());
        }
    }

    public static class StarsetLeggings extends BasicArmorItem {
        public StarsetLeggings() {
            super(Material.LEVER, "Starset Leggings", "STARSET_LEGGINGS", 0, "for", Rarity.UNOBTAINABLE, ArmorType.LEGGINGS,
                    new Stats.Builder().withIntel(100).withLuck(100).withVitality(100).build());
        }
    }

    public static class StarsetBoots extends BasicArmorItem {
        public StarsetBoots() {
            super(Material.MINECART, "Starset Skateboard", "STARSET_BOOTS", 0, "Admins", Rarity.UNOBTAINABLE, ArmorType.BOOTS,
                    new Stats.Builder().withSpeed(Stats.StatsInfo.SPEED.getCap()).build());
        }
    }
}
