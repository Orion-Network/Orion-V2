package orion.mmorpg.items.minecraft;

import org.bukkit.Material;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.items.sample.BasicMeleeWeapon;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;
import orion.mmorpg.utils.Utils;

public class StoneSword extends BasicMeleeWeapon {
    public StoneSword() {
        super(Material.STONE_SWORD, "Epée en pierre", "MINECRAFT_STONE_SWORD", 0, "Cela suffira", Rarity.COMMON,  new Stats(0,0,0,0,0,0,0,0,0), 5);
    }
}
