package orion.mmorpg.entities.samples;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.attributes.AttributeBase;
import net.minecraft.world.entity.ai.attributes.AttributeMapBase;
import net.minecraft.world.entity.ai.attributes.AttributeModifiable;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.attribute.CraftAttributeMap;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import orion.mmorpg.utils.Stats;

import java.lang.reflect.Field;
import java.util.Map;

public abstract class AbstractEntityBase extends EntityCreature {
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
    String uuid, name;
    Location spawnLocation;

    Stats stats;

    protected AbstractEntityBase(EntityTypes<? extends EntityCreature> entitytypes,String uuid, String name,  Location spawnLocation, Stats stats) {
        super(entitytypes, ((CraftWorld) spawnLocation.getWorld()).getHandle());
        this.uuid = uuid;
        this.name = name;
        this.spawnLocation = spawnLocation;
        this.stats = stats;

        this.setPosition(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ());
        ((LivingEntity) this.getBukkitEntity()).setRemoveWhenFarAway(false);
        this.setCustomNameVisible(true);

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
        AttributeMapBase attributeMapBase = ((CraftLivingEntity)entity).getHandle().getAttributeMap();
        Map<AttributeBase, AttributeModifiable> map = (Map<AttributeBase, AttributeModifiable>) attributeField.get(attributeMapBase);
        AttributeBase attributeBase = CraftAttributeMap.toMinecraft(attribute);
        AttributeModifiable attributeModifiable = new AttributeModifiable(attributeBase, AttributeModifiable::getAttribute);
        map.put(attributeBase, attributeModifiable);
    }

    private void generateAttribute() {
        this.getAttributeMap().b().add(new AttributeModifiable(GenericAttributes.d, (a) -> {a.setValue(this.getStats().get(Stats.StatsInfo.SPEED)/10);}));
        this.getAttributeMap().b().add(new AttributeModifiable(GenericAttributes.h, (a) -> {a.setValue(this.getStats().get(Stats.StatsInfo.ATTACK_SPEED));}));
        this.getAttributeMap().b().add(new AttributeModifiable(GenericAttributes.b, (a) -> {a.setValue(1.0);}));
        ;
        this.getAttributeMap().a(GenericAttributes.a).setValue(this.getStats().get(Stats.StatsInfo.VITALITY));
        this.getAttributeMap().a(GenericAttributes.f).setValue(this.getStats().get(Stats.StatsInfo.STRENGTH));
        this.setHealth(this.getMaxHealth());
    }

    protected abstract void generatePathfinder();

    private String generateName() {
        return ChatColor.YELLOW + this.getName();
    }

    public void spawn() {
        ((CraftWorld)this.getSpawnLocation().getWorld()).getHandle().addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);

        if(this instanceof Modeled)
            ((Modeled) this).spawnModel();
    };

    public String getUuid() {
        return uuid;
    }

    @Override
    public String getName() {
        return name;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public Stats getStats() {
        return stats;
    }
}
