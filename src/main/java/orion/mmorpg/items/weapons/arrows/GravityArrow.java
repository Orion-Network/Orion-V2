package orion.mmorpg.items.weapons.arrows;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicArrow;
import orion.mmorpg.players.abilities.spells.FullGravity;
import orion.mmorpg.utils.Rarity;

public class GravityArrow extends BasicArrow {
    public GravityArrow() {
        super(Material.ARROW, "Gravity Arrow", "GRAVITY_ARROW", 0, null, Rarity.RARE, 5);
        this.spell = new FullGravity();
    }
}
