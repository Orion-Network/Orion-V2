package orion.mmorpg.entities.pathfinders;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.bukkit.Location;

import java.util.EnumSet;

public class MoveTowardsTargetIfRange extends Goal {
    private double speed;
    private float maxDistance;
    private Mob entity;
    private LivingEntity target;
    private Location spawnPoint;
    public MoveTowardsTargetIfRange(PathfinderMob entity, double speed, float range, Location spawnPoint) {
        this.speed = speed;
        this.maxDistance = range;
        this.entity = entity;
        this.spawnPoint = spawnPoint;

        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        this.target = this.entity.getTarget();
        if(spawnPoint == null) return false;
        if(this.target == null) {

            this.entity.getNavigation().moveTo(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ(), this.speed);
            return false;
        }
        return true;
    }

    @Override
    public void start() {
        this.entity.getNavigation().moveTo(this.entity, this.speed);
    }

    @Override
    public boolean canContinueToUse() {
        return !this.entity.getNavigation().isDone() && this.target.isAlive() && ((this.entity.distanceToSqr(spawnPoint.getX(), spawnPoint.getY(),spawnPoint.getZ()) < (this.maxDistance) * this.maxDistance) || (this.entity.distanceToSqr(target) < (20*20)));
    }

    @Override
    public void stop() {
        this.entity.getNavigation().moveTo(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ(), this.speed);
        this.target = null;
    }
}
