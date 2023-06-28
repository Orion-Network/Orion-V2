package orion.mmorpg.items.armors;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.Collections;

public class StarSet {
    public static class StarsetHelmet extends BasicArmorItem {

        public StarsetHelmet() {
            super(Material.FLOWERING_AZALEA, "Starset Helmet", "STARSET_HELMET", 0, Collections.singletonList("Don't"), Rarity.UNOBTAINABLE, ArmorType.HELMET,
                    new Stats.Modifiers());
        }

    }

    public static class StarsetChestplate extends BasicArmorItem {
        public StarsetChestplate() {
            super(Material.NETHER_STAR, "Starset Heart", "STARSET_CHESTPLATE", 0, Collections.singletonList("It's"), Rarity.UNOBTAINABLE, ArmorType.CHESTPLATE,
                    new Stats.Modifiers.Builder().add(Stats.StatsInfo.ATTACK_SPEED, 300).add(Stats.StatsInfo.STRENGTH, 999).add(Stats.StatsInfo.CRITICAL_CHANCE, 300).add(Stats.StatsInfo.CRITICAL_DAMAGE, 700).build());
        }
    }

    public static class StarsetLeggings extends BasicArmorItem {
        public StarsetLeggings() {
            super(Material.LEVER, "Starset Leggings", "STARSET_LEGGINGS", 0, Collections.singletonList("for"), Rarity.UNOBTAINABLE, ArmorType.LEGGINGS,
                    new Stats.Modifiers.Builder().add(Stats.StatsInfo.DEFENCE, 100).add(Stats.StatsInfo.INTELLIGENCE, 100).add(Stats.StatsInfo.LUCK, 100).add(Stats.StatsInfo.VITALITY, 100).build());
        }
    }

    public static class StarsetBoots extends BasicArmorItem {
        public StarsetBoots() {
            super(Material.MINECART, "Starset Skateboard", "STARSET_BOOTS", 0, Collections.singletonList("Admins"), Rarity.UNOBTAINABLE, ArmorType.BOOTS,
                    new Stats.Modifiers.Builder().add(Stats.StatsInfo.SPEED, Stats.StatsInfo.SPEED.getCap()).build());
        }
    }
}
