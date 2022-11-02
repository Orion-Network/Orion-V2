package orion.mmorpg.mobs.pets;

import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.EntityPufferFish;
import org.bukkit.Location;
import orion.mmorpg.mobs.samples.SamplePetMob;

public class FunFunGu extends SamplePetMob {
    public FunFunGu(String uuid, Location location) {
        super("FunFunGu", EntityTypes.at, uuid, location);
    }


}
