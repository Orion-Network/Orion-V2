package orion.mmorpg.items.gui;

import orion.mmorpg.config.AssociationConfig;
import orion.mmorpg.items.sample.BasicGuiItem;

public class RightButtonPlaceholder extends BasicGuiItem {
    public RightButtonPlaceholder() {
        super("Right", "RIGHT_BUTTON_PLACEHOLDER", AssociationConfig.getModel("gui/right_button_placeholder"));
    }
}
