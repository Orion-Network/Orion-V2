package orion.mmorpg.items.tools;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicTool;
import orion.mmorpg.utils.Rarity;

public class WoodenRod extends BasicTool {
    public WoodenRod() {
        super(Material.FISHING_ROD, "Wooden Rod", "WOODEN_ROD", 0, null, Rarity.COMMON, ToolType.FISHING_ROD, 1);
    }
}
