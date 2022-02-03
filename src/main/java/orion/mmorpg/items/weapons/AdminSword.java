package orion.mmorpg.items.weapons;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.player.PlayerInteractEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.sample.BasicMeleeWeapon;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.spells.GoatHunger;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

public class AdminSword extends BasicMeleeWeapon {
    public AdminSword() {
        super(Material.NETHERITE_SWORD, "Admin Sword", "ADMIN_SWORD", 0, "You shall not have this !", Rarity.UNOBTAINABLE, "admin.use",
                new Stats(1,100,15,1,1,1,1,1,1), 200);
    }

    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        e.getPlayer().sendMessage(e.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue() + "" + e.getPlayer().getAttackCooldown());
    }
}
