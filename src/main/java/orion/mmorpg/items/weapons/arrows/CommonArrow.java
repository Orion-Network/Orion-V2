package orion.mmorpg.items.weapons.arrows;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicArrow;
import orion.mmorpg.utils.Rarity;

import java.util.Collections;

public class CommonArrow extends BasicArrow {
    public CommonArrow() {
        super(Material.ARROW, "Flèche Commune", "COMMON_ARROW", 1, Collections.singletonList("Des flèches simple"), Rarity.COMMON, 3);
    }
}
