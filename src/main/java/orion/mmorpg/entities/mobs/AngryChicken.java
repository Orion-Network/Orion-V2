package orion.mmorpg.entities.mobs;

import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import orion.mmorpg.entities.samples.AbstractHostile;
import orion.mmorpg.utils.Stats;

public class AngryChicken extends AbstractHostile {

    public AngryChicken(String uuid, Location spawnLocation) {
        super(EntityTypes.l, uuid, "Poulet énervé", spawnLocation, new Stats.Builder().withVitality(20).withStrength(4).withSpeed(4).build(), 3);
    }
}
