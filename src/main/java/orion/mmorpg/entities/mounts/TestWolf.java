package orion.mmorpg.entities.mounts;

import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import orion.mmorpg.entities.samples.AbstractMount;
import orion.mmorpg.utils.Stats;

public class TestWolf extends AbstractMount {

    public TestWolf(String uuid,Location spawnLocation, Player player) {
        super(EntityType.WOLF, uuid, "TestWolf", spawnLocation, new Stats.Builder().setBase(Stats.StatsInfo.SPEED, 4).build(), player, MountType.LAND);
    }
}
