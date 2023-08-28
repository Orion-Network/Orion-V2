package orion.mmorpg.entities.mobs;

import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import orion.mmorpg.entities.samples.AbstractHostile;
import orion.mmorpg.utils.Rewards;
import orion.mmorpg.utils.Stats;

public class CryptSkeleton extends AbstractHostile {

    public CryptSkeleton(String uuid, Location spawnLocation) {
        super(EntityType.SKELETON, uuid, "Squelette de la Crypt", spawnLocation,  new Stats.Builder().setBase(Stats.StatsInfo.VITALITY, 20).setBase(Stats.StatsInfo.STRENGTH, 8).setBase(Stats.StatsInfo.SPEED, 6).build(), 7);

        Rewards.Reward money = new Rewards.Reward(null, 100);
        money.getMoneys().put("gold", 15L);
        this.getRewards().add(money);
    }
}
