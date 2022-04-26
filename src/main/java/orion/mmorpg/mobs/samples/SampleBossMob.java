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
    protected void d(DamageSource damagesource) {
        super.d(damagesource);
        if(damagesource.getEntity() != null)
            if(damagesource.getEntity().getBukkitEntity() instanceof Player)
                lastHitBy = damagesource.getEntity().getBukkitEntity();

    }

    

    @Override
    public void die(DamageSource damagesource) {
        if(damagesource.getEntity() != null) {
            Bukkit.broadcastMessage(ChatColor.YELLOW + getName() + ChatColor.RED + " a été vaincu par " + ChatColor.YELLOW + lastHitBy.getName());
        }
        super.die(damagesource);
    }
}
