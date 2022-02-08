package orion.mmorpg.items.weapons;

import org.bukkit.Material;
import org.bukkit.event.entity.EntityShootBowEvent;
import orion.mmorpg.items.sample.BasicRangedWeapon;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

public class ArcAne extends BasicRangedWeapon {
    public ArcAne() {
        super(Material.BOW, "Arcane", "ARCANE", 0, "Un arc magique ?", Rarity.COMMON, new Stats.Builder().withStrength(2).build(), 2);
    }

    @Override
    public void onShootBow(EntityShootBowEvent e) {
        super.onShootBow(e);
    }
}
