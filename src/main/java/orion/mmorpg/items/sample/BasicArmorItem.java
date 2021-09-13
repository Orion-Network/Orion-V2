package orion.mmorpg.items.sample;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.items.ItemType;
import orion.mmorpg.utils.OrionGson;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicArmorItem extends BasicItem {
    public final ArmorType ARMORTYPE;
    public final Stats STATS;

    public BasicArmorItem(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, String LORE, Rarity RARITY, ArmorType ARMORTYPE, Stats STATS) {
        super(MATERIAL, NAME, INTERNAL_ID, MODEL_ID, LORE, RARITY, ItemType.ARMOR);
        this.ARMORTYPE = ARMORTYPE;
        this.STATS = STATS;
    }

    @Override
    public ItemStack generateItem(ItemStack item, int amount) {
        if(item == null)
            item = new ItemStack(MATERIAL);
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(NamespacedKey.minecraft("stats"), PersistentDataType.STRING, OrionGson.GSON.toJson(STATS));
        meta.getPersistentDataContainer().set(NamespacedKey.minecraft("armor_type"), PersistentDataType.STRING, ARMORTYPE.name());
        item.setItemMeta(meta);
        return super.generateItem(item, amount);
    }

    @Override
    public List<String> generateLore(List<String> lore) {
        if(lore == null)
            lore = new ArrayList<>();
        lore.addAll(STATS.statsAsLore());

        return super.generateLore(lore);
    }

    public enum ArmorType {
        HELMET, CHESTPLATE, LEGGINGS, BOOTS;
    }
}
