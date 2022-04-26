package orion.mmorpg.mobs.samples;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import orion.mmorpg.utils.Stats;

public abstract class SampleBossMob extends SampleMob {
    private Entity lastHitBy;
    public SampleBossMob(String name, int level, EntityTypes<? extends EntityCreature> entitytypes, String uuid, Location location, Stats stats) {
        super(name, level, entitytypes, uuid, location, stats);
    }

    @Override
    public void spawn() {
        Bukkit.broadcastMessage(ChatColor.RED + "Boss " + ChatColor.YELLOW + getName() + ChatColor.RED + " a été invoqué!");
        super.spawn();
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if(damagesource.getEntity() != null)
            if(damagesource.getEntity().getBukkitEntity() instanceof Player)
                lastHitBy = damagesource.getEntity().getBukkitEntity();
        return super.damageEntity(damagesource, f);
    }

    @Override
    public void die(DamageSource damagesource) {
        if(damagesource.getEntity() != null) {
            if(lastHitBy != null)
                Bukkit.broadcastMessage(ChatColor.YELLOW + getName() + ChatColor.RED + " a été vaincu par " + ChatColor.YELLOW + lastHitBy.getName());
            else
                Bukkit.broadcastMessage(ChatColor.YELLOW + getName() + ChatColor.RED + " a été vaincu");
        }
        super.die(damagesource);
    }
}
