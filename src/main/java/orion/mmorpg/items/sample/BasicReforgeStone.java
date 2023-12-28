package orion.mmorpg.items.sample;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.items.ExcludeRegister;
import orion.mmorpg.items.ItemReforge;
import orion.mmorpg.utils.Namespaces;
import orion.mmorpg.utils.Rarity;

import java.util.ArrayList;
import java.util.List;

@ExcludeRegister
public class BasicReforgeStone extends BasicItem {
    @Getter
    ItemReforge reforge;
    @Getter
    int smithingLevelRequired;
    public BasicReforgeStone(ItemStack item) {
        super(item);
        this.reforge = ItemReforge.valueOf(item.getItemMeta().getPersistentDataContainer().get(Namespaces.reforge, PersistentDataType.STRING));
        this.smithingLevelRequired = item.getItemMeta().getPersistentDataContainer().get(Namespaces.smithingLevelRequired, PersistentDataType.INTEGER);
    }

    public BasicReforgeStone(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, List<String> LORE, Rarity RARITY, ItemReforge reforge, int smithingLevelRequired) {
        super(MATERIAL, NAME, INTERNAL_ID, MODEL_ID, LORE, RARITY);
        this.reforge = reforge;
        this.smithingLevelRequired = smithingLevelRequired;
    }

    @Override
    public List<String> generateLore(List<String> lore) {
        if(lore == null)
            lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW + "Smithing Level Required: §e" + ChatColor.GRAY + getSmithingLevelRequired());
        return super.generateLore(lore);
    }

    @Override
    public ItemStack generateItem(ItemStack item, int amount) {
        if (item == null)
            item = new ItemStack(MATERIAL);
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(Namespaces.reforge, PersistentDataType.STRING, reforge.name());
        meta.getPersistentDataContainer().set(Namespaces.smithingLevelRequired, PersistentDataType.INTEGER, getSmithingLevelRequired());
        item.setItemMeta(meta);
        return super.generateItem(item, amount);
    }
}
