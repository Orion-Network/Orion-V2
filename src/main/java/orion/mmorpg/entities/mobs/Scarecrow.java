package orion.mmorpg.entities.mobs;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.player.EntityHuman;
import org.bukkit.Location;
import orion.mmorpg.entities.pathfinders.MeleeAnimationAttack;
import orion.mmorpg.entities.pathfinders.MoveTowardsTargetIfRange;
import orion.mmorpg.entities.samples.AbstractHostile;
import orion.mmorpg.entities.samples.AbstractModeledMob;
import orion.mmorpg.utils.Stats;

public class Scarecrow extends AbstractModeledMob {

    public Scarecrow(String uuid, Location spawnLocation) {
        super("kindletronsr", uuid, "Scarecrow", spawnLocation, new Stats.Builder().withVitality(30).withStrength(2).withSpeed(4).build(), 2);
    }
    @Override
    protected void generatePathfinder() {

        //If reach is too low it will not attack and just stay still
        this.bP.a(1, new MeleeAnimationAttack(this, 0.25, 0.25, 3, 4, 10));
        //Doesn't move towards target only go back to spawn location if no target
        this.bP.a(2, new MoveTowardsTargetIfRange(this, getStats().getSpeed()/10, 10, this.getSpawnLocation()));

        this.bQ.a(1, new PathfinderGoalHurtByTarget(this));
        this.bQ.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this, EntityHuman.class, true));
    }
}
