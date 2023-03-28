package orion.mmorpg.items.gui;

import org.bukkit.ChatColor;
import orion.mmorpg.config.AssociationConfig;
import orion.mmorpg.items.sample.BasicGuiItem;

import java.util.List;

public class PetPlaceholder extends BasicGuiItem {
    public PetPlaceholder() {
        super("Pet Placeholder", "PET_PLACEHOLDER", AssociationConfig.getModel("gui/pet_placeholder"));
    }

    @Override
    public List<String> generateLore(List<String> lore) {
        return List.of(ChatColor.YELLOW + "Cliquez pour voir vos familiers");
    }
}
