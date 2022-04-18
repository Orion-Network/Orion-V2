package orion.mmorpg.mobs.list;

import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.mobs.samples.SampleHostileMob;
import orion.mmorpg.utils.Rewards;
import orion.mmorpg.utils.Stats;

public class AngryChicken extends SampleHostileMob {
    public AngryChicken(String uuid, Location location) {
        super("Poulet énervé", 1, EntityTypes.l, uuid, location, new Stats.Builder().withVitality(20).withStrength(4).withSpeed(4).build());
        this.getRewards().setItemNumber(2);
        this.getRewards().add(new Rewards.Reward(ItemList.DRY_WHEAT.getProcessedItem(1), 2,2));
        this.getRewards().add(new Rewards.Reward(ItemList.WHEAT_SEEDS.getProcessedItem(1), 1,1));
    }
}
