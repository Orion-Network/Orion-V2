package orion.mmorpg.players.abilities.spells;

import org.bukkit.Location;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.utils.Element;
import orion.mmorpg.utils.Knowledge;
import orion.mmorpg.utils.Namespaces;
import orion.mmorpg.utils.Utils;

public class Recall extends Spell {
    public Recall() {
        super("RECALL", 10, Element.ARCANE, 10*1000);
    }

    @Override
    public void launch(Mmorpg mmorpg, Location location, Knowledge knowledge) {
        assert Namespaces.mark_location != null;
        if(getCaster().getPersistentDataContainer().has(Namespaces.mark_location, PersistentDataType.STRING)) {
            String locationString = getCaster().getPersistentDataContainer().get(Namespaces.mark_location, PersistentDataType.STRING);
            Location markLocation = Mmorpg.getOrionGson().getGSON().fromJson(locationString, Location.class);
            getCaster().teleport(markLocation);
            Utils.sendInfoMessage(getCaster(), "Teleportation reussie !");
        }
    }
}
