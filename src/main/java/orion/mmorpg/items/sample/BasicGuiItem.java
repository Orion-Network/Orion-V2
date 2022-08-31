package orion.mmorpg.items.sample;

import org.bukkit.Material;
import orion.mmorpg.items.ItemType;
import orion.mmorpg.utils.Rarity;

import java.util.List;

public class BasicGuiItem extends BasicItem {
    public BasicGuiItem(String NAME, String INTERNAL_ID, int MODEL_ID) {
        super(Material.CARROT_ON_A_STICK, NAME, INTERNAL_ID, MODEL_ID, null, Rarity.TRASH, ItemType.PLACEHOLDER, "item.display");
    }

    public BasicGuiItem(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID) {
        super(MATERIAL, NAME, INTERNAL_ID, MODEL_ID, null, Rarity.TRASH, ItemType.PLACEHOLDER, "item.display");
    }

    @Override
    public List<String> generateLore(List<String> lore) {
        return null;
    }
}
