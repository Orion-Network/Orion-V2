package orion.mmorpg.managers;

import net.minecraft.world.entity.Entity;
import org.bukkit.Location;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.mobs.samples.SampleMob;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class MobManager<T extends SampleMob> extends AbstractManager implements Runnable {
    HashMap<String, T> mobs = new HashMap<>();
    HashMap<SampleMob.MobData, Long> mobList = new HashMap<>();

    public MobManager(Mmorpg mmorpg) {
        super(mmorpg);
    }


    @Override
    public void run() {
        mobList.forEach((mob, time) -> {
            if(time < System.currentTimeMillis()) {
                try {
                    SampleMob sampleMob = mob.generateMob();
                    spawnMob(sampleMob);
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        mobList.values().removeIf(time -> time < System.currentTimeMillis());
    }

    public void addMob(SampleMob.MobData mobData) {
        mobList.put(mobData, System.currentTimeMillis() + mobData.getSpawnTime());
    }

    public void loadMobs() {
        mmorpg.getMongoClient().getMobCollection().find().forEach((document) -> {
            try {
                Class<? extends SampleMob> clazz = (Class<? extends SampleMob>) Class.forName(document.getString("className"));
                Location location = Mmorpg.getOrionGson().getGSON().fromJson(document.getString("location"), Location.class);
                SampleMob sampleMob = (SampleMob) clazz.getDeclaredConstructors()[0].newInstance(document.getString("uuid"), location);
                spawnMob(sampleMob);

            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    public void despawnMobs() {
        for(T mob : mobs.values()) {
            mob.setRemoved(Entity.RemovalReason.a);
        }
    }

    public void spawnMob(SampleMob sampleMob) {
        mobs.put(sampleMob.getUuid(), (T) sampleMob);
        Location location = sampleMob.getSpawnLocation();
        if(location.getChunk().isLoaded()) {
            sampleMob.spawn();
            System.out.println("Spawned mob " + sampleMob.getName() + " at " + location.getX() + ", " + location.getY() + ", " + location.getZ() + "loaded" + location.getChunk().isLoaded());
        } else {
            if (mmorpg.getChunkManager().containChunk(location.getChunk())) {
                mmorpg.getChunkManager().getChunk(location.getChunk()).add(sampleMob.getMobData());
                System.out.println("Spawned mob " + sampleMob.getName() + "added to chunk");
            } else {
                Set<SampleMob.MobData> mobDataSet = new LinkedHashSet<>();
                mobDataSet.add(sampleMob.getMobData());
                mmorpg.getChunkManager().getChunkMobs().put(new ChunkManager.ChunkIdentifier(location.getChunk().getX(), location.getChunk().getZ()), mobDataSet);
                System.out.println("Spawned mob " + sampleMob.getName() + "created chunk");
            }
        }
    }

    public HashMap<String, T> getMobs() {
        return mobs;
    }

    public HashMap<SampleMob.MobData, Long> getMobList() {
        return mobList;
    }
}
