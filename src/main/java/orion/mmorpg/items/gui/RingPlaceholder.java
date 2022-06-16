package orion.mmorpg.items.gui;

import orion.mmorpg.config.AssociationConfig;
import orion.mmorpg.items.sample.BasicGuiItem;

public class RingPlaceholder extends BasicGuiItem {
    public RingPlaceholder() {
        super("Ring Placeholder", "RING_PLACEHOLDER", AssociationConfig.getModel("gui/ring_placeholder"));
    }
}
