package orion.mmorpg.items.minecraft;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicMeleeWeapon;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.Collections;

public class StoneSword extends BasicMeleeWeapon {
    public StoneSword() {
        super(Material.STONE_SWORD, "Epée en pierre", "MINECRAFT_STONE_SWORD", 0,
                Collections.singletonList("Cela suffira"), Rarity.COMMON, new Stats.Modifiers(), 5);
    }
}
