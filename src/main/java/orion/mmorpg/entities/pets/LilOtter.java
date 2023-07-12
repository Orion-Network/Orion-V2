package orion.mmorpg.entities.pets;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import orion.mmorpg.entities.Pets;
import orion.mmorpg.entities.samples.AbstractPet;
import orion.mmorpg.entities.samples.Modeled;

public class LilOtter extends AbstractPet implements Modeled {
    public LilOtter(String uuid, Location spawnLocation, Player owner) {
        super(EntityType.AXOLOTL, uuid, "Lil' Otter", spawnLocation, owner, Pets.LILOTTER);
    }

    ActiveModel model;
    ModeledEntity modeledEntity;
    @Override
    public ActiveModel getModel() {
        if (model == null) {
            model = ModelEngineAPI.createActiveModel("otter");
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
