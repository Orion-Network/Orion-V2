package orion.mmorpg.items.misc.drops;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.utils.Rarity;

import java.util.Collections;

public class NordicWolfBone extends BasicItem {
    public NordicWolfBone() {
        super(Material.BONE, "Od de loup nordique", "NORDIC_WOLF_FUR", 0, Collections.singletonList("Un os de loup, il est gelé"), Rarity.RARE);
    }
}
