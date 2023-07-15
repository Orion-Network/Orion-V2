package orion.mmorpg.entities.samples;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalMeleeAttack;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.player.EntityHuman;
import org.bukkit.Location;
import orion.mmorpg.entities.EntityType;
import orion.mmorpg.entities.Respawnable;
import orion.mmorpg.entities.pathfinders.MeleeAnimationAttack;
import orion.mmorpg.entities.pathfinders.MoveTowardsTargetIfRange;
import orion.mmorpg.managers.ChunkManager;
import orion.mmorpg.utils.Stats;

public abstract class AbstractHostile extends AbstractMob {

    public AbstractHostile(EntityTypes<? extends EntityCreature> entitytypes, String uuid, String name, Location spawnLocation, Stats stats, int level) {
        super(entitytypes, uuid, name, spawnLocation, stats, level);
        this.color = ChatColor.RED;
    }

    @Override
    protected void generatePathfinder() {
        if(this instanceof Modeled)
            this.bP.a(1, new MeleeAnimationAttack(this, 0.25, 0.25, 3, 4, 10));
        else
            this.bP.a(1, new PathfinderGoalMeleeAttack(this, getStats().get(Stats.StatsInfo.SPEED)/10, true));
        this.bP.a(2, new MoveTowardsTargetIfRange(this, getStats().get(Stats.StatsInfo.SPEED)/10, 10, this.getSpawnLocation()));

        this.bQ.a(1, new PathfinderGoalHurtByTarget(this));
        this.bQ.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this, EntityHuman.class, true));
    }
}
