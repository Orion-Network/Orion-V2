package orion.mmorpg.items.gui;

import orion.mmorpg.config.AssociationConfig;
import orion.mmorpg.items.sample.BasicGuiItem;

public class RefuseButtonPlaceholder extends BasicGuiItem {
    public RefuseButtonPlaceholder() {
        super("Refuser", "REFUSE_BUTTON_PLACEHOLDER",  AssociationConfig.getModel("gui/refuse_button_placeholder"));
    }
}
