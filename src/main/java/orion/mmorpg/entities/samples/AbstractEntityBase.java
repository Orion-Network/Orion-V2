package orion.mmorpg.entities.samples;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.attribute.CraftAttributeMap;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftTextDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import orion.mmorpg.utils.Stats;
import orion.mmorpg.utils.Utils;

import java.lang.reflect.Field;
import java.util.Map;

public abstract class AbstractEntityBase extends PathfinderMob {
    private static Field attributeField;
    @Getter@Setter
    TextDisplay overheadEntity;


    //Credit to @ysl3000
    //We need this to register the new attribute to the pig
    static {

        try {
            attributeField = AttributeMap.class.getDeclaredField("b");
            attributeField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
    @Getter
    String uuid, mobName;
    Location spawnLocation;

    Stats stats;

    protected AbstractEntityBase(EntityType<? extends PathfinderMob> entitytypes, String uuid, String mobName, Location spawnLocation, Stats stats) {
        super(entitytypes, ((CraftWorld) spawnLocation.getWorld()).getHandle());
        this.uuid = uuid;
        this.mobName = mobName;
        this.spawnLocation = spawnLocation;
        this.stats = stats;

        this.setPos(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ());
        ((LivingEntity) this.getBukkitEntity()).setRemoveWhenFarAway(false);
        this.setCustomNameVisible(false);

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

    private void registerGenericAttribute(org.bukkit.entity.Entity entity, Attribute attribute) throws IllegalAccessException {
        AttributeMap attributeMapBase = ((CraftLivingEntity)entity).getHandle().getAttributes();
        Map<net.minecraft.world.entity.ai.attributes.Attribute, AttributeInstance> map = (Map<net.minecraft.world.entity.ai.attributes.Attribute, AttributeInstance>) attributeField.get(attributeMapBase);
        net.minecraft.world.entity.ai.attributes.Attribute attributeBase = CraftAttributeMap.toMinecraft(attribute);
        AttributeInstance attributeModifiable = new AttributeInstance(attributeBase, AttributeInstance::getAttribute);
        map.put(attributeBase, attributeModifiable);
    }

    private void generateAttribute() {
        this.getAttributes().getSyncableAttributes().add(new AttributeInstance(Attributes.MOVEMENT_SPEED, (a) -> {a.setBaseValue(this.getStats().get(Stats.StatsInfo.SPEED)/10);}));
        this.getAttributes().getSyncableAttributes().add(new AttributeInstance(Attributes.ATTACK_SPEED, (a) -> {a.setBaseValue(this.getStats().get(Stats.StatsInfo.ATTACK_SPEED));}));
        this.getAttributes().getSyncableAttributes().add(new AttributeInstance(Attributes.ARMOR, (a) -> {a.setBaseValue(1.0);}));
        ;
        this.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(this.getStats().get(Stats.StatsInfo.VITALITY));
        this.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(this.getStats().get(Stats.StatsInfo.STRENGTH));
        this.setHealth(this.getMaxHealth());
    }

    protected abstract void generatePathfinder();

    private String generateName() {
        return ChatColor.YELLOW + this.getMobName() ;
    }

    public void spawn() {
        ((CraftWorld)this.getSpawnLocation().getWorld()).getHandle().addFreshEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);

        this.setYRot(this.spawnLocation.getYaw());
        this.setYHeadRot(this.spawnLocation.getYaw());
        this.setYBodyRot(this.spawnLocation.getYaw());

        if(this instanceof Modeled) {
            ((Modeled) this).spawnModel();
            //TODO see to spawn overhead mob
        } else {
            this.overheadEntity = (TextDisplay) this.getSpawnLocation().getWorld().spawnEntity(this.getSpawnLocation(), org.bukkit.entity.EntityType.TEXT_DISPLAY);
            this.overheadEntity.setText(this.generateName());
            this.overheadEntity.setBillboard(Display.Billboard.CENTER);
            this.overheadEntity.setTransformation(new Transformation(new Vector3f(0, 1, 0), new AxisAngle4f(), new Vector3f(1,1,1), new AxisAngle4f()));
            ((CraftTextDisplay) this.overheadEntity).getHandle().startRiding(this);
        }
    };


    @Override
    public void remove(RemovalReason entity_removalreason) {
        if(this.overheadEntity != null && !this.overheadEntity.isDead())
            this.overheadEntity.remove();
        super.remove(entity_removalreason);
    }

    @Override
    public void die(DamageSource damagesource) {
        super.die(damagesource);
        this.overheadEntity.remove();
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public Stats getStats() {
        return stats;
    }
}
