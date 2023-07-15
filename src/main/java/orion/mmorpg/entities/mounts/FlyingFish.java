package orion.mmorpg.entities.mounts;

import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import orion.mmorpg.entities.samples.AbstractMount;
import orion.mmorpg.utils.Stats;

public class FlyingFish extends AbstractMount {

    public FlyingFish(String uuid, Location spawnLocation, Player player) {
        super(EntityType.TROPICAL_FISH, uuid, "FlyingFish", spawnLocation, new Stats.Builder().setBase(Stats.StatsInfo.SPEED, 4).build(), player, MountType.AIR);

        this.setForwardMultiplier(2);
        this.setStrafeMultiplier(1.7);
    }
}
