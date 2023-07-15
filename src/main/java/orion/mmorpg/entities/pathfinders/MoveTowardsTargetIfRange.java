package orion.mmorpg.entities.pathfinders;

import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.EnumSet;

public class MoveTowardsTargetIfRange extends PathfinderGoal {
    private double speed;
    private float maxDistance;
    private EntityInsentient entity;
    private EntityLiving target;
    private Location spawnPoint;
    public MoveTowardsTargetIfRange(EntityCreature entity, double speed, float range, Location spawnPoint) {
        this.speed = speed;
        this.maxDistance = range;
        this.entity = entity;
        this.spawnPoint = spawnPoint;

        this.a(EnumSet.of(Type.a));
    }

    public boolean a() {
        this.target = this.entity.getGoalTarget();
        if(spawnPoint == null) return false;
        if(this.target == null) {

            this.entity.getNavigation().a(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ(), this.speed);
            return false;
        }
        return true;
    }

    public void c() {
        this.entity.getNavigation().a(this.entity, this.speed);
    }

    public boolean b() {
        return !this.entity.getNavigation().m() && this.target.isAlive() && ((this.entity.h(spawnPoint.getX(), spawnPoint.getY(),spawnPoint.getZ()) < (this.maxDistance) * this.maxDistance) || (this.entity.e(target) < (20*20)));
    }

    public void d() {
        // runs when b() returns false
        this.entity.getNavigation().a(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ(), this.speed);
        this.target = null;
    }
}
