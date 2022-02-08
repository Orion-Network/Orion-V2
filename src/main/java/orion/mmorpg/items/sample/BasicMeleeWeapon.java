package orion.mmorpg.items.sample;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.ItemType;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicMeleeWeapon extends BasicItem{
    public final Stats STATS;
    public final float DAMAGE;

    public BasicMeleeWeapon(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, String LORE, Rarity RARITY, String PERMISSION, Stats STATS, float DAMAGE) {
        super(MATERIAL, NAME, INTERNAL_ID, MODEL_ID, LORE, RARITY, ItemType.MELEE_WEAPON, PERMISSION);
        this.STATS = STATS;
        this.DAMAGE = DAMAGE;
    }

    protected BasicMeleeWeapon(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, String LORE, Rarity RARITY , Stats STATS, float DAMAGE) {
        super(MATERIAL, NAME, INTERNAL_ID, MODEL_ID, LORE, RARITY, ItemType.MELEE_WEAPON);
        this.STATS = STATS;
        this.DAMAGE = DAMAGE;
    }

    @Override
    public ItemStack generateItem(ItemStack item, int amount) {
        if(item == null)
            item = new ItemStack(MATERIAL);
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(NamespacedKey.minecraft("stats"), PersistentDataType.STRING, Mmorpg.getOrionGson().getGSON().toJson(STATS));
        meta.getPersistentDataContainer().set(NamespacedKey.minecraft("damage"), PersistentDataType.FLOAT, DAMAGE);
        item.setItemMeta(meta);
        return super.generateItem(item, amount);
    }

    @Override
    public List<String> generateLore(List<String> lore) {
        if(lore == null)
            lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Damage: " + ChatColor.RED + DAMAGE);
        lore.addAll(STATS.statsAsLore());

        return super.generateLore(lore);
    }

    public Stats getSTATS() {
        return STATS;
    }

    public float getDAMAGE() {
        return DAMAGE;
    }
}
