package orion.mmorpg.items.gui;

import orion.mmorpg.config.AssociationConfig;
import orion.mmorpg.items.sample.BasicGuiItem;

public class BeltPlaceholder extends BasicGuiItem {
    public BeltPlaceholder() {
        super("Belt Placeholder", "BELT_PLACEHOLDER", AssociationConfig.getModel("gui/belt_placeholder"));
    }
}
