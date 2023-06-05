package orion.mmorpg.entities.mobs;

import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import orion.mmorpg.entities.samples.AbstractHostile;
import orion.mmorpg.utils.Stats;

public class AngryChicken extends AbstractHostile {

    public AngryChicken(String uuid, Location spawnLocation) {
        super(EntityTypes.l, uuid, "Poulet énervé", spawnLocation, new Stats.Builder().setBase(Stats.StatsInfo.VITALITY, 20).setBase(Stats.StatsInfo.STRENGTH, 4).setBase(Stats.StatsInfo.SPEED, 4).build(), 3);
    }
}
