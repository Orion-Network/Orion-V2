package orion.mmorpg.items.armors;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

public class NecroSet {
    public static class NecroRing extends BasicArmorItem {
        public NecroRing() {
            super(Material.BLUE_DYE, "Anneau Nécrologique", "NECRO_RING", 3, "Un Anneau fait de divers matériaux pourris", Rarity.RARE, ArmorType.RING,
                    new Stats.Builder().withIntel(10).withVitality(7).build());
        }
    }

    public static class NecroBelt extends BasicArmorItem {
        public NecroBelt() {
            super(Material.MAGENTA_DYE, "Ceinture Nécrotique", "NECRO_BELT", 2, "Une ceinture pourrie mais pratique", Rarity.RARE, ArmorType.BELT,
                    new Stats.Builder().withVitality(15).withDefence(5).withSpeed(2).build());
        }
    }
}
