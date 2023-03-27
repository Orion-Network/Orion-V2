package orion.mmorpg.mobs.samples;

import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.EnumHand;
import net.minecraft.world.EnumInteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Location;
import orion.mmorpg.utils.Stats;

import javax.annotation.Nullable;
import java.util.EnumSet;

public abstract class SampleMountMob extends SampleMob {


    double forwardMultiplier = 0.2, strafeMultiplier = 0.3, jumpForce = 0.5;
    final MountType type;

    protected SampleMountMob(String name, int level, EntityTypes<? extends EntityCreature> entitytypes, String uuid, Location location, Stats stats, MountType type) {
        super(name, level, entitytypes, uuid, location, stats);
        this.type = type;
    }

    @Override
    public void generatePathfinder() {
    }

    @Override
    protected EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) {
        entityhuman.startRiding(this);
        return super.b(entityhuman, enumhand);
    }

    public double getForwardMultiplier() {
        return forwardMultiplier;
    }

    public void setForwardMultiplier(double forwardMultiplier) {
        this.forwardMultiplier = forwardMultiplier;
    }

    public double getStrafeMultiplier() {
        return strafeMultiplier;
    }

    public void setStrafeMultiplier(double strafeMultiplier) {
        this.strafeMultiplier = strafeMultiplier;
    }

    public double getJumpForce() {
        return jumpForce;
    }

    public void setJumpForce(double jumpForce) {
        this.jumpForce = jumpForce;
    }

    public MountType getType() {
        return type;
    }

    public enum MountType {
        LAND, WATER, AIR;
    }
}
