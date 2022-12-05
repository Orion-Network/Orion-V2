package orion.mmorpg.items.weapons;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityShootBowEvent;
import orion.mmorpg.items.sample.BasicRangedWeapon;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.Collections;

public class HouYi extends BasicRangedWeapon {
    public HouYi() {
        super(Material.BOW, "Arc d'Hou Yi", "HOUYI_BOW", 1, Collections.singletonList("Un arc divin pouvant éteindre les soleils"), Rarity.MYTHIC, new Stats.Builder().withStrength(20).build(), 75);
    }

    @Override
    public void onShootBow(EntityShootBowEvent e) {
        e.getProjectile().setVelocity(e.getProjectile().getVelocity().multiply(10));
        Bukkit.broadcastMessage("test");
        super.onShootBow(e);
    }
}
