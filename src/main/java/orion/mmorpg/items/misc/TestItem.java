package orion.mmorpg.items.misc;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.supports.Soleil;
import orion.mmorpg.utils.Rarity;

public class TestItem extends BasicItem {
    Player p;

    public TestItem() {
        super(Material.NETHER_STAR, "Test Item", "TEST_ITEM", 0, null, Rarity.UNOBTAINABLE);
    }

    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        /*EquationEffect effect = new EquationEffect(Mmorpg.effectManager);
        effect.iterations = 500;
        effect.period = 1;
        effect.duration = 1*1000;
        effect.particles = 150;
        effect.setLocation(e.getPlayer().getLocation());
        effect.xEquation = "sin(t)+8^(2/3)*sin(t/8)";
        effect.yEquation = "cos(t)+8^(2/3)*cos(t/8)";

        effect.start();*/

        data.setSupport(new Soleil());
        super.onUseItem(mmorpg, data, e);
    }
}
