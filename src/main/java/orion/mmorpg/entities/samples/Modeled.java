package orion.mmorpg.entities.samples;

import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;

public interface Modeled {
    ActiveModel getModel();
    ModeledEntity getModeledEntity();

    default void spawnModel() {
        getModeledEntity().addModel(getModel(), true);
        getModeledEntity().setBaseEntityVisible(false);
    }
}
