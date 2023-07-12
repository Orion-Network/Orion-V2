package orion.mmorpg.entities.samples;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Location;
import orion.mmorpg.entities.pathfinders.MoveTowardsTargetIfRange;
import orion.mmorpg.utils.Stats;

public abstract class AbstractNeutral extends AbstractMob {
    public AbstractNeutral(EntityType<? extends PathfinderMob> entitytypes, String uuid, String name, Location spawnLocation, Stats stats, int level) {
        super(entitytypes, uuid, name, spawnLocation, stats, level);

        this.color = ChatColor.GREEN;
    }

    @Override
    protected void generatePathfinder() {

        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, getStats().get(Stats.StatsInfo.SPEED)/10, true));
        this.goalSelector.addGoal(2, new MoveTowardsTargetIfRange(this, getStats().get(Stats.StatsInfo.SPEED)/10, 10, this.getSpawnLocation()));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }
}
