package orion.mmorpg.entities.samples;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import orion.mmorpg.utils.Stats;

public abstract class AbstractModeledMob extends AbstractMob {
    String modelName;
    ActiveModel model;
    ModeledEntity modeledEntity;
    public AbstractModeledMob(String modelName, String uuid, String name, Location spawnLocation, Stats stats, int level) {
        super(EntityTypes.be, uuid, name, spawnLocation, stats, level);
        this.modelName = modelName;
    }

    @Override
    public void spawn() {
        super.spawn();
        model = ModelEngineAPI.createActiveModel(modelName);
        modeledEntity = ModelEngineAPI.createModeledEntity(this.getBukkitEntity());
        modeledEntity.addModel(model, true);
        modeledEntity.setBaseEntityVisible(false);
    }

    public ModeledEntity getModeledEntity() {
        return modeledEntity;
    }
}
