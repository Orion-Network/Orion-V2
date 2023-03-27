package orion.mmorpg.entities.mounts;

import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import orion.mmorpg.entities.samples.AbstractMount;
import orion.mmorpg.utils.Stats;

public class FlyingFish extends AbstractMount {

    public FlyingFish(String uuid, Location spawnLocation, Player player) {
        super(EntityTypes.aS, uuid, "FlyingFish", spawnLocation, new Stats.Builder().withSpeed(4).build(), player, MountType.AIR);

        this.setForwardMultiplier(2);
        this.setStrafeMultiplier(1.7);
    }
}
