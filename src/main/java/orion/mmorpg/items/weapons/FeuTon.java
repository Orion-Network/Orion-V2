package orion.mmorpg.items.weapons;

import orion.mmorpg.config.AssociationConfig;
import org.bukkit.Material;
import org.bukkit.Particle;
import orion.mmorpg.items.sample.BasicMagicWeapon;
import orion.mmorpg.utils.Element;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.Collections;

public class FeuTon extends BasicMagicWeapon {
    public FeuTon() {
        super(Material.WOODEN_SHOVEL, "FeuTon", "FEUTON", AssociationConfig.getModel("weapon/staff/brillant_staff"), Collections.singletonList("Un baton en bois, lançant du feu, logique."), Rarity.RARE, new Stats.Modifiers.Builder().add(Stats.StatsInfo.INTELLIGENCE, 9).add(Stats.StatsInfo.LUCK, 3).build(), 7);
        this.ELEMENT = Element.FIRE;
        this.particle = Particle.SMALL_FLAME;
    }
}
