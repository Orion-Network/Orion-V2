package orion.mmorpg.items.sample;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.items.ExcludeRegister;
import orion.mmorpg.utils.Rarity;

import java.util.ArrayList;
import java.util.List;

@ExcludeRegister
public class BasicCurrency extends BasicItem {
    public BasicCurrency(ItemStack item) {
        super(item);
    }

    public BasicCurrency(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, List<String> LORE, Rarity RARITY) {
        super(MATERIAL, NAME, INTERNAL_ID, MODEL_ID, LORE, RARITY);
    }

    @Override
    public List<String> generateLore(List<String> lore) {
        if(lore == null)
            lore = new ArrayList<>();
        lore.add(ChatColor.DARK_GRAY + "Monnaie");
        return super.generateLore(lore);
    }
}
