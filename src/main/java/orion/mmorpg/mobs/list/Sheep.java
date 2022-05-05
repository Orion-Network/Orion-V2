package orion.mmorpg.mobs.list;

import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.mobs.samples.SampleNeutralMob;
import orion.mmorpg.utils.Rewards;
import orion.mmorpg.utils.Stats;

public class Sheep extends SampleNeutralMob {
    public Sheep(String uuid, Location location) {
        super("Mouton", 2, EntityTypes.ax, uuid, location, new Stats.Builder().withVitality(30).withStrength(2).withSpeed(4).build());
        this.getRewards().add(new Rewards.Reward(ItemList.SHEEP_LAMB.getProcessedItem(1), 80));
    }
}
