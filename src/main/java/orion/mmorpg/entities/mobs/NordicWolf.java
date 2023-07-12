package orion.mmorpg.entities.mobs;

import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import orion.mmorpg.entities.samples.AbstractHostile;
import orion.mmorpg.items.misc.drops.NordicWolfBone;
import orion.mmorpg.items.misc.drops.NordicWolfFur;
import orion.mmorpg.utils.Rewards;
import orion.mmorpg.utils.Stats;

public class NordicWolf extends AbstractHostile {
    public NordicWolf(String uuid, Location spawnLocation) {
        super(EntityType.WOLF, uuid, "Loup Nordique", spawnLocation, new Stats.Builder().setBase(Stats.StatsInfo.VITALITY, 150).setBase(Stats.StatsInfo.STRENGTH, 17).setBase(Stats.StatsInfo.DEFENCE, 4).setBase(Stats.StatsInfo.SPEED, 5).build(), 13);
        this.getRewards().add(new Rewards.Reward(new NordicWolfFur().generateItem(null, 1), 70));
        this.getRewards().add(new Rewards.Reward(new NordicWolfBone().generateItem(null, 1), 30));
    }
}
