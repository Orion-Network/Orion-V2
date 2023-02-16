package orion.mmorpg.players.abilities.spells;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.utils.Element;
import orion.mmorpg.utils.Knowledge;

public class Levitae extends Spell {
    public Levitae() {
        super("LEVITAE", 10, Element.ARCANE, 30*1000, TargetType.SELF);
    }

    @Override
    public void launch(Mmorpg mmorpg, Location location, Knowledge knowledge) {
        getTargets().getFirst().addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 10*20, 0));

    }
}
