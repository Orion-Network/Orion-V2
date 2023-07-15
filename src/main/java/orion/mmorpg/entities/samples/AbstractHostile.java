package orion.mmorpg.entities.samples;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Location;
import orion.mmorpg.entities.pathfinders.MeleeAnimationAttack;
import orion.mmorpg.entities.pathfinders.MoveTowardsTargetIfRange;
import orion.mmorpg.utils.Stats;

public abstract class AbstractHostile extends AbstractMob {

    public AbstractHostile(EntityType<? extends PathfinderMob> entitytypes, String uuid, String name, Location spawnLocation, Stats stats, int level) {
        super(entitytypes, uuid, name, spawnLocation, stats, level);
        this.color = ChatColor.RED;
    }

    @Override
    protected void generatePathfinder() {
        if(this instanceof Modeled)
            this.goalSelector.addGoal(1, new MeleeAnimationAttack(this, 0.25, 0.25, 3, 4, 10));
        else
            this.goalSelector.addGoal(1, new MeleeAttackGoal(this, getStats().get(Stats.StatsInfo.SPEED)/10, true));
        this.goalSelector.addGoal(2, new MoveTowardsTargetIfRange(this, getStats().get(Stats.StatsInfo.SPEED)/10, 10, this.getSpawnLocation()));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<Player>(this, Player.class, true));
    }
}
