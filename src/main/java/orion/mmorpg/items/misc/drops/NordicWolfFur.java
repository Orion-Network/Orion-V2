package orion.mmorpg.items.misc.drops;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.items.sample.BasicRessource;
import orion.mmorpg.utils.Rarity;

import java.util.Collections;

public class NordicWolfFur extends BasicRessource {
    public NordicWolfFur() {
        super(Material.LEATHER, "Peau de loup", "NORDIC_WOLF_FUR", 0, Collections.singletonList("Une peau très résistante au froid"), Rarity.RARE);
    }
}
