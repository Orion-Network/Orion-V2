package orion.mmorpg.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.SpellList;
import orion.mmorpg.players.races.Race;
import orion.mmorpg.utils.Utils;

public class RaceGui extends ChestGui {

    Player p;
    OrionPlayer orionPlayer;
    int rows = (int) Math.ceil(Race.values().length / 9);

    public RaceGui(Player p, OrionPlayer orionPlayer) {
        super(1, "Race Selection");

        Bukkit.broadcastMessage("r : " + (int) Math.ceil(Race.values().length / 9) + " m/9 : " + Race.values().length / 9);

        this.p = p;
        this.orionPlayer = orionPlayer;

        this.setOnTopClick(e -> e.setCancelled(true));

        this.addPane(generateRacePane());

        show(p);
    }

    OutlinePane racePane = new OutlinePane(0,0,9, 1);
    public OutlinePane generateRacePane() {
        for(Race race : Race.values()) {
            racePane.addItem(new GuiItem(race.getItemStack(), e -> {
                if(orionPlayer.getRace() == null) {

                    new AcceptGui("Devenir " + race.getName(), p, orionPlayer, () -> {
                        orionPlayer.setRace(race);
                        for(String s : race.spellList)
                            SpellList.learnSpell(orionPlayer, s);
                        race.getOnRaceSelected().accept(p, orionPlayer);
                    }, "Vous êtes devenu un(e) " + race.getName());
                } else {
                    Utils.sendErrorMessage(p, "Vous avez déja selectionné une classe");
                }
            }));
        }
        return racePane;
    }
}
