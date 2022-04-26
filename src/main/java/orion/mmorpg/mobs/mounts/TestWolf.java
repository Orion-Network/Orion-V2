package orion.mmorpg.mobs.mounts;

import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import orion.mmorpg.mobs.samples.SampleMountMob;
import orion.mmorpg.utils.Stats;

public class TestWolf extends SampleMountMob {
    public TestWolf(String uuid, Location location) {
        super("TestWolf", 0, EntityTypes.bc, uuid, location, new Stats.Builder().withSpeed(4).build());
    }

    @Override
    protected void generatePathfinder() {

    }
}
