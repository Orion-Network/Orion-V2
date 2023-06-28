package orion.mmorpg.items.weapons;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicRangedWeapon;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.Collections;

public class ReinforcedBow extends BasicRangedWeapon {
    public ReinforcedBow() {
        super(Material.BOW, "Arc Renforcé", "REINFORCED_BOW", 2, Collections.singletonList("Un arc un peu plus solide que la moyenne"), Rarity.RARE, new Stats.Modifiers.Builder().add(Stats.StatsInfo.STRENGTH, 4).add(Stats.StatsInfo.SPEED, 3).build(), 5);
    }
}
