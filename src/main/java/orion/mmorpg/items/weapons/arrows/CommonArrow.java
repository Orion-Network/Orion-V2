package orion.mmorpg.items.weapons.arrows;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicArrow;
import orion.mmorpg.utils.Rarity;

public class CommonArrow extends BasicArrow {
    public CommonArrow() {
        super(Material.ARROW, "Flèche Commune", "COMMON_ARROW", 1, "Des flèches simple", Rarity.COMMON, 3);
    }
}
