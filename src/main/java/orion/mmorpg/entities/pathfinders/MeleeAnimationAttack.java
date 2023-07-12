package orion.mmorpg.entities.pathfinders;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;
import orion.mmorpg.entities.samples.AbstractHostile;
import orion.mmorpg.entities.samples.Modeled;
import orion.mmorpg.utils.Stats;

import java.util.EnumSet;

public class MeleeAnimationAttack<T extends AbstractHostile & Modeled> extends Goal {

    private final String animation = "attack";
    private double lerpIn, lerpOut, speed, reach, distance;
    T abstractModeledMob;

    private Path path;

    public  MeleeAnimationAttack(T abstractModeledMob, double lerpIn, double lerpOut, double speed, double reach, double distance) {
        this.abstractModeledMob = abstractModeledMob;
        this.lerpIn = lerpIn;
        this.lerpOut = lerpOut;
        this.speed = speed;
        this.reach = reach;
        this.distance = distance;

        this.setFlags(EnumSet.of(Flag.MOVE, Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = abstractModeledMob.getTarget();
        if(target == null) return false;
        if(!target.isAlive()) return false;

        path = abstractModeledMob.getNavigation().createPath(target, 0);
        if(path != null) return true;
        return abstractModeledMob.distanceToSqr(target) < distance * distance;
    }


    @Override
    public boolean canContinueToUse() {
        LivingEntity target = abstractModeledMob.getTarget();
        if(target == null) return false;
        if(!target.isAlive()) return false;

        if(abstractModeledMob.getNavigation().isDone()) return true;
        return abstractModeledMob.distanceToSqr(target) < (distance * distance);
    }

    @Override
    public void start() {
        abstractModeledMob.getNavigation().moveTo(path, abstractModeledMob.getStats().get(Stats.StatsInfo.SPEED)/10);
    }

    @Override
    public void stop() {
        abstractModeledMob.setTarget(null);
        abstractModeledMob.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity target = abstractModeledMob.getTarget();
        this.abstractModeledMob.getLookControl().setLookAt(target, 30.0F, 30.0F);
        double distance = this.abstractModeledMob.distanceToSqr(target.getX(), target.getY(), target.getZ());

        if(distance < (reach*reach) && !abstractModeledMob.getModeledEntity().getModel("kindletronsr").getAnimationHandler().isPlayingAnimation(animation)) {
            abstractModeledMob.getModeledEntity().getModel("kindletronsr").getAnimationHandler().playAnimation(animation, lerpIn, lerpOut, speed, false);
            //scarecrow.attackEntity(target);
        } else {
            abstractModeledMob.getNavigation().moveTo(target, abstractModeledMob.getStats().get(Stats.StatsInfo.SPEED)/10);
        }
    }
}
