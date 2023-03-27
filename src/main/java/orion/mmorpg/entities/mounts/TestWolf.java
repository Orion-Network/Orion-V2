package orion.mmorpg.entities.mounts;

import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import orion.mmorpg.entities.samples.AbstractMount;
import orion.mmorpg.utils.Stats;

public class TestWolf extends AbstractMount {

    public TestWolf(String uuid,Location spawnLocation, Player player) {
        super(EntityTypes.bc, uuid, "TestWolf", spawnLocation, new Stats.Builder().withSpeed(4).build(), player, MountType.LAND);
    }
}
