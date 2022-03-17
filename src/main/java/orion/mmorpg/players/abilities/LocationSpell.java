package orion.mmorpg.players.abilities;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;

public interface LocationSpell {
    void launchFromLocation(Mmorpg mmorpg, Player player, OrionPlayer orionPlayer, Location location);
}
