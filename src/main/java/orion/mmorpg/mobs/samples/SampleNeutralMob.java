package orion.mmorpg.mobs.samples;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.ai.goal.PathfinderGoalMeleeAttack;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomLookaround;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.entity.player.EntityHuman;
import org.bukkit.Location;
import orion.mmorpg.mobs.pathfinders.MoveTowardsTargetIfRange;
import orion.mmorpg.utils.Stats;

public abstract class SampleNeutralMob extends SampleMob {
    public SampleNeutralMob(String name, int level, EntityTypes<? extends EntityCreature> entitytypes, String uuid, Location location, Stats stats) {
        super(name, level, entitytypes, uuid, location, stats);
        this.color = ChatColor.GREEN;
    }

    @Override
    protected void generatePathfinder() {

        this.bP.a(1, new PathfinderGoalMeleeAttack(this, getStats().getSpeed()/10, true));
        this.bP.a(2, new MoveTowardsTargetIfRange(this, getStats().getSpeed()/10, 10, this.getSpawnLocation()));
        this.bP.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
        this.bP.a(4, new PathfinderGoalRandomLookaround(this));

        this.bQ.a(1, new PathfinderGoalHurtByTarget(this));
    }
}
