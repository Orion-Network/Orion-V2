package orion.mmorpg.mobs.list;

import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import orion.mmorpg.mobs.samples.SamplePassiveMob;
import orion.mmorpg.utils.Stats;

public class Vachette extends SamplePassiveMob {
    public Vachette(String uuid, Location location) {
        super("Vachette", 1, EntityTypes.ah, uuid, location, new Stats.Builder().withVitality(20).withSpeed(4).build());
    }
}
