package orion.mmorpg.items.misc.reforge;

import org.bukkit.Material;
import orion.mmorpg.items.ItemReforge;
import orion.mmorpg.items.sample.BasicReforgeStone;
import orion.mmorpg.utils.Rarity;

import java.util.Arrays;

public class TastyStone extends BasicReforgeStone {
    public TastyStone() {
        super(Material.NETHERITE_INGOT, "Pierre appétissante", "TASTY_STONE", 0, Arrays.asList("Permet d'appliquer appétissante"), Rarity.RARE, ItemReforge.TASTY_STONE, 5);
    }

}
