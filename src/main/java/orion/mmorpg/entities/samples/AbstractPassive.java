package orion.mmorpg.entities.samples;

import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.EntityHuman;
import org.bukkit.Location;
import orion.mmorpg.entities.EntityType;
import orion.mmorpg.utils.Stats;

public abstract class AbstractPassive extends AbstractMob {
    public AbstractPassive(EntityTypes<? extends EntityCreature> entitytypes, String uuid, String name, Location spawnLocation, Stats stats, int level) {
        super(entitytypes, uuid, name, spawnLocation, stats, level);
    }

    @Override
    protected void generatePathfinder() {

        this.bP.a(0, new PathfinderGoalFloat(this));
        this.bP.a(1, new PathfinderGoalPanic(this, 0.5D));
        this.bP.a(2, new PathfinderGoalRandomStrollLand(this, 0.4D));
        this.bP.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
        this.bP.a(4, new PathfinderGoalRandomLookaround(this));
    }
}
