package orion.mmorpg.entities.samples;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.entities.Pets;
import orion.mmorpg.utils.Stats;

import java.util.EnumSet;

public abstract class AbstractPet extends AbstractEntityBase {
    Player owner;
    Pets pet;

    public AbstractPet(EntityType<? extends PathfinderMob> entitytypes, String uuid, String name, Location spawnLocation, Player owner, Pets pet) {
        super(entitytypes, uuid, name, spawnLocation, new Stats.Builder().setBase(Stats.StatsInfo.VITALITY, 1).setBase(Stats.StatsInfo.SPEED, 4).build());
        this.owner = owner;
        this.pet = pet;

        this.setInvulnerable(true);

        this.setTarget(((CraftPlayer) owner).getHandle(), EntityTargetEvent.TargetReason.CUSTOM, true);
        Mmorpg.INSTANCE.getPlayerManager().getPlayer(owner.getUniqueId()).setPet(this);
        //this.setCustomName(new TextComponent(generateName()));
    }

    @Override
    public void spawn() {
        super.spawn();
    }

    @Override
    protected void generatePathfinder() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PathfinderGoalPet(this, 0.8D, 25));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, net.minecraft.world.entity.player.Player.class, 8.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    @Override
    protected InteractionResult mobInteract(net.minecraft.world.entity.player.Player entityhuman, InteractionHand enumhand) {
        //Play particle
        ((Player) entityhuman.getBukkitEntity()).spawnParticle(Particle.VILLAGER_HAPPY, this.getBukkitEntity().getLocation(), 5, 0.5, 0.5, 0.5, 0.5);
        return super.mobInteract(entityhuman, enumhand);
    }


    @Override
    public void tick() {
        if(!owner.isOnline())
            this.setRemoved(RemovalReason.UNLOADED_WITH_PLAYER);
        super.tick();
    }

    @Override
    public boolean hurt(DamageSource damagesource, float f) {
        //this.setCustomName(new TextComponent(generateName()));
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
    private static class PathfinderGoalPet extends Goal {
        private final PathfinderMob petEntity;
        private LivingEntity owner;

        private final double speed;
        private final float distance;

        private double targetX;
        private double targetY;
        private double targetZ;

        public PathfinderGoalPet(PathfinderMob petEntity,  double speed, float distance) {
            this.petEntity = petEntity;
            this.speed = speed;
            this.distance = distance;

            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            this.owner = this.petEntity.getTarget();
            if(this.owner == null) {
                return false;
            } else if(!this.owner.isAlive()) {
                return false;
            } else if(this.petEntity.distanceToSqr(this.owner) > (this.distance * this.distance)) {
                petEntity.setPos(this.owner.getX(), this.owner.getY(), this.owner.getZ());
                return false;
            } else {
                if(this.petEntity.distanceToSqr(this.owner) < 1) {
                    return false;
                }
                this.targetX = this.owner.getX();
                this.targetY = this.owner.getY();
                this.targetZ = this.owner.getZ();

                return true;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return !this.petEntity.getNavigation().isDone() && this.petEntity.distanceToSqr(this.owner) < (this.distance * this.distance);
        }

        @Override
        public void start() {

            this.petEntity.getNavigation().moveTo(this.targetX, this.targetY, this.targetZ, this.speed);
        }

        @Override
        public void stop() {
            this.owner = null;
        }
    }
}
