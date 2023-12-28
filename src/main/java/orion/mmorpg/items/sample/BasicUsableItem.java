package orion.mmorpg.items.sample;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.items.ExcludeRegister;
import orion.mmorpg.utils.Rarity;

import java.util.List;

@ExcludeRegister
public class BasicUsableItem extends BasicItem {

    public BasicUsableItem(ItemStack item) {
        super(item);
    }

    public BasicUsableItem(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, List<String> LORE, Rarity RARITY) {
        super(MATERIAL, NAME, INTERNAL_ID, MODEL_ID, LORE, RARITY);
    }
}
