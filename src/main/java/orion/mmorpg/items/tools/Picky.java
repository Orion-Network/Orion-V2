package orion.mmorpg.items.tools;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicTool;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.List;

public class Picky extends BasicTool {

    public Picky() {
        super(Material.IRON_PICKAXE, "Picky the good Pick", "PICKY", 0, null, Rarity.HERO, new Stats.Modifiers(), ToolType.PICKAXE, 9);
    }
}
