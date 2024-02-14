package orion.mmorpg.items.gui;

import orion.mmorpg.config.AssociationConfig;
import org.bukkit.ChatColor;
import orion.mmorpg.items.sample.BasicGuiItem;

import java.util.LinkedList;
import java.util.List;

public class RestrictedPlaceholder extends BasicGuiItem {
    public RestrictedPlaceholder() {
        super("Slot Restraint", "RESTRICTED_PLACEHOLDER", AssociationConfig.getModel("gui/unusable_placeholder"));
    }

    @Override
    public List<String> generateLore(List<String> lore) {
        if (lore == null)
            lore = new LinkedList<>();
        lore.add(ChatColor.GRAY + "Slot bloqué");
        return super.generateLore(lore);
    }
}
