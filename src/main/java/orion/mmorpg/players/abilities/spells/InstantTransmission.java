package orion.mmorpg.players.abilities.spells;

import de.slikey.effectlib.effect.WarpEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.utils.Element;
import orion.mmorpg.utils.Knowledge;

public class InstantTransmission extends Spell {
    public InstantTransmission() {
        super("INSTANT_TRANSMISSION", 30, Element.ARCANE, 15 * 1000);
    }

    @Override
    public void launch(Mmorpg mmorpg, Location location, Knowledge knowledge) {
        WarpEffect startEffect = new WarpEffect(Mmorpg.effectManager);
        startEffect.particle = Particle.PORTAL;
        startEffect.duration = 500;
        WarpEffect endEffect = new WarpEffect(Mmorpg.effectManager);
        endEffect.particle = Particle.PORTAL;
        endEffect.duration = 500;
        if(getCaster() != null && getCaster() instanceof Player) {
            Player player = (Player) getCaster();

            Vector v = player.getEyeLocation().getDirection().normalize().multiply(8);
            Location l = player.getLocation().add(v);

            RayTraceResult rayTrace = player.getWorld().rayTraceBlocks(player.getEyeLocation(), player.getEyeLocation().getDirection(), 8);

            if (l.getBlock().isEmpty() && l.add(0, 1, 0).getBlock().isEmpty() && rayTrace == null) {
                startEffect.setLocation(getCaster().getLocation());
                startEffect.start();
                player.teleport(l);
                endEffect.setLocation(l);
                endEffect.start();
            }
        } else if(getTarget() != null && getTarget() instanceof Player) {
            Player player = (Player) getTarget();
            player.teleport(location);
        }
    }
}
