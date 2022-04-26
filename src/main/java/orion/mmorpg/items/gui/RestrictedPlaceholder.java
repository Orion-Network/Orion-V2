package orion.mmorpg.items.gui;

import org.bukkit.ChatColor;
import orion.mmorpg.items.sample.BasicGuiItem;

import java.util.LinkedList;
import java.util.List;

public class RestrictedPlaceholder extends BasicGuiItem {
    public RestrictedPlaceholder() {
        super("Slot Restraint", "RESTRICTED_PLACEHOLDER", 14);
    }

    @Override
    public List<String> generateLore(List<String> lore) {
        if(lore == null)
            lore = new LinkedList<>();
        lore.add(ChatColor.GRAY + "Slot bloqué");
        return super.generateLore(lore);
    }
}
