package orion.mmorpg.items.sample;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.items.ItemReforge;
import orion.mmorpg.utils.Namespaces;
import orion.mmorpg.utils.Rarity;

import java.util.List;

public class BasicReforgeStone extends BasicItem {
    ItemReforge reforge;
    int smithingLevelRequired;
    public BasicReforgeStone(ItemStack item) {
        super(item);
        this.reforge = ItemReforge.valueOf(item.getItemMeta().getPersistentDataContainer().get(Namespaces.reforge, PersistentDataType.STRING));
    }

    public BasicReforgeStone(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, List<String> LORE, Rarity RARITY, ItemReforge reforge, int smithingLevelRequired) {
        super(MATERIAL, NAME, INTERNAL_ID, MODEL_ID, LORE, RARITY);
        this.reforge = reforge;
        this.smithingLevelRequired = smithingLevelRequired;
    }

    @Override
    public ItemStack generateItem(ItemStack item, int amount) {
        if (item == null)
            item = new ItemStack(MATERIAL);
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(Namespaces.reforge, PersistentDataType.STRING, reforge.name());
        item.setItemMeta(meta);
        return super.generateItem(item, amount);
    }

    public ItemReforge getReforge() {
        return reforge;
    }
}
