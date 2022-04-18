package orion.mmorpg.mobs.list;

import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import orion.mmorpg.mobs.samples.SampleHostileMob;
import orion.mmorpg.utils.Stats;

public class MountainZombie extends SampleHostileMob {
    public MountainZombie(String uuid, Location location) {
        super("Zombie Montagnard", 5, EntityTypes.be, uuid, location, new Stats.Builder().withVitality(40).withStrength(7).withDefence(2).withSpeed(4).build());
    }
}
