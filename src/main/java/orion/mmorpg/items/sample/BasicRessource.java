package orion.mmorpg.items.sample;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.items.ExcludeRegister;
import orion.mmorpg.items.ItemType;
import orion.mmorpg.utils.Rarity;

import java.util.ArrayList;
import java.util.List;

@ExcludeRegister
public class BasicRessource extends BasicItem {
    public BasicRessource(ItemStack item) {
        super(item);
    }

    public BasicRessource(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, List<String> LORE,
            Rarity RARITY) {
        super(MATERIAL, NAME, INTERNAL_ID, MODEL_ID, LORE, RARITY, ItemType.RESSOURCE);
    }

    public BasicRessource(Material material, String name, String internalId, int modelId, List<String> lore,
            Rarity rarity, ItemType itemType) {
        super(material, name, internalId, modelId, lore, rarity, itemType);
    }

    @Override
    public List<String> generateLore(List<String> lore) {
        if (lore == null)
            lore = new ArrayList<>();
        return super.generateLore(lore);
    }
}
