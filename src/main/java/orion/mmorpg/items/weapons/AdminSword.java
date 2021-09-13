package orion.mmorpg.items.weapons;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicMeleeWeapon;
import orion.mmorpg.players.abilities.list.TestSpell;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

public class AdminSword extends BasicMeleeWeapon {
    public AdminSword() {
        super(Material.NETHERITE_SWORD, "Admin Sword", "ADMIN_SWORD", 0, "You shall not have this !", Rarity.UNOBTAINABLE, "admin.use",
                new Stats(0,100,15,0,0,0,0,0,0), 200);
        this.spell = new TestSpell();
    }
}
