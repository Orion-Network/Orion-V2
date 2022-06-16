package orion.mmorpg.items.gui;

import orion.mmorpg.config.AssociationConfig;
import orion.mmorpg.items.sample.BasicGuiItem;

public class DownButtonPlaceholder extends BasicGuiItem {

    public DownButtonPlaceholder() {
        super("Down", "UP_BUTTON_PLACEHOLDER",  AssociationConfig.getModel("gui/down_button_placeholder"));
    }
}
