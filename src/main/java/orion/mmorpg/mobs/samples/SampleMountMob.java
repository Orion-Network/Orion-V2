package orion.mmorpg.mobs.samples;

import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.EnumHand;
import net.minecraft.world.EnumInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Location;
import orion.mmorpg.utils.Stats;

import javax.annotation.Nullable;

public abstract class SampleMountMob extends SampleMob {


    protected boolean cd;
    protected float cg;
    protected int ci;

    protected SampleMountMob(String name, int level, EntityTypes<? extends EntityCreature> entitytypes, String uuid, Location location, Stats stats) {
        super(name, level, entitytypes, uuid, location, stats);
    }

    @Override
    protected EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) {
        entityhuman.startRiding(this);
        return super.b(entityhuman, enumhand);
    }

    @Nullable
    public Entity getRidingPassenger() {
        return this.cB();
    }

    public boolean isPlayerRiding() {
        return this.getRidingPassenger() instanceof EntityPlayer;
    }

    public void g(Vec3D vec3d) {
        if (this.isAlive()) {
            if (this.isVehicle() && isPlayerRiding()) {
                EntityLiving entityliving = (EntityLiving)this.getRidingPassenger();
                this.setYRot(entityliving.getYRot());
                this.x = this.getYRot();
                this.setXRot(entityliving.getXRot() * 0.5F);
                this.setYawPitch(this.getYRot(), this.getXRot());
                this.aX = this.getYRot();
                this.aZ = this.aX;

                this.setMot(1*this.getMot().getX(), 0, 1*this.getMot().getY());
            } else {
                this.bb = 0.02F;
                super.g(vec3d);
            }
        }

    }

    public boolean fI() {
        return this.cd;
    }

    public void x(boolean flag) {
        this.cd = flag;
    }


}
