package orion.mmorpg.items.misc.reforge;

import org.bukkit.Material;
import orion.mmorpg.items.ItemReforge;
import orion.mmorpg.items.sample.BasicReforgeStone;
import orion.mmorpg.utils.Rarity;

import java.util.Arrays;

public class StrongStone extends BasicReforgeStone {
    public StrongStone() {
        super(Material.NETHERITE_INGOT, "Pierre forte", "STRONG_STONE", 0, Arrays.asList("Permet d'appliquer forte"),
                Rarity.UNCOMMON, ItemReforge.STRONG, 5);
    }
}
