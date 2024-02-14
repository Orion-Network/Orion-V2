package orion.mmorpg.items.weapons;

import org.bukkit.Material;
import org.bukkit.event.entity.EntityShootBowEvent;
import orion.mmorpg.items.sample.BasicRangedWeapon;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.Collections;

public class ArcAne extends BasicRangedWeapon {
    public ArcAne() {
        super(Material.BOW, "Arcane", "ARCANE", 0,
                Collections.singletonList("Un arc simple tirant son nom d'une ancienne créature."), Rarity.COMMON,
                new Stats.Modifiers.Builder().add(Stats.StatsInfo.STRENGTH, 2).build(), 2);
    }

    @Override
    public void onShootBow(EntityShootBowEvent e) {
        super.onShootBow(e);
    }
}
