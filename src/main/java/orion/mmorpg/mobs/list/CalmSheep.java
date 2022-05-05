package orion.mmorpg.mobs.list;

import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.mobs.samples.SamplePassiveMob;
import orion.mmorpg.utils.Rewards;
import orion.mmorpg.utils.Stats;

public class CalmSheep extends SamplePassiveMob {
    public CalmSheep(String uuid, Location location) {
        super("Mouton Calme", 1, EntityTypes.ax, uuid, location, new Stats.Builder().withVitality(20).withSpeed(4).build());
        this.getRewards().add(new Rewards.Reward(ItemList.SHEEP_LAMB.getProcessedItem(1), 40));
    }
}
