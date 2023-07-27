package orion.mmorpg.entities.samples;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.mount.controller.walking.WalkingMountController;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import orion.mmorpg.utils.Stats;

//TODO See if mounts are compatible with custom models
public abstract class AbstractMount extends AbstractEntityBase {
    double forwardMultiplier = 0.2, strafeMultiplier = 0.3, jumpForce = 0.5;
    final Player owner;
    final MountType type;

    public AbstractMount(EntityType<? extends PathfinderMob> entitytypes, String uuid, String name, Location spawnLocation, Stats stats, Player owner, MountType type) {
        super(entitytypes, uuid, name, spawnLocation, stats);
        this.owner = owner;
        this.type = type;
    }

    @Override
    public void generatePathfinder() {
    }

    @Override
    protected InteractionResult mobInteract(net.minecraft.world.entity.player.Player entityhuman, InteractionHand enumhand) {
        //check if the player is the owner
        if(!entityhuman.getUUID().equals(owner.getUniqueId()))
            return super.mobInteract(entityhuman, enumhand);
        entityhuman.startRiding(this);
        return super.mobInteract(entityhuman, enumhand);
    }

    @Override
    public boolean hurt(DamageSource damagesource, float f) {
        return super.hurt(damagesource, f);
    }

    //TODO handle dismount of modeled entity to prevent glitching
    @Override
    public void spawn() {
        super.spawn();
        if(this instanceof Modeled) {
            WalkingMountController controller = (WalkingMountController) ModelEngineAPI.getControllerRegistry().get("");
            ((Modeled) this).getModeledEntity().getMountManager().setDriver(getOwner(), controller);
        } else {
            ((CraftPlayer) owner).getHandle().startRiding(this);
        }
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

    public Player getOwner() {
        return owner;
    }

    public MountType getMountType() {
        return this.type;
    }

    public enum MountType {
        LAND, WATER, AIR;
    }
}
