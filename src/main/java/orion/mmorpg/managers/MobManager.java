package orion.mmorpg.managers;

import org.bukkit.Location;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.mobs.samples.SampleMob;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class MobManager<T extends SampleMob> implements Runnable {
    HashMap<String, T> mobs = new HashMap<>();
    HashMap<SampleMob.MobData, Long> mobList = new HashMap<>();
    Mmorpg mmorpg;

    public MobManager(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @Override
    public void run() {
        mobList.forEach((mob, time) -> {
            if(time < System.currentTimeMillis()) {
                System.out.println(mob.getLocation().getWorld().isChunkLoaded(mob.getLocation().getChunk()));
                try {
                    SampleMob sampleMob = mob.generateMob();
                    sampleMob.spawn();
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
                sampleMob.spawn();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }
}
