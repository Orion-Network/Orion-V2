package orion.mmorpg.entities.pathfinders;

import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.level.pathfinder.PathEntity;
import orion.mmorpg.entities.samples.AbstractHostile;
import orion.mmorpg.entities.samples.Modeled;
import orion.mmorpg.utils.Stats;

import java.util.EnumSet;

public class MeleeAnimationAttack<T extends AbstractHostile & Modeled> extends PathfinderGoal {

    private final String animation = "attack";
    private double lerpIn, lerpOut, speed, reach, distance;
    T abstractModeledMob;

    private PathEntity path;

    public  MeleeAnimationAttack(T abstractModeledMob, double lerpIn, double lerpOut, double speed, double reach, double distance) {
        this.abstractModeledMob = abstractModeledMob;
        this.lerpIn = lerpIn;
        this.lerpOut = lerpOut;
        this.speed = speed;
        this.reach = reach;
        this.distance = distance;

        this.a(EnumSet.of(Type.a, Type.b));
    }

    //Can Pathfinding goal be run
    @Override
    public boolean a() {
        EntityLiving target = abstractModeledMob.getGoalTarget();
        if(target == null) return false;
        if(!target.isAlive()) return false;

        path = abstractModeledMob.getNavigation().a(target, 0);
        if(path != null) return true;
        if(abstractModeledMob.e(target) < distance*distance) return true;
        return false;
    }

    //Can continue running
    @Override
    public boolean b() {
        EntityLiving target = abstractModeledMob.getGoalTarget();
        if(target == null) return false;
        if(!target.isAlive()) return false;

        if(abstractModeledMob.getNavigation().m()) return true;
        return abstractModeledMob.e(target) < (distance * distance);
    }

    //Start pathfinding called when a() returns true
    @Override
    public void c() {
        abstractModeledMob.getNavigation().a(path, abstractModeledMob.getStats().get(Stats.StatsInfo.SPEED)/10);
    }

    //Stop pathfinding called when b() returns false
    @Override
    public void d() {
        abstractModeledMob.setGoalTarget(null);
        abstractModeledMob.getNavigation().o();
    }

    //Tick
    @Override
    public void e() {
        EntityLiving target = abstractModeledMob.getGoalTarget();
        this.abstractModeledMob.getControllerLook().a(target, 30.0F, 30.0F);
        double distance = this.abstractModeledMob.h(target.locX(), target.locY(), target.locZ());

        if(distance < (reach*reach) && !abstractModeledMob.getModeledEntity().getModel("kindletronsr").getAnimationHandler().isPlayingAnimation(animation)) {
            abstractModeledMob.getModeledEntity().getModel("kindletronsr").getAnimationHandler().playAnimation(animation, lerpIn, lerpOut, speed, false);
            //scarecrow.attackEntity(target);
        } else {
            abstractModeledMob.getNavigation().a(target, abstractModeledMob.getStats().get(Stats.StatsInfo.SPEED)/10);
        }
    }
}
