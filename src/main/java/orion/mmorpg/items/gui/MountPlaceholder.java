package orion.mmorpg.items.gui;

import org.bukkit.ChatColor;
import orion.mmorpg.config.AssociationConfig;
import orion.mmorpg.items.sample.BasicGuiItem;

import java.util.Collections;
import java.util.List;

public class MountPlaceholder extends BasicGuiItem {
    public MountPlaceholder() {
        super("Mount Placeholder", "MOUNT_PLACEHOLDER", AssociationConfig.getModel("gui/mount_placeholder"));
    }

    @Override
    public List<String> generateLore(List<String> lore) {
        return List.of(ChatColor.YELLOW + "Cliquez pour voir vos montures");
    }
}
