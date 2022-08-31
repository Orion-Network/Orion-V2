package orion.mmorpg.mobs.list;

import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import orion.mmorpg.items.misc.drops.NordicWolfBone;
import orion.mmorpg.items.misc.drops.NordicWolfFur;
import orion.mmorpg.mobs.samples.SampleHostileMob;
import orion.mmorpg.utils.Rewards;
import orion.mmorpg.utils.Stats;

public class NordicWolf extends SampleHostileMob {
    public NordicWolf(String uuid, Location location) {
        super("Loup Nordique", 13, EntityTypes.bc, uuid, location, new Stats.Builder().withVitality(150).withStrength(17).withDefence(4).withSpeed(5).build());
        this.getRewards().add(new Rewards.Reward(new NordicWolfFur().generateItem(null, 1), 70));
        this.getRewards().add(new Rewards.Reward(new NordicWolfBone().generateItem(null, 1), 30));
    }
}
