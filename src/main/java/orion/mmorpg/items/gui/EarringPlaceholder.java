package orion.mmorpg.items.gui;

import orion.mmorpg.config.AssociationConfig;
import orion.mmorpg.items.sample.BasicGuiItem;

public class EarringPlaceholder extends BasicGuiItem {
    public EarringPlaceholder() {
        super("Earring Placeholder", "EARRING_PLACEHOLDER", AssociationConfig.getModel("gui/earring_placeholder"));
    }
}
