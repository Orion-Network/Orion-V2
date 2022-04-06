package orion.mmorpg.mobs.list;

import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import orion.mmorpg.mobs.SampleMob;

public class AngryChicken extends SampleMob {
    public AngryChicken(Location location) {
        super(EntityTypes.l, location);
        this.setPosition(location.getX(), location.getY(), location.getZ());
        ((LivingEntity) this.getBukkitEntity()).setRemoveWhenFarAway(false);
    }

}
