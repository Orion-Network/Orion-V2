package orion.mmorpg.items.gui;

import orion.mmorpg.config.AssociationConfig;
import org.bukkit.ChatColor;
import orion.mmorpg.items.sample.BasicGuiItem;

import java.util.LinkedList;
import java.util.List;

public class UsablePlaceholder extends BasicGuiItem {
    public UsablePlaceholder() {
        super("Slot Raccourcis", "USABLE_PLACEHOLDER", AssociationConfig.getModel("gui/usable_placeholder"));
    }

    @Override
    public List<String> generateLore(List<String> lore) {
        if(lore == null)
            lore = new LinkedList<>();
        lore.add(ChatColor.GRAY + "Cliquez pour rendre ce slot libre");
        return super.generateLore(lore);
    }
}
