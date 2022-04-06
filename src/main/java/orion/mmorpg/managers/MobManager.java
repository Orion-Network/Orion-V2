package orion.mmorpg.managers;

import net.minecraft.world.entity.EntityCreature;
import org.bukkit.Location;
import org.bukkit.event.entity.CreatureSpawnEvent;
import orion.mmorpg.mobs.list.AngryChicken;
import orion.mmorpg.players.PortalStone;

import java.util.HashMap;

public class MobManager<T extends EntityCreature> implements Runnable {
    HashMap<T, Long> entityMap = new HashMap<>();

    public MobManager() {
        AngryChicken chicken = new AngryChicken(PortalStone.ARENA_STONE.getLocation());

        chicken.getWorld().addEntity(chicken, CreatureSpawnEvent.SpawnReason.CUSTOM);
        this.entityMap.put((T) chicken, System.currentTimeMillis());
    }

    @Override
    public void run() {
        entityMap.forEach((entity, respawnTime) -> {
            System.out.println(entity.isAlive() + " " + entity.isRemoved());
            if(!entity.isRemoved()) return;
            System.out.println("Dead" + System.currentTimeMillis() + " " + respawnTime);
            if(respawnTime < System.currentTimeMillis()) {
                System.out.println("Spawn");
                //entity.getWorld().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
                entity.setHealth(entity.getMaxHealth());
                Location loc = PortalStone.ARENA_STONE.getLocation();
                entity.setPosition(loc.getX(), loc.getY(), loc.getZ());
                entity.unsetRemoved();
            }
        });
    }
}
