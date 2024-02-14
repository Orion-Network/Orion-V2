package orion.mmorpg.items.sample;

import org.bukkit.Material;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.items.ExcludeRegister;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.List;

@ExcludeRegister
public class BasicRangedWeapon extends BasicMeleeWeapon {
    public BasicRangedWeapon(ItemStack item) {
        super(item);
    }

    public BasicRangedWeapon(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, List<String> LORE,
            Rarity RARITY, String PERMISSION, Stats.Modifiers MODIFIERS, float DAMAGE) {
        super(MATERIAL, NAME, INTERNAL_ID, MODEL_ID, LORE, RARITY, PERMISSION, MODIFIERS, DAMAGE);
    }

    public BasicRangedWeapon(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, List<String> LORE,
            Rarity RARITY, Stats.Modifiers MODIFIERS, float DAMAGE) {
        super(MATERIAL, NAME, INTERNAL_ID, MODEL_ID, LORE, RARITY, MODIFIERS, DAMAGE);
    }

    public void onShootBow(EntityShootBowEvent e) {

    }
}
