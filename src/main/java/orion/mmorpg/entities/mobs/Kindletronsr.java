package orion.mmorpg.entities.mobs;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import orion.mmorpg.entities.samples.AbstractHostile;
import orion.mmorpg.entities.samples.Modeled;
import orion.mmorpg.utils.Stats;

public class Kindletronsr extends AbstractHostile implements Modeled {

    public Kindletronsr(String uuid, Location spawnLocation) {
        super(EntityTypes.bc, uuid, "Kindletronsr", spawnLocation, new Stats.Builder().withVitality(30).withStrength(2).withSpeed(4).build(), 2);
    }

    ActiveModel model;
    ModeledEntity modeledEntity;
    @Override
    public ActiveModel getModel() {
        if (model == null) {
            model = ModelEngineAPI.createActiveModel("kindletronsr");
        }
        return model;
    }

    @Override
    public ModeledEntity getModeledEntity() {
        if (modeledEntity == null) {
            modeledEntity = ModelEngineAPI.createModeledEntity(this.getBukkitEntity());
        }
        return modeledEntity;
    }
}
