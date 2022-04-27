package orion.mmorpg.players.abilities.spells;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.utils.Element;

public class InstantTransmission extends Spell {
    public InstantTransmission() {
        super("INSTANT_TRANSMISSION", 30, Element.ARCANE, 15 * 1000);
    }

    @Override
    public void launch(Mmorpg mmorpg, Location location) {
        if(getCaster() != null && getCaster() instanceof Player) {
            Player player = (Player) getCaster();
            Vector v = player.getEyeLocation().getDirection().normalize().multiply(8);
            Location l = player.getLocation().add(v);

            RayTraceResult rayTrace = player.getWorld().rayTraceBlocks(player.getEyeLocation(), player.getEyeLocation().getDirection(), 8);

            if (l.getBlock().isEmpty() && l.add(0, 1, 0).getBlock().isEmpty() && rayTrace == null)
                player.teleport(l);
        } else if(getTarget() != null && getTarget() instanceof Player) {
            Player player = (Player) getTarget();
            player.teleport(location);
        }
    }
}
