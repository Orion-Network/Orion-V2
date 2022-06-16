package orion.mmorpg.items.gui;

import orion.mmorpg.config.AssociationConfig;
import orion.mmorpg.items.sample.BasicGuiItem;

public class BootsPlaceholder extends BasicGuiItem {

    public BootsPlaceholder() {
        super("Boots", "BOOTS_PLACEHOLDER",  AssociationConfig.getModel("gui/boots_placeholder"));
    }
}
