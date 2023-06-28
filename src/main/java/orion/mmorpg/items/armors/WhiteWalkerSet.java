package orion.mmorpg.items.armors;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.Collections;

public class WhiteWalkerSet {
    public static class WhiteWalkerHelmet extends BasicArmorItem {

        public WhiteWalkerHelmet() {
            super(Material.DIAMOND_HELMET, "Casque de Marcheur Blanc", "WHITE_WALKER_HELMET", 1, Collections.singletonList("Un casque solide dégageant une sombre aura"), Rarity.SUPERRARE, ArmorType.HELMET,
                    new Stats.Builder().withDefence(12).withVitality(12).withIntel(3).build());
        }
    }

    public static class WhiteWalkerChestplate extends BasicArmorItem {
        public WhiteWalkerChestplate() {
            super(Material.DIAMOND_CHESTPLATE, "Plastron de Marcheur Blanc", "WHITE_WALKER_CHESTPLATE", 1, Collections.singletonList("Un plastron solide dégageant une sombre aura"), Rarity.SUPERRARE, ArmorType.CHESTPLATE,
                    new Stats.Builder().withDefence(14).withVitality(11).withIntel(4).build());
        }
    }

    public static class WhiteWalkerLeggings extends BasicArmorItem {
        public WhiteWalkerLeggings() {
            super(Material.DIAMOND_LEGGINGS, "Jambières de Marcheur Blanc", "WHITE_WALKER_LEGGINGS", 1, Collections.singletonList("De jambières solides dégageant une sombre aura"), Rarity.SUPERRARE, ArmorType.LEGGINGS,
                    new Stats.Builder().withDefence(12).withVitality(11).withIntel(1).build());
        }
    }

    public static class WhiteWalkerBoots extends BasicArmorItem {
        public WhiteWalkerBoots() {
            super(Material.DIAMOND_BOOTS, "Bottes de Marcheur Blanc", "WHITE_WALKER_BOOTS", 1, Collections.singletonList("De bottes solides dégageant une sombre aura"), Rarity.SUPERRARE, ArmorType.BOOTS,
                    new Stats.Builder().withDefence(9).withVitality(10).withSpeed(7).build());
        }
    }

    public static class WhiteWalkerRing extends BasicArmorItem {
        public WhiteWalkerRing() {
            super(Material.BLUE_DYE, "Anneau de Marcheur Blanc", "WHITE_WALKER_RING", 2, Collections.singletonList("Un anneau solide dégageant une sombre aura"), Rarity.SUPERRARE, ArmorType.RING,
                    new Stats.Builder().withIntel(11).withVitality(10).withStrength(7).build());
        }
    }

    public static class WhiteWalkerBelt extends BasicArmorItem {
        public WhiteWalkerBelt() {
            super(Material.MAGENTA_DYE, "Ceinture de Marcheur Blanc", "WHITE_WALKER_BELT", 2, Collections.singletonList("Une ceinture solide dégageant une sombre aura"), Rarity.SUPERRARE, ArmorType.BELT,
                    new Stats.Builder().withIntel(3).withVitality(17).withDefence(6).withSpeed(5).build());
        }
    }
}
