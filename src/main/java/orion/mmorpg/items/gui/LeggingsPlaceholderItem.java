package orion.mmorpg.items.gui;

import orion.mmorpg.config.AssociationConfig;
import orion.mmorpg.items.sample.BasicGuiItem;

public class LeggingsPlaceholderItem extends BasicGuiItem {

    public LeggingsPlaceholderItem() {
        super("Leggings", "LEGGINGS_PLACEHOLDER", AssociationConfig.getModel("gui/leggings_placeholder"));
    }
}
