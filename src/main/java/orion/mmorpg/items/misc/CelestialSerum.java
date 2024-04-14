package orion.mmorpg.items.misc;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.gui.AcceptGui;
import orion.mmorpg.items.sample.BasicConsumableItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.classes.Classe;
import orion.mmorpg.utils.Rarity;

import java.util.Collections;

public class CelestialSerum extends BasicConsumableItem {
    public CelestialSerum() {
        super(Material.POTION, "Serum Celeste", "CELESTIAL_SERUM", 0,
                Collections.singletonList("Un serum pouvant selon les légendes vous faire atteindre le 7 ème ciel"),
                Rarity.MYTHIC);
    }

    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        super.onUseItem(mmorpg, data, e);
        Classe classe = Classe.CELESTIAL;
        new AcceptGui("Devenir " + classe.getName(), e.getPlayer(), data, () -> {
            data.setClasse(classe);
            classe.getOnRaceSelected().accept(e.getPlayer(), data);
        }, "Vous êtes devenu un(e) " + classe.getName());
    }
}
