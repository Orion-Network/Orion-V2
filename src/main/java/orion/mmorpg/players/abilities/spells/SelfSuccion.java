package orion.mmorpg.players.abilities.spells;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.supports.SuccionSanguine;
import orion.mmorpg.utils.Element;

public class SelfSuccion extends Spell {
    public SelfSuccion() {
        super("SELF_SUCCION", 15, Element.BLACK, 30 * 1000);
    }

    @Override
    public void launch(Mmorpg mmorpg, Location location) {
        if(getCaster() instanceof Player) {
            double damage = getOrionCaster().getHealth() / 2;
            ((Player)getOrionCaster()).damage(damage);
            if (getOrionCaster().getParty() != null)
                getOrionCaster().getParty().getPlayers().forEach(p -> p.heal(damage / 10));
            else
                getOrionCaster().getSupportMap().put(new SuccionSanguine(), (1000 * 15) + System.currentTimeMillis());
        }

    }
}
