package orion.mmorpg.items.weapons;

import orion.mmorpg.config.AssociationConfig;
import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicMagicWeapon;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.Collections;

public class BahTon extends BasicMagicWeapon {
    public BahTon() {
        super(Material.IRON_SHOVEL, "BahTon", "BAHTON", AssociationConfig.getModel("weapon/staff/brillant_staff"), Collections.singletonList("Un baton simple avec peu de magie"), Rarity.COMMON, new Stats.Modifiers.Builder().add(Stats.StatsInfo.INTELLIGENCE, 2).build(), 5);
    }
}
