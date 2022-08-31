package orion.mmorpg.items.misc;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.gui.AcceptGui;
import orion.mmorpg.items.sample.BasicConsumableItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.races.Race;
import orion.mmorpg.utils.Rarity;

import java.util.Collections;

public class CelestialSerum extends BasicConsumableItem {
    public CelestialSerum() {
        super(Material.POTION, "Serum Celeste", "CELESTIAL_SERUM", 0, Collections.singletonList("Un serum pouvant selon les légendes vous faire atteindre le 7 ème ciel"), Rarity.MYTHIC);
    }

    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        super.onUseItem(mmorpg, data, e);
        Race race = Race.CELESTIAL;
        new AcceptGui("Devenir " + race.getName(), e.getPlayer(), data, () -> {
            data.setRace(race);
            race.getOnRaceSelected().accept(e.getPlayer(), data);
        }, "Vous êtes devenu un(e) " + race.getName());
    }
}
