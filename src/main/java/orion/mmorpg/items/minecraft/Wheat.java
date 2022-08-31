package orion.mmorpg.items.minecraft;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.utils.Rarity;

import java.util.Collections;

public class Wheat extends BasicItem {

    public Wheat() {
        super(Material.WHEAT, "Blé", "MINECRAFT_WHEAT", 0, Collections.singletonList("Du blé simple"), Rarity.COMMON);
    }
}
