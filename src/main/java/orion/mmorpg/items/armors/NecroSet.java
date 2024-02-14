package orion.mmorpg.items.armors;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.Collections;

public class NecroSet {
    public static class NecroRing extends BasicArmorItem {
        public NecroRing() {
            super(Material.BLUE_DYE, "Anneau Nécrologique", "NECRO_RING", 3,
                    Collections.singletonList("Un Anneau fait de divers matériaux pourris"), Rarity.RARE,
                    ArmorType.RING, new Stats.Modifiers.Builder().add(Stats.StatsInfo.INTELLIGENCE, 10)
                            .add(Stats.StatsInfo.VITALITY, 7).build());
        }
    }

    public static class NecroBelt extends BasicArmorItem {
        public NecroBelt() {
            super(Material.MAGENTA_DYE, "Ceinture Nécrotique", "NECRO_BELT", 2,
                    Collections.singletonList("Une ceinture pourrie mais pratique"), Rarity.RARE, ArmorType.BELT,
                    new Stats.Modifiers.Builder().add(Stats.StatsInfo.VITALITY, 15).add(Stats.StatsInfo.DEFENCE, 5)
                            .add(Stats.StatsInfo.SPEED, 2).build());
        }
    }
}
