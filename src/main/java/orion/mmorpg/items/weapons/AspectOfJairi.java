package orion.mmorpg.items.weapons;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.sample.BasicMeleeWeapon;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.spells.InstantTransmission;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.Collections;

public class AspectOfJairi extends BasicMeleeWeapon {
    public AspectOfJairi() {
        super(Material.IRON_SWORD, "Aspect of Jairi", "ASPECT_OF_JAIRI", 0,
                Collections.singletonList("Une épée intrigante d'un autre univers"), Rarity.RARE, new Stats.Modifiers(),
                1);
        this.spell = new InstantTransmission();
    }

    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        data.getCooldownSpells().remove(spell.getInternal_id());
        super.onUseItem(mmorpg, data, e);
    }
}
