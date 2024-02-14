package orion.mmorpg.items.gui;

import orion.mmorpg.config.AssociationConfig;
import orion.mmorpg.items.sample.BasicGuiItem;

public class ChestplatePlaceholderItem extends BasicGuiItem {

    public ChestplatePlaceholderItem() {
        super("Chestplate", "CHESTPLATE_PLACEHOLDER", AssociationConfig.getModel("gui/chestplate_placeholder"));
    }
}
