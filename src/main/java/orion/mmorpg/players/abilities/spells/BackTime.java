package orion.mmorpg.players.abilities.spells;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.utils.Element;
import orion.mmorpg.utils.Knowledge;

public class BackTime extends Spell {

    public BackTime() {
        super("BACK_TIME", 30, Element.ARCANE, 3000);
    }

    @Override
    public void launch(Mmorpg mmorpg, Location location, Knowledge knowledge) {
        Location currentLocation = this.getCaster().getLocation();

        Bukkit.getScheduler().runTaskLater(mmorpg, () -> {
            this.getCaster().teleport(currentLocation);
        }, 300);
    }
}
