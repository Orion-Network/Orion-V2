package orion.mmorpg.managers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.entities.samples.AbstractMob;
import orion.mmorpg.entities.samples.AbstractMount;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class MobManager<T extends AbstractMob> extends AbstractManager implements Runnable {
    HashMap<String, T> mobs = new HashMap<>();
    HashMap<AbstractMob.MobData, Long> mobRespawnCooldown = new HashMap<>();

    public MobManager(Mmorpg mmorpg) {
        super(mmorpg);
    }


    @Override
    public void run() {
        mobRespawnCooldown.forEach((mob, time) -> {
            if(time < System.currentTimeMillis()) {
                try {
                    AbstractMob abstractMob = mob.generateMob();
                    mobs.put(abstractMob.getUuid(), (T) abstractMob);
                    abstractMob.respawn(mob.getUuid(), mob.getLocation(), mmorpg.getChunkManager());
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        mobRespawnCooldown.values().removeIf(time -> time < System.currentTimeMillis());
    }

    public void addMob(AbstractMob.MobData mobData) {
        mobRespawnCooldown.put(mobData, System.currentTimeMillis() + mobData.getSpawnTime());
    }

    public void loadMobs() {
        mmorpg.getMongoClient().getMobCollection().find().forEach((document) -> {
            try {
                Class<? extends AbstractMob> clazz = (Class<? extends AbstractMob>) Class.forName(document.getString("className"));
                Location location = Mmorpg.getOrionGson().getGSON().fromJson(document.getString("location"), Location.class);
                AbstractMob abstractMob = (AbstractMob) clazz.getDeclaredConstructors()[0].newInstance(document.getString("uuid"), location);
                mobs.put(abstractMob.getUuid(), (T) abstractMob);

                Bukkit.getScheduler().runTaskLater(mmorpg, () -> {
                    abstractMob.respawn(abstractMob.getUuid(), abstractMob.getSpawnLocation(), mmorpg.getChunkManager());
                }, 20);

            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        ProtocolLibrary.getProtocolManager().addPacketListener(new MountPacketAdpater(mmorpg));
    }

    public void despawnMobs() {
        for(T mob : mobs.values()) {
            mob.setRemoved(Entity.RemovalReason.UNLOADED_TO_CHUNK);
        }
    }

    public HashMap<String, T> getMobs() {
        return mobs;
    }

    public HashMap<AbstractMob.MobData, Long> getMobRespawnCooldown() {
        return mobRespawnCooldown;
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
                AbstractMount mob = ((CraftEntity) vehicle).getHandle() instanceof AbstractMount ? (AbstractMount) ((CraftEntity) vehicle).getHandle() : null;
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
                if(mob.getMountType().equals(AbstractMount.MountType.LAND)) {
                    if(vehicle.isOnGround() && mob.getJumpForce() + vehicle.getVelocity().getY() < mob.getJumpForce())
                        velocity.setY(jumping ? mob.getJumpForce() : 0);
                    else {
                        velocity.multiply(0.2);
                    }
                    vehicle.setVelocity(vehicle.getVelocity().add(velocity));
                } else if(mob.getMountType().equals(AbstractMount.MountType.AIR)) {
                    vehicle.setVelocity(velocity);
                } else if(mob.getMountType().equals(AbstractMount.MountType.WATER)) {
                    vehicle.setVelocity(vehicle.getVelocity().add(velocity));
                }
        }
    }
}
