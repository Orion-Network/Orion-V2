package orion.mmorpg.entities.samples;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.world.EnumHand;
import net.minecraft.world.EnumInteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomLookaround;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.entity.player.EntityHuman;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.entities.EntityType;
import orion.mmorpg.entities.Pets;
import orion.mmorpg.utils.Stats;
import orion.mmorpg.utils.Utils;

import java.util.EnumSet;

public abstract class AbstractPet extends AbstractEntityBase {
    Player owner;
    Pets pet;

    public AbstractPet(EntityTypes<? extends EntityCreature> entitytypes, String uuid, String name, Location spawnLocation, Player owner, Pets pet) {
        super(entitytypes, uuid, name, spawnLocation, new Stats.Builder().setBase(Stats.StatsInfo.VITALITY, 1).setBase(Stats.StatsInfo.SPEED, 4).build());
        this.owner = owner;
        this.pet = pet;

        this.setInvulnerable(true);

        this.setGoalTarget(((CraftPlayer) owner).getHandle(), EntityTargetEvent.TargetReason.CUSTOM, true);
        Mmorpg.INSTANCE.getPlayerManager().getPlayer(owner.getUniqueId()).setPet(this);
        this.setCustomName(new ChatMessage(generateName()));
    }

    @Override
    public void spawn() {
        super.spawn();
    }

    @Override
    protected void generatePathfinder() {
        this.bP.a(0, new PathfinderGoalFloat(this));
        this.bP.a(1, new PathfinderGoalPet(this, 0.8D, 25));
        this.bP.a(2, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.bP.a(3, new PathfinderGoalRandomLookaround(this));
    }

    @Override
    protected EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) {
        //Play particle
        ((Player) entityhuman.getBukkitEntity()).spawnParticle(Particle.VILLAGER_HAPPY, this.getBukkitEntity().getLocation(), 5, 0.5, 0.5, 0.5, 0.5);
        return super.b(entityhuman, enumhand);
    }

    @Override
    public void tick() {
        if(!owner.isOnline())
            this.setRemoved(RemovalReason.a);
        super.tick();
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        this.setCustomName(new ChatMessage(generateName()));
        return true;
    }

    private String generateName() {
        return name + ChatColor.YELLOW + " [ " + owner.getName() + " ]";
    }

    public Player getOwner() {
        return owner;
    }

    public Pets getPet() {
        return pet;
    }

    //Make so the pet don't go in the player
    private static class PathfinderGoalPet extends PathfinderGoal {
        private final EntityInsentient petEntity;
        private EntityLiving owner;

        private final double speed;
        private final float distance;

        private double targetX;
        private double targetY;
        private double targetZ;

        public PathfinderGoalPet(EntityInsentient petEntity,  double speed, float distance) {
            this.petEntity = petEntity;
            this.speed = speed;
            this.distance = distance;

            this.a(EnumSet.of(Type.a));
        }

        //run when c true
        @Override
        public boolean a() {
            this.owner = this.petEntity.getGoalTarget();
            if(this.owner == null) {
                return false;
            } else if(!this.owner.isAlive()) {
                return false;
            } else if(this.petEntity.f(this.owner) > (this.distance * this.distance)) {
                petEntity.setPosition(this.owner.locX(), this.owner.locY(), this.owner.locZ());
                return false;
            } else {
                if(this.petEntity.f(this.owner) < 1) {
                    return false;
                }
                this.targetX = this.owner.locX();
                this.targetY = this.owner.locY();
                this.targetZ = this.owner.locZ();

                return true;
            }
        }

        //run c every tick if true
        @Override
        public boolean b() {
            return !this.petEntity.getNavigation().m() && this.petEntity.f(this.owner) < (this.distance * this.distance);
        }

        //runner
        @Override
        public void c() {
            this.petEntity.getNavigation().a(this.targetX, this.targetY, this.targetZ, this.speed);
        }

        // run when b false
        @Override
        public void d() {
            this.owner = null;
        }
    }
}
