package orion.mmorpg.items.tools;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.items.sample.BasicTool;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

public class MinerPickaxe extends BasicTool {
    public MinerPickaxe() {
        super(Material.IRON_PICKAXE, "Pioche du mineur", "MINER_PICKAXE", 0, null, Rarity.UNCOMMON, ToolType.PICKAXE, 1, new Stats.Modifiers());
    }
}
