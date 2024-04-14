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
import orion.mmorpg.players.classes.Classe;
import orion.mmorpg.utils.Utils;

import java.util.Arrays;
import java.util.List;

public class ClasseGui extends ChestGui {

    Player p;
    OrionPlayer orionPlayer;
    List<Classe> excludedClasses = Arrays.asList(Classe.CELESTIAL);
    int rows = (int) Math.ceil(Classe.values().length / 9);
    OutlinePane racePane = new OutlinePane(0, 0, 9, 1);

    public ClasseGui(Player p, OrionPlayer orionPlayer) {
        super(1, "Classe Selection");

        this.p = p;
        this.orionPlayer = orionPlayer;

        this.setOnTopClick(e -> e.setCancelled(true));

        this.addPane(generateRacePane());

        show(p);
    }

    public OutlinePane generateRacePane() {
        for (Classe classe : Classe.values()) {
            if (excludedClasses.contains(classe))
                continue;
            racePane.addItem(new GuiItem(classe.getItemStack(), e -> {
                if (orionPlayer.getClasse() == null) {
                    new AcceptGui("Devenir " + classe.getName(), p, orionPlayer, () -> {
                        orionPlayer.setClasse(classe);
                        for (Class<? extends Spell> s : classe.spellList)
                            SpellList.learnSpell(orionPlayer, s, 100);
                        classe.getOnRaceSelected().accept(p, orionPlayer);

                        SphereEffect sphereEffect = new SphereEffect(Mmorpg.effectManager);
                        sphereEffect.particle = Particle.GLOW;
                        sphereEffect.duration = 700;
                        sphereEffect.radius = 1;
                        sphereEffect.radiusIncrease = 0.3;
                        sphereEffect.setLocation(p.getLocation());
                        sphereEffect.start();

                        Mmorpg.INSTANCE.getPlayerManager().savePlayer(orionPlayer);
                    }, "Vous êtes devenu un(e) " + classe.getName());
                } else {
                    Utils.sendErrorMessage(p, "Vous avez déja selectionné une classe");
                }
            }));
        }
        return racePane;
    }
}
