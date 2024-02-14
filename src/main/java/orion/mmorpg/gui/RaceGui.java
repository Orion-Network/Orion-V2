package orion.mmorpg.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import de.slikey.effectlib.effect.SphereEffect;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.SpellList;
import orion.mmorpg.players.races.Race;
import orion.mmorpg.utils.Utils;

import java.util.Arrays;
import java.util.List;

public class RaceGui extends ChestGui {

    Player p;
    OrionPlayer orionPlayer;
    List<Race> excludedRaces = Arrays.asList(Race.CELESTIAL);
    int rows = (int) Math.ceil(Race.values().length / 9);
    OutlinePane racePane = new OutlinePane(0, 0, 9, 1);

    public RaceGui(Player p, OrionPlayer orionPlayer) {
        super(1, "Race Selection");

        this.p = p;
        this.orionPlayer = orionPlayer;

        this.setOnTopClick(e -> e.setCancelled(true));

        this.addPane(generateRacePane());

        show(p);
    }

    public OutlinePane generateRacePane() {
        for (Race race : Race.values()) {
            if (excludedRaces.contains(race))
                continue;
            racePane.addItem(new GuiItem(race.getItemStack(), e -> {
                if (orionPlayer.getRace() == null) {
                    new AcceptGui("Devenir " + race.getName(), p, orionPlayer, () -> {
                        orionPlayer.setRace(race);
                        for (Class<? extends Spell> s : race.spellList)
                            SpellList.learnSpell(orionPlayer, s, 100);
                        race.getOnRaceSelected().accept(p, orionPlayer);

                        SphereEffect sphereEffect = new SphereEffect(Mmorpg.effectManager);
                        sphereEffect.particle = Particle.GLOW;
                        sphereEffect.duration = 700;
                        sphereEffect.radius = 1;
                        sphereEffect.radiusIncrease = 0.3;
                        sphereEffect.setLocation(p.getLocation());
                        sphereEffect.start();

                        Mmorpg.INSTANCE.getPlayerManager().savePlayer(orionPlayer);
                    }, "Vous êtes devenu un(e) " + race.getName());
                } else {
                    Utils.sendErrorMessage(p, "Vous avez déja selectionné une classe");
                }
            }));
        }
        return racePane;
    }
}
