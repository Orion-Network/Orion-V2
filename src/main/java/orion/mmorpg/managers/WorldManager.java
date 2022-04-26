package orion.mmorpg.managers;

import org.bukkit.World;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.mobs.samples.SampleMob;

import java.util.LinkedHashMap;

public class WorldManager extends AbstractManager {
    private LinkedHashMap<World, SampleMob> bosses;

    public WorldManager(Mmorpg mmorpg) {
        super(mmorpg);
        bosses = new LinkedHashMap<>();
    }

    public void spawn() {

    }

    public void addBoss(World world, SampleMob mob) {
        bosses.put(world, mob);
    }

    public SampleMob getBoss(World world) {
        return bosses.get(world);
    }
}
