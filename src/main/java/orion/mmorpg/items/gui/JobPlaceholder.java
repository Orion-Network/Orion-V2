package orion.mmorpg.items.gui;

import orion.mmorpg.config.AssociationConfig;
import orion.mmorpg.items.sample.BasicGuiItem;

public class JobPlaceholder extends BasicGuiItem {
    public JobPlaceholder() {
        super("Menu des métiers", "JOB_PLACEHOLDER", AssociationConfig.getModel("gui/job_placeholder"));
    }
}
