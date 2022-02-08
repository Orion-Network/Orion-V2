package orion.mmorpg.items.weapons;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicMagicWeapon;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

public class BahTon extends BasicMagicWeapon {
    public BahTon() {
        super(Material.IRON_SHOVEL, "BahTon", "BAHTON", 0, "Un baton simple avec peu de magie", Rarity.COMMON, new Stats.Builder().withIntel(2).build(), 5);
    }
}
