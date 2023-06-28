package orion.mmorpg.entities.mobs;

import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import orion.mmorpg.entities.samples.AbstractPassive;
import orion.mmorpg.utils.Stats;

public class CalmSheep extends AbstractPassive {
    public CalmSheep(String uuid, Location spawnLocation) {
        super(EntityTypes.ax, uuid, "Mouton Calme", spawnLocation, new Stats.Builder().withVitality(20).withSpeed(4).build(), 1);
    }
}
