package orion.mmorpg.players.abilities.spells;

import de.slikey.effectlib.effect.SphereEffect;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.utils.Element;
import orion.mmorpg.utils.Knowledge;
import orion.mmorpg.utils.Utils;

public class Attraction extends Spell {
    public Attraction() {
        super("ATTRACTION", 30, Element.ARCANE, 15*1000, TargetType.GLOBAL, 5);
    }

    @Override
    public void launch(Mmorpg mmorpg, Location location, Knowledge knowledge) {
        getTargets().forEach(target -> {
            if(target instanceof Player)
                if(!Utils.canPvp(target))
                    return;
            SphereEffect sphereEffect = new SphereEffect(Mmorpg.effectManager);
            sphereEffect.particle = Particle.FIREWORKS_SPARK;
            sphereEffect.color = Color.PURPLE;
            sphereEffect.duration = 200;
            sphereEffect.radius = 1;
            sphereEffect.radiusIncrease = -1;
            sphereEffect.setLocation(location);
            sphereEffect.start();
            target.setVelocity(target.getLocation().toVector().subtract(location.toVector()).normalize().multiply(-5));
        });
    }
}
