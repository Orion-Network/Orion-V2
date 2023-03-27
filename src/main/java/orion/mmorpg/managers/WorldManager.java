package orion.mmorpg.managers;

import org.bukkit.World;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.entities.samples.AbstractMob;

import java.util.LinkedHashMap;

public class WorldManager extends AbstractManager {
    private LinkedHashMap<World, AbstractMob> bosses;

    public WorldManager(Mmorpg mmorpg) {
        super(mmorpg);
        bosses = new LinkedHashMap<>();
    }

    public void spawn() {

    }

    public void addBoss(World world, AbstractMob mob) {
        bosses.put(world, mob);
    }

    public AbstractMob getBoss(World world) {
        return bosses.get(world);
    }
}
