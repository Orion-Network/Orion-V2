package orion.mmorpg.entities.mounts;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import orion.mmorpg.entities.samples.AbstractMount;
import orion.mmorpg.entities.samples.Modeled;
import orion.mmorpg.utils.Stats;

public class Chief extends AbstractMount implements Modeled {
    public Chief(String uuid, Location spawnLocation, Player player) {
        super(EntityTypes.bc, uuid, "TestWolf", spawnLocation, new Stats.Builder().withSpeed(4).build(), player, MountType.LAND);
    }


    ActiveModel model;
    ModeledEntity modeledEntity;
    @Override
    public ActiveModel getModel() {
        if (model == null) {
            model = ModelEngineAPI.createActiveModel("chief");
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
