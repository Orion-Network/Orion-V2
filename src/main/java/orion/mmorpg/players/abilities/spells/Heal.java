package orion.mmorpg.players.abilities.spells;

import de.slikey.effectlib.effect.CylinderEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.utils.Element;
import orion.mmorpg.utils.Knowledge;

public class Heal extends Spell {
    public Heal() {
        super("HEAL", 1, Element.WHITE, 1000 * 60);
    }

    @Override
    public void launch(Mmorpg mmorpg, Location location, Knowledge knowledge) {
        if(getCaster() instanceof Player) {
            Player player = (Player) getCaster();
            OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(player.getUniqueId());

            CylinderEffect cylinderEffect = new CylinderEffect(Mmorpg.effectManager);
            cylinderEffect.radius = 2;
            cylinderEffect.particle = Particle.HEART;
            cylinderEffect.duration = 50;
            cylinderEffect.setLocation(location);
            cylinderEffect.start();
            orionPlayer.heal(orionPlayer.getStats().getVitality() / 10);
        }
    }
}
