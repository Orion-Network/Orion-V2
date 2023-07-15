package orion.mmorpg.entities.mobs;

import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import orion.mmorpg.entities.samples.AbstractNeutral;
import orion.mmorpg.utils.Stats;

public class Sheep extends AbstractNeutral {

    public Sheep(String uuid, Location spawnLocation) {
        super(EntityTypes.ax, uuid, "Mouton", spawnLocation, new Stats.Builder().setBase(Stats.StatsInfo.VITALITY, 30).setBase(Stats.StatsInfo.STRENGTH, 2).setBase(Stats.StatsInfo.SPEED, 4).build(), 2);
    }
}
