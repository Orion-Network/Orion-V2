package orion.mmorpg.players.abilities.spells;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.utils.Element;

public class InstantTransmission extends Spell {
    public InstantTransmission() {
        super("INSTANT_TRANSMISSION", 30, Element.ARCANE, 15*1000);
    }

    @Override
    public void launch(Mmorpg mmorpg, Player player, OrionPlayer orionPlayer) {
        Vector v = player.getEyeLocation().getDirection().normalize().multiply(8);
        Location l = player.getLocation().add(v);
        if(l.getBlock().isEmpty() && l.add(0,1,0).getBlock().isEmpty())
            player.teleport(l);
    }
}
