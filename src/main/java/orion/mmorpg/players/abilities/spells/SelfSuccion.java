package orion.mmorpg.players.abilities.spells;

import org.bukkit.entity.Player;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.supports.SuccionSanguine;
import orion.mmorpg.utils.Element;

public class SelfSuccion extends Spell {
    public SelfSuccion() {
        super("SELF_SUCCION", 15, Element.BLACK, 30*1000);
    }

    @Override
    public void launch(Mmorpg mmorpg, Player player, OrionPlayer orionPlayer) {
        double damage = orionPlayer.getHealth()/2;
        player.damage(damage);
        if(orionPlayer.getParty() != null)
            orionPlayer.getParty().getPlayers().forEach(p -> p.heal(damage/10));
        else
            orionPlayer.getSupportMap().put(new SuccionSanguine(), (1000*15)+System.currentTimeMillis());
    }
}
