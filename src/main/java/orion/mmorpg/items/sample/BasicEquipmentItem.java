package orion.mmorpg.items.sample;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.*;
import orion.mmorpg.items.enchants.CustomEnchants;
import orion.mmorpg.utils.Namespaces;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;
import orion.mmorpg.utils.Utils;

import java.util.List;

@ExcludeRegister
public class BasicEquipmentItem extends BasicItem implements Modifiers, Reforgeable {
    private final Stats.Modifiers MODIFIERS;
    private ItemReforge reforge = ItemReforge.NONE;

    public BasicEquipmentItem(ItemStack item) {
        super(item);
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        this.MODIFIERS = Mmorpg.getOrionGson().getGSON().fromJson(container.get(Namespaces.modifiers, PersistentDataType.STRING), Stats.Modifiers.class);
        setReforge(ItemReforge.valueOf(container.get(Namespaces.reforge, PersistentDataType.STRING)));
    }

    public BasicEquipmentItem(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, List<String> LORE, Rarity RARITY, ItemType TYPE, Stats.Modifiers modifiers) {
        super(MATERIAL, NAME, INTERNAL_ID, MODEL_ID, LORE, RARITY, TYPE);
        this.MODIFIERS = modifiers;
    }

    public BasicEquipmentItem(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, List<String> LORE, Rarity RARITY, ItemType TYPE, String PERMISSION, Stats.Modifiers modifiers) {
        super(MATERIAL, NAME, INTERNAL_ID, MODEL_ID, LORE, RARITY, TYPE, PERMISSION);
        this.MODIFIERS = modifiers;
    }

    @Override
    public ItemStack generateItem(ItemStack item, int amount) {
        if (item == null)
            item = new ItemStack(MATERIAL);
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(Namespaces.reforge, PersistentDataType.STRING, reforge.name());
        meta.getPersistentDataContainer().set(Namespaces.modifiers, PersistentDataType.STRING, Mmorpg.getOrionGson().getGSON().toJson(getModifiers()));
        item.setItemMeta(meta);
        return super.generateItem(item, amount);
    }

    @Override
    public List<String> generateLore(List<String> lore) {
        List<String> lorea =  super.generateLore(lore);
        //Generate enchants with comma separated values
        if(this.hasItemMeta()) {
            StringBuilder enchants = new StringBuilder();
            for(Enchantment enchantment : getItemMeta().getEnchants().keySet()) {
                enchants.append(ChatColor.GRAY + enchantment.getName()).append(" ").append(Utils.integerToRoman5(getItemMeta().getEnchants().get(enchantment))).append(", ");
            }
            if(enchants.length() > 0) {
                enchants.delete(enchants.length() - 2, enchants.length());
                lorea.add(enchants.toString());
            }
        }

        return lorea;
    }

    @Override
    public String generateDisplayName() {
        return getRARITY().getChatColor() + (reforge == ItemReforge.NONE ? "" : reforge.getTitle() + " ") + NAME;
    }

    public ItemReforge getReforge() {
        return reforge;
    }

    public void setReforge(ItemReforge reforge) {
        this.reforge = reforge;
    }

    public Stats.Modifiers getModifiers() {
        return MODIFIERS;
    }

    public Stats.Modifiers getModifiersWithReforge() {
        return reforge.getFunction().apply(MODIFIERS);
    }
}
