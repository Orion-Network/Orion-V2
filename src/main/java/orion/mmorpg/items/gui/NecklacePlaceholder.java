package orion.mmorpg.items.gui;

import orion.mmorpg.config.AssociationConfig;
import orion.mmorpg.items.sample.BasicGuiItem;

public class NecklacePlaceholder extends BasicGuiItem {
    public NecklacePlaceholder() {
        super("Necklace Placeholder", "NECKLACE_PLACEHOLDER", AssociationConfig.getModel("gui/necklace_placeholder"));
    }
}
