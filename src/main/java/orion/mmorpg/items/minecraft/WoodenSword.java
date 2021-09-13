package orion.mmorpg.items.minecraft;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicMeleeWeapon;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

public class WoodenSword extends BasicMeleeWeapon {
    public WoodenSword() {
        super(Material.WOODEN_SWORD, "Epée en bois", "MINECRAFT_WOODEN_SWORD", 0, null, Rarity.COMMON,  new Stats(0,0,0,0,0,0,0,0,0), 3);
    }
}
