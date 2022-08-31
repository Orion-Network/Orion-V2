package orion.mmorpg.mobs.list;

import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import orion.mmorpg.mobs.samples.SampleHostileMob;
import orion.mmorpg.utils.Stats;

public class CryptSkeleton extends SampleHostileMob {
    public CryptSkeleton(String uuid, Location location) {
        super("Squelette de la Crypte", 7, EntityTypes.aB, uuid, location, new Stats.Builder().withVitality(20).withStrength(8).withSpeed(6).build());
        //this.getRewards().add(new Rewards.Reward(ItemList.DAMAGED_BONE.getProcessedItem(1), 70));
        //this.getRewards().add(new Rewards.Reward(ItemList.SKELETON_SKULL.getProcessedItem(1), 1));
    }
}
