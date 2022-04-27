package orion.mmorpg.players.abilities.spells;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.utils.Element;

public class Heal extends Spell {
    public Heal() {
        super("HEAL", 1, Element.WHITE, 1000 * 60);
    }

    @Override
    public void launch(Mmorpg mmorpg, Location location) {
        if(getCaster() instanceof Player) {
            Player player = (Player) getCaster();
            OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(player.getUniqueId());
            orionPlayer.heal(orionPlayer.getStats().getVitality() / 10);
        }
    }
}
