package orion.mmorpg.items.misc;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicPotion;
import orion.mmorpg.utils.Rarity;

import java.util.ArrayList;

public class Potion extends BasicPotion {
    public Potion() {
        super("Potion", "BASIC_POTION", 0, new ArrayList<>(), Rarity.COMMON, 10);

    }
}
