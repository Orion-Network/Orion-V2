package orion.mmorpg.items.gui;

import orion.mmorpg.config.AssociationConfig;
import orion.mmorpg.items.sample.BasicGuiItem;

public class AcceptButtonPlaceholder extends BasicGuiItem {
    public AcceptButtonPlaceholder() {
        super("Accepter", "ACCEPT_BUTTON_PLACEHOLDER", AssociationConfig.getModel("gui/accept_button_placeholder"));
    }
}
