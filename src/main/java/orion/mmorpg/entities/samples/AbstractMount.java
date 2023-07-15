package orion.mmorpg.entities.samples;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.mount.MountControllerRegistry;
import com.ticxo.modelengine.api.mount.MountManager;
import com.ticxo.modelengine.api.mount.controller.walking.WalkingMountController;
import net.minecraft.world.EnumHand;
import net.minecraft.world.EnumInteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.horse.EntityHorse;
import net.minecraft.world.entity.animal.horse.EntityHorseAbstract;
import net.minecraft.world.entity.player.EntityHuman;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import orion.mmorpg.entities.EntityType;
import orion.mmorpg.utils.Stats;

//TODO See if mounts are compatible with custom models
public abstract class AbstractMount extends AbstractEntityBase {
    double forwardMultiplier = 0.2, strafeMultiplier = 0.3, jumpForce = 0.5;
    final Player owner;
    final MountType type;

    public AbstractMount(EntityTypes<? extends EntityCreature> entitytypes, String uuid, String name, Location spawnLocation, Stats stats, Player owner, MountType type) {
        super(entitytypes, uuid, name, spawnLocation, stats);
        this.owner = owner;
        this.type = type;
    }

    @Override
    public void generatePathfinder() {
    }

    @Override
    protected EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) {
        //check if the player is the owner
        if(!entityhuman.getUniqueID().equals(owner.getUniqueId()))
            return super.b(entityhuman, enumhand);
        entityhuman.startRiding(this);
        return super.b(entityhuman, enumhand);
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if(damagesource.equals(DamageSource.k))
            return false;
        return super.damageEntity(damagesource, f);
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

    public MountType getType() {
        return type;
    }

    public enum MountType {
        LAND, WATER, AIR;
    }
}
