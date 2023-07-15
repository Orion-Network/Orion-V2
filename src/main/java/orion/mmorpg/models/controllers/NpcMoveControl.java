package orion.mmorpg.models.controllers;

import lombok.Getter;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;
import orion.mmorpg.models.NPC;
import orion.mmorpg.models.NPCUtils;
import orion.mmorpg.utils.NMS;

import java.util.Random;

public class NpcMoveControl extends MoveControl {
    protected final NPC entity;
    private int jumpTicks;
    protected boolean moving;
    @Getter
    protected double speed;
    @Getter
    protected double tx;
    @Getter
    protected double ty;
    @Getter
    protected double tz;

    public NpcMoveControl(NPC var0) {
        super(NPCUtils.getDummySentient(var0.getLevel()));
        this.entity = var0;
        this.tx = var0.getX();
        this.ty = var0.getY();
        this.tz = var0.getZ();
    }

    @Override
    public boolean hasWanted() {
        return this.moving;
    }

    protected int jumpTicks() {
        return new Random().nextInt(20) + 10;
    }

    @Override
    protected float rotlerp(float f, float f1, float f2) {
        float f3 = Mth.wrapDegrees(f1 - f);
        if (f3 > f2) {
            f3 = f2;
        }
        if (f3 < -f2) {
            f3 = -f2;
        }
        float f4 = f + f3;
        if (f4 < 0.0F)
            f4 += 360.0F;
        else if (f4 > 360.0F) {
            f4 -= 360.0F;
        }
        return f4;
    }

    @Override
    public void setWantedPosition(double d0, double d1, double d2, double d3) {
        this.tx = d0;
        this.ty = d1;
        this.tz = d2;
        this.speed = d3;
        this.moving = true;
    }

    private boolean shouldJump() {
        if (this.jumpTicks-- <= 0) {
            return true;
        }
        return false;
    }

    @Override
    public void tick() {
        this.entity.zza = 0;
        if (this.moving) {
            this.moving = false;
            double dX = this.tx - this.entity.getX();
            double dZ = this.tz - this.entity.getZ();
            double dY = this.ty - this.entity.getY();
            double dXZ = Math.sqrt(dX * dX + dZ * dZ);
            if (Math.abs(dY) < 1.0 && dXZ < 0.01) {
                this.entity.zza = 0.0F;
                return;
            }
            if (dXZ > 0.4) {
                float f = (float) (Mth.atan2(dZ, dX) * 57.2957763671875D) - 90.0F;
                this.entity.setYRot(rotlerp(this.entity.getYRot(), f, 90.0F));
                NMS.setHeadYaw(entity.getBukkitEntity(), this.entity.getYRot());
            }
            AttributeInstance speed = this.entity.getAttribute(Attributes.MOVEMENT_SPEED);
            float movement = (float) (this.speed * speed.getValue());
            this.entity.setSpeed(movement);
            this.entity.zza = movement;
            this.entity.move(MoverType.SELF, new Vec3(dX, dY, dZ));
            //entity.travel(new Vec3(dX, dY, dZ));
            if (shouldJump() || (dY >= NMS.getStepHeight(entity.getBukkitEntity()) && dXZ < 1.0D)) {
                this.jumpTicks = jumpTicks();
                this.jumpTicks /= 3;
                entity.setJumping(true);
            }
        }
    }
}
