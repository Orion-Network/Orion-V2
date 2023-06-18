package orion.mmorpg.items.misc.reforge;

import org.bukkit.Material;
import orion.mmorpg.items.ItemReforge;
import orion.mmorpg.items.sample.BasicReforgeStone;
import orion.mmorpg.utils.Rarity;

import java.util.Arrays;

public class PolarizedStone extends BasicReforgeStone {
    public PolarizedStone() {
        super(Material.NETHERITE_INGOT, "Pierre polarisée", "POLARIZED_STONE", 0, Arrays.asList("Permet d'appliquer polarisée"), Rarity.UNCOMMON, ItemReforge.POLARIZER, 5);
    }
}
