package orion.mmorpg.mobs.mounts;

import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import orion.mmorpg.mobs.samples.SampleMountMob;
import orion.mmorpg.utils.Stats;

public class FlyingFish extends SampleMountMob {
    public FlyingFish(String uuid, Location location) {
        super("FlyingFish", 0, EntityTypes.aS, uuid, location, new Stats.Builder().withSpeed(4).build(), MountType.AIR);
        this.setForwardMultiplier(2);
        this.setStrafeMultiplier(1.7);
    }
}
