package orion.mmorpg.entities.samples;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.entities.Respawnable;
import orion.mmorpg.events.MobKilledEvent;
import orion.mmorpg.managers.ChunkManager;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Rewards;
import orion.mmorpg.utils.Stats;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class AbstractMob extends AbstractEntityBase implements Respawnable {
    int mobLevel;
    public KeyedBossBar keyedBossBar;
    @Getter@Setter
    private boolean respawn = true, isSummon = false;
    public ChatColor color = ChatColor.YELLOW;
    private Rewards rewards = new Rewards();

    public AbstractMob(EntityType<? extends PathfinderMob> entitytypes, String uuid, String name, Location spawnLocation, Stats stats, int mobLevel) {
        super(entitytypes, uuid, name, spawnLocation, stats);
        this.mobLevel = mobLevel;

        this.getRewards().setExp(this.mobLevel * 10);
    }

    @Override
    public boolean hurt(DamageSource damagesource, float f) {
        //this.setCustomName(new TextComponent(generateName()));
        float progress = (this.getHealth()-f) / this.getMaxHealth();
        this.keyedBossBar.setProgress((progress > 1 ? 1 : (progress < 0 ? 0 : progress)));
        if(damagesource.getEntity() instanceof net.minecraft.world.entity.player.Player) {
            Player player = ((CraftPlayer) damagesource.getEntity().getBukkitEntity()).getPlayer();
            if(!this.keyedBossBar.getPlayers().contains(player)) {
                this.keyedBossBar.addPlayer(player);
            }
        }
        return super.hurt(damagesource, f);
    }

    private String generateName() {
        return color + mobName + ChatColor.YELLOW + " [Lvl " + mobLevel + "]";
    }

    @Override
    public void respawn(String uuid, Location location, ChunkManager chunkManager) {
        if(location.getChunk().isLoaded()) {
            this.spawn();
        } else {
            if (chunkManager.containChunk(location.getChunk())) {
                chunkManager.getChunk(location.getChunk()).add(this.getMobData());
            } else {
                Set<MobData> mobDataSet = new LinkedHashSet<>();
                mobDataSet.add(this.getMobData());
                chunkManager.getChunkMobs().put(new ChunkManager.ChunkIdentifier(location.getChunk().getX(), location.getChunk().getZ()), mobDataSet);
            }
        }
    }

    @Override
    public void spawn() {
        //this.setCustomName(new TextComponent(generateName()));
        if(Bukkit.getBossBar(NamespacedKey.minecraft(uuid)) != null)
            this.keyedBossBar = Bukkit.getBossBar(NamespacedKey.minecraft(uuid));
        else
            this.keyedBossBar = Bukkit.createBossBar(NamespacedKey.minecraft(uuid), mobName, BarColor.PURPLE, BarStyle.SEGMENTED_10);

        this.keyedBossBar.setVisible(true);

        super.spawn();
    }

    //TODO See why mob respawns when it dies
    @Override
    public void die(DamageSource damagesource) {
        super.die(damagesource);
        this.keyedBossBar.setVisible(false);
        this.keyedBossBar.removeAll();
        if(respawn)
            Mmorpg.INSTANCE.getMobManager().addMob(getMobData());
        if(damagesource.getEntity() == null) return;
        if(damagesource.getEntity().getBukkitEntity() instanceof Player player) {
            OrionPlayer orionplayer = Mmorpg.INSTANCE.getPlayerManager().getPlayer(player.getUniqueId());
            orionplayer.addExperience(player, rewards.getExp());
            Bukkit.getPluginManager().callEvent(new MobKilledEvent(this, player));
        }
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damagesource, int i, boolean flag) {
        OrionPlayer orionplayer;
        if(damagesource.getEntity() == null) return;
        if(damagesource.getEntity().getBukkitEntity() instanceof Player player) {
            orionplayer = Mmorpg.INSTANCE.getPlayerManager().getPlayer(player.getUniqueId());
            this.drops = rewards.award(orionplayer);
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.keyedBossBar.getPlayers().forEach(player -> {
            if(this.distanceToSqr(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()) > 100*100)
                this.keyedBossBar.removePlayer(player);
        });
    }


    public int getMobLevel() {
        return mobLevel;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public Stats getStats() {
        return stats;
    }

    public MobData getMobData() {
        return new MobData(this.getClass(), uuid, this.getSpawnLocation(), 60*1000, this.getType());
    }

    public String getUuid() {
        return uuid;
    }

    public Rewards getRewards() {
        return rewards;
    }



    /**
     * This class is used to store mob data for respawn
     */
    public class MobData {
        private String uuid;
        private Class mobClass;
        private Location location;
        private EntityType entityType;
        private int respawnTime;


        public MobData(Class mobClass,String uuid, Location location, int respawnTime, EntityType entityType) {
            this.mobClass = mobClass;
            this.uuid = uuid;
            this.location = location;
            this.respawnTime = respawnTime;
            this.entityType = entityType;
        }

        public Class getMobClass() {
            return mobClass;
        }

        public String getUuid() {
            return uuid;
        }

        public Location getLocation() {
            return location;
        }

        public EntityType getEntityType() {
            return entityType;
        }

        public long getSpawnTime() {
            return respawnTime;
        }

        /**
         * Generates an instance of the mob from data
         * @return return an instance of the mob cast to AbstractMob
         * @throws InvocationTargetException if the constructor throws an exception
         * @throws InstantiationException if the class that declares the underlying constructor represents an abstract class.
         * @throws IllegalAccessException if this {@code Constructor} object is enforcing Java language access control and the underlying constructor is inaccessible.
         */
        public AbstractMob generateMob() throws InvocationTargetException, InstantiationException, IllegalAccessException {
            return (AbstractMob) this.getMobClass().getDeclaredConstructors()[0].newInstance(this.getUuid(), this.getLocation());
        }
    }
}
