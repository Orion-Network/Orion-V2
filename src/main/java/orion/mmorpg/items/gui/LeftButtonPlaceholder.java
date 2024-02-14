package orion.mmorpg.items.gui;

import orion.mmorpg.config.AssociationConfig;
import orion.mmorpg.items.sample.BasicGuiItem;

public class LeftButtonPlaceholder extends BasicGuiItem {

    public LeftButtonPlaceholder() {
        super("Left", "LEFT_BUTTON_PLACEHOLDER", AssociationConfig.getModel("gui/left_button_placeholder"));
    }
}
