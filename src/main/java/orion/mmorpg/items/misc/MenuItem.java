package orion.mmorpg.items.misc;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.gui.MenuGui;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Rarity;

public class MenuItem extends BasicItem {
    public MenuItem() {
        super(Material.CLOCK, "Menu", "MENU_ITEM", 1, "Permet d'ouvrir le menu", Rarity.RARE);
    }

    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        new MenuGui(e.getPlayer(), data).show(e.getPlayer());
        super.onUseItem(mmorpg, data, e);
    }
}
