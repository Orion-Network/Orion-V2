package orion.mmorpg.items.tools;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicTool;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

public class WoodenRod extends BasicTool {
    public WoodenRod() {
        super(Material.FISHING_ROD, "Wooden Rod", "WOODEN_ROD", 0, null, Rarity.COMMON, new Stats.Modifiers.Builder()
                .add(Stats.StatsInfo.FISHING_SPEED, 10).add(Stats.StatsInfo.SEA_CREATURE_CHANCE, 5).build(),
                ToolType.FISHING_ROD, 1);
    }
}
