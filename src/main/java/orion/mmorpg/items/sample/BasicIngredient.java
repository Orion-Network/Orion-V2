package orion.mmorpg.items.sample;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.items.ExcludeRegister;
import orion.mmorpg.items.ItemType;
import orion.mmorpg.players.abilities.OEffect;
import orion.mmorpg.utils.Namespaces;
import orion.mmorpg.utils.Rarity;

import java.util.ArrayList;
import java.util.List;

@Getter
@ExcludeRegister
public class BasicIngredient extends BasicRessource {
    Class<? extends OEffect> oEffect;
    int duration;
    int experience;

    public BasicIngredient(ItemStack item) {
        super(item);
        try {
            this.oEffect = (Class<? extends OEffect>) Class.forName(
                    item.getItemMeta().getPersistentDataContainer().get(Namespaces.oeffect, PersistentDataType.STRING));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public BasicIngredient(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, List<String> LORE,
            Rarity RARITY, Class<? extends OEffect> effect) {
        super(MATERIAL, NAME, INTERNAL_ID, MODEL_ID, LORE, RARITY, ItemType.INGREDIENT);
        this.oEffect = effect;
    }

    @Override
    public ItemStack generateItem(ItemStack item, int amount) {
        if (item == null)
            item = new ItemStack(MATERIAL);
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(Namespaces.oeffect, PersistentDataType.STRING, oEffect.getName());
        item.setItemMeta(meta);
        return super.generateItem(item, amount);
    }

    @Override
    public List<String> generateLore(List<String> lore) {
        if (lore == null)
            lore = new ArrayList<>();
        lore.add(ChatColor.WHITE + "Effet: " + oEffect.getSimpleName());
        return super.generateLore(lore);
    }
}
