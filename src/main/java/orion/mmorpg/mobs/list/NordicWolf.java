package orion.mmorpg.mobs.list;

import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.mobs.samples.SampleHostileMob;
import orion.mmorpg.utils.Rewards;
import orion.mmorpg.utils.Stats;

public class NordicWolf extends SampleHostileMob {
    public NordicWolf(String uuid, Location location) {
        super("Loup Nordique", 15, EntityTypes.bc, uuid, location, new Stats.Builder().withVitality(200).withStrength(23).withDefence(4).withSpeed(6).build());
        this.getRewards().setItemNumber(2);
        this.getRewards().add(new Rewards.Reward(ItemList.NORDIC_WOLF_FUR.getProcessedItem(1), 1, 2));
        this.getRewards().add(new Rewards.Reward(ItemList.NORDIC_WOLF_BONE.getProcessedItem(1), 1, 1));
    }
}
