package orion.mmorpg.items.gui;

import orion.mmorpg.config.AssociationConfig;
import orion.mmorpg.items.sample.BasicGuiItem;

public class SettingsPlaceholder extends BasicGuiItem {
    public SettingsPlaceholder() {
        super("Settings", "SETTINGS_PLACEHOLDER", AssociationConfig.getModel("gui/settings_placeholder"));
    }
}
