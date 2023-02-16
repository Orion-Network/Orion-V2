package orion.mmorpg.players.abilities.spells;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.utils.Element;
import orion.mmorpg.utils.Knowledge;
import orion.mmorpg.utils.Utils;

public class Switch extends Spell {

        public Switch() {
            super("SWITCH", 30, Element.ARCANE, 15*1000);
        }

    @Override
    public void launch(Mmorpg mmorpg, Location location, Knowledge knowledge) {
        Entity target = Utils.getTargetInFront((LivingEntity) getCaster(), 20);
        if(target != null) {
            Location targetLocation = target.getLocation();
            Location casterLocation = getCaster().getLocation();
            target.teleport(casterLocation);
            getCaster().teleport(targetLocation);
        }
    }
}
