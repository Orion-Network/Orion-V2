package orion.mmorpg.items.sample;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.ItemReforge;
import orion.mmorpg.items.ItemType;
import orion.mmorpg.items.Modifiers;
import orion.mmorpg.items.Reforgeable;
import orion.mmorpg.utils.Namespaces;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.List;

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
        return super.generateLore(lore);
    }

    @Override
    public String generateDisplayName() {
        return getRARITY().getChatColor() + reforge.getTitle() + " " + NAME;
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
    /*
    Reforge seem to Work only on BasicEquipement not Tools
    And lore is glitchy item lore show good modifiers but menu duplicate the addition of the reforge
    Test if damage are affected in duplicate or not
     */
}
