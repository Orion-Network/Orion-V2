package orion.mmorpg.mobs.samples;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalMeleeAttack;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.player.EntityHuman;
import org.bukkit.Location;
import orion.mmorpg.mobs.pathfinders.MoveTowardsTargetIfRange;
import orion.mmorpg.utils.Stats;

public abstract class SampleHostileMob extends SampleMob {
    public SampleHostileMob(String name, int level, EntityTypes<? extends EntityCreature> entitytypes, String uuid, Location location, Stats stats) {
        super(name, level, entitytypes, uuid, location, stats);
        this.color = ChatColor.RED;
    }

    @Override
    protected void generatePathfinder() {
        this.bP.a(1, new PathfinderGoalMeleeAttack(this, getStats().getSpeed()/10, true));
        this.bP.a(2, new MoveTowardsTargetIfRange(this, getStats().getSpeed()/10, 10, this.getSpawnLocation()));

        this.bQ.a(1, new PathfinderGoalHurtByTarget(this));
        this.bQ.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this, EntityHuman.class, true));
    }
}
