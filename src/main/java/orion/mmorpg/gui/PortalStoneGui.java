package orion.mmorpg.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.PortalStone;

public class PortalStoneGui extends ChestGui {
    Player p;
    OrionPlayer orionPlayer;
    private final StaticPane listPane = new StaticPane(0, 0, 9, 1);

    public PortalStoneGui(Player p, OrionPlayer orionPlayer) {
        super(1, ChatColor.GREEN + "Portal Stones");
        this.p = p;
        this.orionPlayer = orionPlayer;

        this.setOnTopClick(e -> e.setCancelled(true));
        this.addPane(getListPane());
    }

    public StaticPane getListPane() {
        // TODO add this to Orion World
        /*
         * int i = 0; for (String id : orionPlayer.getPortalStones()) {
         *
         * if(!Mmorpg.INSTANCE.getWorldManager().getPortalStones().containsKey(id)) continue; PortalStone portalStone =
         * Mmorpg.INSTANCE.getWorldManager().getPortalStone(id);
         *
         * listPane.addItem(new GuiItem(portalStone.generateItem(), e -> { p.teleport(portalStone.getLocation()); }), i,
         * i / 6); i++; } return listPane;
         */
        return new StaticPane(0, 0, 9, 1);
    }
}
