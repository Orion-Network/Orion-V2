package orion.mmorpg.players.abilities.spells;

import de.slikey.effectlib.effect.LineEffect;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.utils.Element;
import orion.mmorpg.utils.Knowledge;
import orion.mmorpg.utils.Utils;

import java.awt.*;

public class ChannelStrike extends Spell {
    public ChannelStrike() {
        super("CHANNEL_STRIKE", 10, Element.ELECTRIC, 10*1000);
    }

    @Override
    public void launch(Mmorpg mmorpg, Location location, Knowledge knowledge) {

        LivingEntity previousTarget = null;
        LivingEntity target = Utils.getTargetInFront((LivingEntity) getCaster(), 20);
        if(target == null)
            return;

        int count = 0;
        int max = 5;

        while(count < max) {
            strike(target, (int) (getOrionCaster().getStats().getIntel()/10));
            //Draw LineEffect between previous and current target
            if(previousTarget != null) {
                LineEffect lineEffect = new LineEffect(Mmorpg.effectManager);
                lineEffect.particle = Particle.REDSTONE;
                lineEffect.color = Color.YELLOW;
                lineEffect.duration = 200;
                lineEffect.setLocation(previousTarget.getLocation());
                lineEffect.setTargetLocation(target.getLocation());
                lineEffect.start();
            }

            previousTarget = target;
            target = getNextTarget(target);
            if(target == null)
                break;
            count++;
        }
    }

    public LivingEntity getNextTarget(LivingEntity previous ) {
        return previous.getNearbyEntities(10, 10, 10).stream()
                .filter(entity -> entity instanceof LivingEntity)
                .filter(entity -> entity != previous)
                .filter(entity -> entity != getCaster())
                .map(entity -> (LivingEntity) entity)
                .findFirst()
                .orElse(null);
    }

    public void strike(LivingEntity target, int power) {
        target.damage(power);

        Utils.sendInfoMessage(getCaster(), "You strike " + target.getName() + " for " + power + " damage.");
    }
}
