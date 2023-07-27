package orion.mmorpg.utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Tameable;

public class NMS {
    public static void setHeadYaw(org.bukkit.entity.Entity entity, float yaw) {
        if (!(entity instanceof org.bukkit.entity.LivingEntity))
            return;
        LivingEntity handle = (LivingEntity) getHandle(entity);
        yaw = Utils.clamp(yaw);
        handle.yBodyRotO = yaw;
        if (!(handle instanceof net.minecraft.world.entity.player.Player)) {
            handle.setYBodyRot(yaw);
        }
        handle.setYHeadRot(yaw);
    }

    public static Entity getHandle(org.bukkit.entity.Entity entity) {
        if (!(entity instanceof CraftEntity))
            return null;
        return ((CraftEntity) entity).getHandle();
    }

    private static LivingEntity getHandle(org.bukkit.entity.LivingEntity entity) {
        return (LivingEntity) getHandle((org.bukkit.entity.Entity) entity);
    }

    private static LivingEntity getHandle(Tameable entity) {
        return (LivingEntity) getHandle((org.bukkit.entity.Entity) entity);
    }

    public static float getStepHeight(org.bukkit.entity.Entity entity) {
        return getHandle(entity).maxUpStep();
    }

}
