package orion.mmorpg.entities.mobs;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import orion.mmorpg.entities.samples.AbstractHostile;
import orion.mmorpg.entities.samples.AbstractNeutral;
import orion.mmorpg.entities.samples.Modeled;
import orion.mmorpg.utils.Stats;

public class Scarecrow extends AbstractHostile implements Modeled {
    public Scarecrow(String uuid,Location spawnLocation) {
        super(EntityTypes.bc, uuid, "Scarecrow", spawnLocation, new Stats.Builder().setBase(Stats.StatsInfo.VITALITY, 30).setBase(Stats.StatsInfo.STRENGTH, 2).setBase(Stats.StatsInfo.SPEED, 4).build(), 2);
    }

    ActiveModel model;
    ModeledEntity modeledEntity;
    @Override
    public ActiveModel getModel() {
        if (model == null) {
            model = ModelEngineAPI.createActiveModel("scarecrow");
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
