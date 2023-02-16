package orion.mmorpg.players.abilities.spells;

import org.bukkit.Location;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.utils.Element;
import orion.mmorpg.utils.Knowledge;
import orion.mmorpg.utils.Namespaces;
import orion.mmorpg.utils.Utils;

public class Mark extends Spell {
    public Mark() {
        super("MARK", 10, Element.ARCANE, 10*1000);
    }

    @Override
    public void launch(Mmorpg mmorpg, Location location, Knowledge knowledge) {
        assert Namespaces.mark_location != null;
        getCaster().getPersistentDataContainer().set(Namespaces.mark_location, PersistentDataType.STRING , Mmorpg.getOrionGson().getGSON().toJson(location));
        Utils.sendInfoMessage(getCaster(), "Location marquee !");
    }
}
