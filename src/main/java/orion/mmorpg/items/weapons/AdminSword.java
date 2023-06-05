package orion.mmorpg.items.weapons;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.player.PlayerInteractEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.sample.BasicMeleeWeapon;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.Collections;

public class AdminSword extends BasicMeleeWeapon {
    public AdminSword() {
        super(Material.NETHERITE_SWORD, "Admin Sword", "ADMIN_SWORD", 0, Collections.singletonList("You shall not have this !"), Rarity.UNOBTAINABLE, "admin.use",
                new Stats.Modifiers.Builder().add(Stats.StatsInfo.STRENGTH, 10000).add(Stats.StatsInfo.CRITICAL_CHANCE, 95).build(), 200);
    }

    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        e.getPlayer().sendMessage(e.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue() + "" + e.getPlayer().getAttackCooldown());
    }
}
