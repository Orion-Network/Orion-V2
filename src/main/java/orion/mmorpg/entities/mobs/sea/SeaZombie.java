package orion.mmorpg.entities.mobs.sea;

import net.minecraft.world.entity.EntityTypes;
import orion.mmorpg.entities.samples.AbstractHostile;
import orion.mmorpg.players.jobs.JobType;
import orion.mmorpg.utils.Rewards;
import orion.mmorpg.utils.Stats;
import org.bukkit.Location;
//TODO remove back to spawn pathfinder

public class SeaZombie extends AbstractHostile {
    public SeaZombie(String uuid, Location spawnLocation) {
        super(EntityTypes.s, uuid, "Zombie marin", spawnLocation,
                new Stats.Builder().setBase(Stats.StatsInfo.VITALITY, 200).setBase(Stats.StatsInfo.STRENGTH, 4).setBase(Stats.StatsInfo.SPEED, 4).build(), 3);

        Rewards.Reward reward = new Rewards.Reward(null, 100);
        reward.getJobExp().put(JobType.FISHING, 25);
        this.getRewards().add(reward);
    }
}
