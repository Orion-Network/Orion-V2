package orion.mmorpg.items.sample;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.ItemReforge;
import orion.mmorpg.items.ItemType;
import orion.mmorpg.utils.Namespaces;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.List;

public class BasicEquipmentItem extends BasicItem {
    private final Stats BASE_STATS;
    private ItemReforge reforge = ItemReforge.NONE;

    public BasicEquipmentItem(ItemStack item) {
        super(item);
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        this.BASE_STATS = Mmorpg.getOrionGson().getGSON().fromJson(container.get(Namespaces.base_stats, PersistentDataType.STRING), Stats.class);
        this.reforge = ItemReforge.valueOf(container.get(Namespaces.reforge, PersistentDataType.STRING));
    }

    public BasicEquipmentItem(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, List<String> LORE, Rarity RARITY, ItemType TYPE, Stats stats) {
        super(MATERIAL, NAME, INTERNAL_ID, MODEL_ID, LORE, RARITY, TYPE);
        BASE_STATS = stats;
    }

    public BasicEquipmentItem(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, List<String> LORE, Rarity RARITY, ItemType TYPE, String PERMISSION, Stats stats) {
        super(MATERIAL, NAME, INTERNAL_ID, MODEL_ID, LORE, RARITY, TYPE, PERMISSION);
        BASE_STATS = stats;
    }

    @Override
    public ItemStack generateItem(ItemStack item, int amount) {
        if (item == null)
            item = new ItemStack(MATERIAL);
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(Namespaces.base_stats, PersistentDataType.STRING, Mmorpg.getOrionGson().getGSON().toJson(getBASE_STATS()));
        meta.getPersistentDataContainer().set(Namespaces.reforge, PersistentDataType.STRING, reforge.name());
        meta.getPersistentDataContainer().set(Namespaces.stats, PersistentDataType.STRING, Mmorpg.getOrionGson().getGSON().toJson(getStats()));
        item.setItemMeta(meta);
        return super.generateItem(item, amount);
    }

    @Override
    public String generateDisplayName() {
        return getRARITY().getChatColor() + reforge.getTitle() + " " + NAME;
    }

    public Stats getBASE_STATS() {
        return BASE_STATS;
    }

    public Stats getStats() {
        return reforge.getFunction().apply(getBASE_STATS());
    }

    public ItemReforge getReforge() {
        return reforge;
    }

    public void setReforge(ItemReforge reforge) {
        this.reforge = reforge;
    }


}
