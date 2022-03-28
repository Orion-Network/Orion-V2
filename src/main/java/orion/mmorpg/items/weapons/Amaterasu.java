package orion.mmorpg.items.weapons;

import de.slikey.effectlib.Effect;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.effects.SlashEffect;
import orion.mmorpg.items.sample.BasicMeleeWeapon;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

public class Amaterasu extends BasicMeleeWeapon {
    public Amaterasu() {
        super(Material.NETHERITE_SWORD, "Amaterasu", "AMATERASU", 3, "No lore", Rarity.SUPERRARE, new Stats.Builder().withStrength(7).withSpeed(5).withCritical_chance(20).withCritical_damage(10).build(), 15);
    }

    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        //TODO Make slash effect
        Effect slashEffect = new SlashEffect(Mmorpg.effectManager);
        slashEffect.setLocation(e.getPlayer().getLocation());
        slashEffect.setTargetLocation(e.getPlayer().getLocation().clone().add(10,0,10));
        slashEffect.start();
        super.onUseItem(mmorpg, data, e);
    }
}
