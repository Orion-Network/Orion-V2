package orion.mmorpg.listeners;

import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.mobs.samples.SampleMob;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class ChunkListener implements Listener {

    Mmorpg mmorpg;
    private LinkedHashMap<Chunk, Set<SampleMob.MobData>> chunkMobs = new LinkedHashMap<>();

    public ChunkListener(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if(chunkMobs.containsKey(event.getChunk())) {
            for(SampleMob.MobData mobData : chunkMobs.get(event.getChunk())) {
                try {
                    SampleMob mob = mobData.generateMob();
                    mob.spawn();
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        LinkedHashSet<SampleMob.MobData> mobs = new LinkedHashSet<>();
        for(Entity entity : event.getChunk().getEntities()) {
            net.minecraft.world.entity.Entity nmsEntity = ((CraftEntity) entity).getHandle();
            if(nmsEntity instanceof SampleMob) {
                mobs.add(((SampleMob) nmsEntity).getMobData());
            }
            nmsEntity.setRemoved(net.minecraft.world.entity.Entity.RemovalReason.a);
            System.out.println("Unloading entity: " + nmsEntity.getClass().getName());
        }
        chunkMobs.put(event.getChunk(), mobs);
    }


}
