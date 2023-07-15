package orion.mmorpg.entities.samples;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import net.minecraft.world.entity.EntityCreature;
import org.bukkit.Bukkit;

public interface Modeled {
    ActiveModel getModel();
    ModeledEntity getModeledEntity();

    default void spawnModel() {
        getModeledEntity().addModel(getModel(), true);
        getModeledEntity().setBaseEntityVisible(false);
    }
}
