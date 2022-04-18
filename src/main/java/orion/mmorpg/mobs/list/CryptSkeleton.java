package orion.mmorpg.mobs.list;

import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.mobs.samples.SampleHostileMob;
import orion.mmorpg.utils.Rewards;
import orion.mmorpg.utils.Stats;

public class CryptSkeleton extends SampleHostileMob {
    public CryptSkeleton(String uuid, Location location) {
        super("Squelette de la Crypte", 7, EntityTypes.aB, uuid, location, new Stats.Builder().withVitality(20).withStrength(8).withSpeed(6).build());
        this.getRewards().add(new Rewards.Reward(ItemList.BRONZE_COIN.getProcessedItem(1), 1, 1));
    }
}
