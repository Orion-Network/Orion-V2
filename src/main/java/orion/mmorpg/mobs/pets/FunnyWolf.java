package orion.mmorpg.mobs.pets;

import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import orion.mmorpg.mobs.samples.SamplePetMob;

public class FunnyWolf extends SamplePetMob {
    public FunnyWolf(String uuid, Location location) {
        super("Lil wolf", EntityTypes.bc, uuid, location);
    }
}
