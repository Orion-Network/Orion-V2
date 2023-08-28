package orion.mmorpg.entities.mobs;

import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import orion.mmorpg.entities.samples.AbstractHostile;
import orion.mmorpg.utils.Rewards;
import orion.mmorpg.utils.Stats;

public class MountainZombie extends AbstractHostile {
    public MountainZombie(String uuid, Location spawnLocation) {
        super(EntityType.ZOMBIE, uuid, "Zombie Montagnard", spawnLocation,  new Stats.Builder().setBase(Stats.StatsInfo.VITALITY, 40)
                .setBase(Stats.StatsInfo.STRENGTH, 7)
                .setBase(Stats.StatsInfo.DEFENCE,2)
                .setBase(Stats.StatsInfo.SPEED, 4)
                .build(), 5);

        Rewards.Reward money = new Rewards.Reward(null, 100);
        money.getMoneys().put("gold", 10L);
        this.getRewards().add(money);
    }
}
