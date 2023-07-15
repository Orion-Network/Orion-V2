package orion.mmorpg.listeners;

import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.managers.ChunkManager;
import orion.mmorpg.entities.samples.AbstractMob;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashSet;

public class ChunkListener implements Listener {

    Mmorpg mmorpg;

    public ChunkListener(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if(mmorpg.getChunkManager().containChunk(event.getChunk())) {
            for(AbstractMob.MobData mobData : mmorpg.getChunkManager().getChunk(event.getChunk())) {
                try {
                    AbstractMob mob = mobData.generateMob();
                    mob.spawn();
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        LinkedHashSet<AbstractMob.MobData> mobs = new LinkedHashSet<>();
        for(Entity entity : event.getChunk().getEntities()) {
            net.minecraft.world.entity.Entity nmsEntity = ((CraftEntity) entity).getHandle();
            if(nmsEntity instanceof AbstractMob) {
                mobs.add(((AbstractMob) nmsEntity).getMobData());
                ((AbstractMob)nmsEntity).keyedBossBar.setVisible(false);
            }
            nmsEntity.setRemoved(net.minecraft.world.entity.Entity.RemovalReason.UNLOADED_TO_CHUNK);
        }
        mmorpg.getChunkManager().getChunkMobs().put(new ChunkManager.ChunkIdentifier(event.getChunk().getX(), event.getChunk().getZ()), mobs);
    }


}
