package orion.mmorpg.items.tools;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicTool;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.List;

public class AdminRod extends BasicTool {

    public AdminRod() {
        super(Material.FISHING_ROD, "Admin Rod", "ADMIN_ROD", 0, null, Rarity.UNOBTAINABLE,
                new Stats.Modifiers.Builder().add(Stats.StatsInfo.FISHING_SPEED, 1000).add(Stats.StatsInfo.SEA_CREATURE_CHANCE, 100).build(), ToolType.FISHING_ROD, 10);

    }
}
