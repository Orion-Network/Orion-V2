package orion.mmorpg.entities.mobs;

import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import orion.mmorpg.entities.samples.AbstractHostile;
import orion.mmorpg.utils.Stats;

public class MountainZombie extends AbstractHostile {
    public MountainZombie(String uuid, Location spawnLocation) {
        super(EntityTypes.be, uuid, "Zombie Montagnard", spawnLocation,  new Stats.Builder().withVitality(40).withStrength(7).withDefence(2).withSpeed(4).build(), 5);
    }
}
