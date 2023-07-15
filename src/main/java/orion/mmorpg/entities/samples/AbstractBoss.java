package orion.mmorpg.entities.samples;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import orion.mmorpg.events.BossKilledEvent;
import orion.mmorpg.utils.Stats;

public abstract class AbstractBoss extends AbstractMob {
    private Entity lastHitBy;

    public AbstractBoss(EntityType<? extends PathfinderMob> entitytypes, String uuid, String name, Location spawnLocation, Stats stats, int level) {
        super(entitytypes, uuid, name, spawnLocation, stats, level);
    }

    @Override
    public void spawn() {
        Bukkit.broadcastMessage(ChatColor.YELLOW + getName().getString() + ChatColor.RED + " a été invoqué!");
        super.spawn();
    }

    @Override
    public boolean hurt(DamageSource damagesource, float f) {
        if(damagesource.getEntity() != null)
            if(damagesource.getEntity().getBukkitEntity() instanceof Player)
                lastHitBy = damagesource.getEntity().getBukkitEntity();
        return super.hurt(damagesource, f);
    }

    @Override
    public void die(DamageSource damagesource) {
        if(damagesource.getEntity() != null) {
            Bukkit.getServer().getPluginManager().callEvent(new BossKilledEvent(this, (Player) lastHitBy));
        }
        super.die(damagesource);
    }
}
