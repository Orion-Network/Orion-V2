package orion.mmorpg.players.abilities.spells;

import org.bukkit.Location;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.utils.Element;

public class TestSpell extends Spell {
    public TestSpell() {
        super("TEST_SPELL", 0, Element.ARCANE, 1000);
    }

    @Override
    public void launch(Mmorpg mmorpg, Location Location) {
    }
}
