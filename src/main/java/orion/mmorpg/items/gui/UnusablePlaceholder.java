package orion.mmorpg.items.gui;

import orion.mmorpg.config.AssociationConfig;
import org.bukkit.ChatColor;
import orion.mmorpg.items.sample.BasicGuiItem;

import java.util.LinkedList;
import java.util.List;

public class UnusablePlaceholder extends BasicGuiItem {
    public UnusablePlaceholder() {
        super("Slot Libre", "UNUSABLE_PLACEHOLDER", AssociationConfig.getModel("gui/unusable_placeholder"));
    }

    @Override
    public List<String> generateLore(List<String> lore) {
        if(lore == null)
            lore = new LinkedList<>();
        lore.add(ChatColor.GRAY + "Cliquez pour rendre ce slot en raccourcis");
        return super.generateLore(lore);
    }
}
