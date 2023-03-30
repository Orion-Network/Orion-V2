package orion.mmorpg.entities.pathfinders;

import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import org.bukkit.Bukkit;
import orion.mmorpg.entities.samples.AbstractModeledMob;

import java.util.EnumSet;

public class PlayAnimationIfRange extends PathfinderGoal {

    private String animation;
    private double lerpIn, lerpOut, reach;
    private float maxDistance;

    private AbstractModeledMob modeledMob;
    private EntityLiving target;

    //Work but conflict with the back spawn
    public PlayAnimationIfRange(AbstractModeledMob modeledMob, String animation, double lerpIn, double lerpOut, double reach, float maxDistance) {
        this.modeledMob = modeledMob;
        this.animation = animation;
        this.lerpIn = lerpIn;
        this.lerpOut = lerpOut;
        this.reach = reach;
        this.maxDistance = maxDistance;

        this.a(EnumSet.of(Type.a));
    }

    //Can Pathfinding goal be run
    @Override
    public boolean a() {
        this.target = this.modeledMob.getGoalTarget();
        if(this.target == null) return false;
        if(this.modeledMob.getModeledEntity().getModel("kindletronsr").getAnimationHandler().isPlayingAnimation(animation)) return false;
        return this.modeledMob.e(this.target) < (this.maxDistance) * this.maxDistance;
    }

    //Can continue running
    @Override
    public boolean b() {
        System.out.println("Distance: " + this.modeledMob.e(this.target));
        return !this.modeledMob.getNavigation().m() && this.target.isAlive()  && this.modeledMob.e(this.target) < (this.maxDistance) * this.maxDistance;
    }


    //Start pathfinding
    @Override
    public void c() {
        this.modeledMob.getNavigation().a(this.target, this.modeledMob.getStats().getSpeed()/10);
        //this.scarecrow.getModeledEntity().getModel("kindletronsr").getAnimationHandler().playAnimation(animation, lerpIn, lerpOut, speed, false);
    }

    //Stop pathfinding
    @Override
    public void d() {
        this.modeledMob.setGoalTarget(null);
        this.modeledMob.getNavigation().o();
    }

    //run every tick
    @Override
    public void e() {
        EntityLiving entity = this.modeledMob.getGoalTarget();
        this.modeledMob.getControllerLook().a(entity, 30.0F, 30.0F);
        if(this.modeledMob.getModeledEntity().getModel("kindletronsr").getAnimationHandler().isPlayingAnimation(animation) || this.modeledMob.e(this.target) > (this.reach) * this.reach) return;
        Bukkit.broadcastMessage("Playing animation");
        this.modeledMob.getModeledEntity().getModel("kindletronsr").getAnimationHandler().playAnimation(animation, lerpIn, lerpOut, 1, false);
    }


}
