package orion.mmorpg.mobs.samples;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.attributes.AttributeBase;
import net.minecraft.world.entity.ai.attributes.AttributeMapBase;
import net.minecraft.world.entity.ai.attributes.AttributeModifiable;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.attribute.CraftAttributeMap;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.utils.Rewards;
import orion.mmorpg.utils.Stats;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public abstract class SampleMob extends EntityCreature {
    private static Field attributeField;


    //Credit to @ysl3000
    //We need this to register the new attribute to the pig
    static {
        try {
            attributeField = AttributeMapBase.class.getDeclaredField("b");
            attributeField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    String name;
    int level;
    String uuid;
    Location spawnLocation;
    Stats stats;
    private boolean respawn = true;
    public ChatColor color = ChatColor.YELLOW;
    private Rewards rewards = new Rewards();

    protected SampleMob(String name, int level, EntityTypes<? extends EntityCreature> entitytypes, String uuid, Location location, Stats stats) {
        super(entitytypes, ((CraftWorld) location.getWorld()).getHandle());
        this.name = name;
        this.level = level;
        this.uuid = uuid;
        this.spawnLocation = location;
        this.stats = stats;

        this.setPosition(location.getX(), location.getY(), location.getZ());
        ((LivingEntity) this.getBukkitEntity()).setRemoveWhenFarAway(false);
        this.setCustomName(new ChatMessage(color + name + " [Lvl " + level + "]"));
        this.setCustomNameVisible(true);
        System.out.println("Created mob");

        try {
            registerGenericAttribute(this.getBukkitEntity(), Attribute.GENERIC_ATTACK_DAMAGE);
            registerGenericAttribute(this.getBukkitEntity(), Attribute.GENERIC_MAX_HEALTH);
            registerGenericAttribute(this.getBukkitEntity(), Attribute.GENERIC_MOVEMENT_SPEED);
            registerGenericAttribute(this.getBukkitEntity(), Attribute.GENERIC_ATTACK_SPEED);
            registerGenericAttribute(this.getBukkitEntity(), Attribute.GENERIC_FOLLOW_RANGE);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        generateAttribute();
        generatePathfinder();
    }

    private void generateAttribute() {
        this.getAttributeMap().b().add(new AttributeModifiable(GenericAttributes.f, (a) -> {a.setValue(this.getStats().getStrength());}));
        this.getAttributeMap().b().add(new AttributeModifiable(GenericAttributes.a, (a) -> {a.setValue(this.getStats().getVitality());}));
        this.getAttributeMap().b().add(new AttributeModifiable(GenericAttributes.d, (a) -> {a.setValue(this.getStats().getSpeed()/10);}));
        this.getAttributeMap().b().add(new AttributeModifiable(GenericAttributes.h, (a) -> {a.setValue(this.getStats().getAttack_speed());}));
        this.getAttributeMap().b().add(new AttributeModifiable(GenericAttributes.b, (a) -> {a.setValue(1.0);}));
    }


    public void spawn() {
        ((CraftWorld)this.getSpawnLocation().getWorld()).getHandle().addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        System.out.println("Spawned mob");
    }

    @Override
    protected void initPathfinder() {}

    @Override
    public void die(DamageSource damagesource) {
        super.die(damagesource);
        if(respawn)
            Mmorpg.INSTANCE.getMobManager().addMob(getMobData());
        System.out.println("Died mob");
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        this.setRemoved(RemovalReason.a);
        System.out.println("Saved mob");
    }

    @Override
    protected void dropDeathLoot(DamageSource damagesource, int i, boolean flag) {
        this.drops = rewards.generateRewards();
    }

    protected abstract void generatePathfinder();

    @Override
    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public Stats getStats() {
        return stats;
    }

    public MobData getMobData() {
        return new MobData(this.getClass(), uuid, this.getSpawnLocation(), this.getEntityType());
    }

    public String getUuid() {
        return uuid;
    }

    public Rewards getRewards() {
        return rewards;
    }

    public boolean isRespawn() {
        return respawn;
    }

    public void setRespawn(boolean respawn) {
        this.respawn = respawn;
    }

    public void registerGenericAttribute(org.bukkit.entity.Entity entity, Attribute attribute) throws IllegalAccessException {
        AttributeMapBase attributeMapBase = ((CraftLivingEntity)entity).getHandle().getAttributeMap();
        Map<AttributeBase, AttributeModifiable> map = (Map<AttributeBase, AttributeModifiable>) attributeField.get(attributeMapBase);
        AttributeBase attributeBase = CraftAttributeMap.toMinecraft(attribute);
        AttributeModifiable attributeModifiable = new AttributeModifiable(attributeBase, AttributeModifiable::getAttribute);
        map.put(attributeBase, attributeModifiable);
    }

    public class MobData {
        private String uuid;
        private Class mobClass;
        private Location location;
        private EntityTypes entityType;


        public MobData(Class mobClass,String uuid, Location location, EntityTypes entityType) {
            this.mobClass = mobClass;
            this.uuid = uuid;
            this.location = location;
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

        public EntityTypes getEntityType() {
            return entityType;
        }

        public long getSpawnTime() {
            return 5000;
        }

        public SampleMob generateMob() throws InvocationTargetException, InstantiationException, IllegalAccessException {
            return (SampleMob) this.getMobClass().getDeclaredConstructors()[0].newInstance(this.getUuid(), this.getLocation());
        }
    }
}
