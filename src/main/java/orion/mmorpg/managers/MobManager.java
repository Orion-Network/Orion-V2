package orion.mmorpg.managers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.EntityHorseAbstract;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.mobs.samples.SampleMob;
import orion.mmorpg.mobs.samples.SampleMountMob;

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

        ProtocolLibrary.getProtocolManager().addPacketListener(new MountPacketAdpater(mmorpg));
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
        } else {
            if (mmorpg.getChunkManager().containChunk(location.getChunk())) {
                mmorpg.getChunkManager().getChunk(location.getChunk()).add(sampleMob.getMobData());
            } else {
                Set<SampleMob.MobData> mobDataSet = new LinkedHashSet<>();
                mobDataSet.add(sampleMob.getMobData());
                mmorpg.getChunkManager().getChunkMobs().put(new ChunkManager.ChunkIdentifier(location.getChunk().getX(), location.getChunk().getZ()), mobDataSet);
            }
        }
    }

    public HashMap<String, T> getMobs() {
        return mobs;
    }

    public HashMap<SampleMob.MobData, Long> getMobList() {
        return mobList;
    }

    private class MountPacketAdpater extends PacketAdapter {
        public MountPacketAdpater(Plugin plugin) {
            super(plugin, PacketType.Play.Client.STEER_VEHICLE);
        }

        @Override
        public void onPacketReceiving(PacketEvent event) {
                if(event.isCancelled()) return;
                org.bukkit.entity.Entity vehicle = event.getPlayer().getVehicle();
                if(vehicle == null) return;
                SampleMountMob mob = ((CraftEntity) vehicle).getHandle() instanceof SampleMountMob ? (SampleMountMob) ((CraftEntity) vehicle).getHandle() : null;
                if(mob == null) return;

                float swSpeed, fwSpeed; // so we can access them outside of the scope of our try-catch block
                boolean jumping;
                try {
                    swSpeed = event.getPacket().getFloat().read(0);
                    fwSpeed = event.getPacket().getFloat().read(1);
                    jumping = event.getPacket().getBooleans().read(0);

                } catch (FieldAccessException ex){
                    ex.printStackTrace();
                    return; // if something goes wrong, we exit and don't do anything with the packet.
                }

                Location location = event.getPlayer().getLocation();
                vehicle.setRotation(location.getYaw(), location.getPitch());
                Vector forwardDir = location.getDirection().multiply(mob.getForwardMultiplier());
                Vector sideways = forwardDir.clone().crossProduct(new Vector(0, -1, 0)).multiply(mob.getStrafeMultiplier());

                Vector velocity = forwardDir.multiply(fwSpeed).add(sideways.multiply(swSpeed));
                if(mob.getType().equals(SampleMountMob.MountType.LAND)) {
                    if(vehicle.isOnGround() && mob.getJumpForce() + vehicle.getVelocity().getY() < mob.getJumpForce())
                        velocity.setY(jumping ? mob.getJumpForce() : 0);
                    else {
                        velocity.multiply(0.2);
                    }
                    vehicle.setVelocity(vehicle.getVelocity().add(velocity));
                } else if(mob.getType().equals(SampleMountMob.MountType.AIR)) {
                    vehicle.setVelocity(velocity);
                } else if(mob.getType().equals(SampleMountMob.MountType.WATER)) {
                    vehicle.setVelocity(vehicle.getVelocity().add(velocity));
                }
        }
    }
}
