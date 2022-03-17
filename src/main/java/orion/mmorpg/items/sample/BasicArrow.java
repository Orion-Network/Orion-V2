package orion.mmorpg.items.sample;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.utils.Rarity;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicArrow extends BasicItem {
    public final float DAMAGE;

    public BasicArrow(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, String LORE, Rarity RARITY, float DAMAGE) {
        super(MATERIAL, NAME, INTERNAL_ID, MODEL_ID, LORE, RARITY);
        this.DAMAGE = DAMAGE;
    }

    @Override
    public ItemStack generateItem(ItemStack item, int amount) {
        if (item == null)
            item = new ItemStack(MATERIAL);
        if (this.spell != null) {
            ItemMeta meta = item.getItemMeta();
            meta.getPersistentDataContainer().set(NamespacedKey.minecraft("spell"), PersistentDataType.STRING, spell.getInternal_id());
            meta.getPersistentDataContainer().set(NamespacedKey.minecraft("damage"), PersistentDataType.FLOAT, DAMAGE);
            item.setItemMeta(meta);
        }
        return super.generateItem(item, amount);
    }

    @Override
    public List<String> generateLore(List<String> lore) {
        if (lore == null)
            lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Damage: " + ChatColor.RED + DAMAGE);
        return super.generateLore(lore);
    }
}
