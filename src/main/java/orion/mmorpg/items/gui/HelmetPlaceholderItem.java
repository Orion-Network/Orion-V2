package orion.mmorpg.items.gui;

import orion.mmorpg.config.AssociationConfig;
import orion.mmorpg.items.sample.BasicGuiItem;

public class HelmetPlaceholderItem extends BasicGuiItem {

    public HelmetPlaceholderItem() {
        super("Helmet", "HELMET_PLACEHOLDER", AssociationConfig.getModel("gui/helmet_placeholder"));
    }
}
