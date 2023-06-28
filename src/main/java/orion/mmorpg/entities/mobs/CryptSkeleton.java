package orion.mmorpg.entities.mobs;

import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import orion.mmorpg.entities.samples.AbstractHostile;
import orion.mmorpg.utils.Stats;

public class CryptSkeleton extends AbstractHostile {

    public CryptSkeleton(String uuid, Location spawnLocation) {
        super(EntityTypes.aB, uuid, "Squelette de la Crypt", spawnLocation,  new Stats.Builder().withVitality(20).withStrength(8).withSpeed(6).build(), 7);
    }
}
