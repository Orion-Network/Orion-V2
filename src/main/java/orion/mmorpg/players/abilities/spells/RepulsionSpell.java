package orion.mmorpg.players.abilities.spells;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.LocationSpell;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.utils.Element;
import orion.mmorpg.utils.Utils;

public class RepulsionSpell extends Spell implements LocationSpell {

    public RepulsionSpell() {
        super("REPULSION", 30, Element.ARCANE, 15*1000);
    }

    @Override
    public void launch(Mmorpg mmorpg, Player player, OrionPlayer orionPlayer) {
        player.getNearbyEntities(5, 5, 5).forEach(entity -> {
            if(entity instanceof Player)
                if(!Utils.canPvp(entity))
                    return;
            entity.setVelocity(entity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(5));
        });
    }

    @Override
    public void launchFromLocation(Mmorpg mmorpg, Player player, OrionPlayer orionPlayer, Location location) {
        location.getWorld().getNearbyEntities(location, 5, 5, 5).forEach(entity -> {
            entity.setVelocity(entity.getLocation().toVector().subtract(location.toVector()).normalize().multiply(5));
        });
    }
}
