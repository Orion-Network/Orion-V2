package orion.mmorpg.items.gui;

import orion.mmorpg.config.AssociationConfig;
import orion.mmorpg.items.sample.BasicGuiItem;

public class UpButtonPlaceholder extends BasicGuiItem {

    public UpButtonPlaceholder() {
        super("Up", "UP_BUTTON_PLACEHOLDER", AssociationConfig.getModel("gui/up_button_placeholder"));
    }
}
