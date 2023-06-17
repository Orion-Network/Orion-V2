package orion.mmorpg.items.sample;

import lombok.Getter;
import org.bukkit.ChatColor;
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
import orion.mmorpg.items.tools.ToolType;
import orion.mmorpg.utils.Namespaces;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.ArrayList;
import java.util.List;

public class BasicTool extends BasicEquipmentItem {
    @Getter
    ToolType toolType;
    @Getter
    int toolPower;

    public BasicTool(ItemStack item) {
        super(item);

        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        this.toolType = ToolType.valueOf(container.get(Namespaces.tool_type, PersistentDataType.STRING));
        this.toolPower = container.get(Namespaces.tool_power, PersistentDataType.INTEGER);
    }

    public BasicTool(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, List<String> LORE, Rarity RARITY, Stats.Modifiers modifiers, ToolType toolType, int toolPower) {
        super(MATERIAL, NAME, INTERNAL_ID, MODEL_ID, LORE, RARITY,
                ItemType.TOOL, modifiers);
        this.toolType = toolType;
        this.toolPower = toolPower;
    }

    @Override
    public ItemStack generateItem(ItemStack item, int amount) {

        if (item == null)
            item = new ItemStack(MATERIAL);
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(Namespaces.tool_type, PersistentDataType.STRING, toolType.name());
        meta.getPersistentDataContainer().set(Namespaces.tool_power, PersistentDataType.INTEGER, toolPower);
        meta.getPersistentDataContainer().set(Namespaces.modifiers, PersistentDataType.STRING, Mmorpg.getOrionGson().getGSON().toJson(getModifiers()));
        item.setItemMeta(meta);

        return super.generateItem(item, amount);
    }

    @Override
    public List<String> generateLore(List<String> lore) {
        if (lore == null)
            lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Puissance : " + ChatColor.RED + toolPower);
        lore.add(ChatColor.GRAY + "Type : " + ChatColor.RED + toolType.name());
        lore.addAll(getModifiersWithReforge().getAsLore());

        return super.generateLore(lore);
    }

}
