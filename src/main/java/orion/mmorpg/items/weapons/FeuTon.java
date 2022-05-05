package orion.mmorpg.items.weapons;

import org.bukkit.Material;
import org.bukkit.Particle;
import orion.mmorpg.items.sample.BasicMagicWeapon;
import orion.mmorpg.utils.Element;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

public class FeuTon extends BasicMagicWeapon {
    public FeuTon() {
        super(Material.WOODEN_SHOVEL, "FeuTon", "FEUTON", 2, "Un baton en bois, lançant du feu, logique.", Rarity.RARE, new Stats.Builder().withIntel(9).withLuck(3).build(), 7);
        this.ELEMENT = Element.FIRE;
        this.particle = Particle.SMALL_FLAME;
    }
}
