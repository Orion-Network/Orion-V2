package orion.mmorpg.mobs.samples;

import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomLookaround;
import net.minecraft.world.entity.player.EntityHuman;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.utils.Stats;

import java.util.EnumSet;

public abstract class SamplePetMob extends SampleMob {
    public SamplePetMob(String name, EntityTypes<? extends EntityCreature> entitytypes, String uuid, Location location) {
        super(name, -1, entitytypes, uuid, location, new Stats.Builder().withVitality(1).withSpeed(4).build());
    }

    public void summonPet(Player player) {
        this.spawn();
        this.setGoalTarget(((CraftPlayer) player).getHandle(), EntityTargetEvent.TargetReason.CUSTOM, true);
        Mmorpg.INSTANCE.getPlayerManager().getPlayer(player.getUniqueId()).setPet(this);
    }

    @Override
    protected void generatePathfinder() {
        this.bP.a(0, new PathfinderGoalFloat(this));
        this.bP.a(1, new PathfinderGoalPet(this, 0.8D, 25));
        this.bP.a(2, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.bP.a(3, new PathfinderGoalRandomLookaround(this));
    }

    private class PathfinderGoalPet extends PathfinderGoal {
        private EntityInsentient petEntity;
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
            } else if(!(this.petEntity.f(this.owner) > (this.distance * this.distance))) {
                petEntity.setPosition(this.owner.locX(), this.owner.locY(), this.owner.locZ());
                return false;
            } else {
                if(this.petEntity.f(this.owner) < 1)
                    return false;
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
