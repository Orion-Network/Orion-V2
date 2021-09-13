package orion.mmorpg.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.PortalStone;
import orion.mmorpg.utils.Utils;

import javax.sound.sampled.Port;

public class PortalStoneGui extends ChestGui {
    Player p;
    OrionPlayer orionPlayer;
    public PortalStoneGui(Player p, OrionPlayer orionPlayer) {
        super(1, ChatColor.GREEN + "Portal Stones");
        this.p = p;
        this.orionPlayer = orionPlayer;

        this.setOnTopClick(e -> e.setCancelled(true));
        this.addPane(getListPane());
    }

    private StaticPane listPane = new StaticPane(0,0,9, 1);
    public StaticPane getListPane() {
        int i = 0;
        for(String id : orionPlayer.getPortalStones()) {

            PortalStone portalStone = PortalStone.valueOf(id);

            listPane.addItem(new GuiItem(portalStone.generateItem(), e -> {
                //TODO Debug world teleportation
                p.teleport(portalStone.getLocation());
            }), i,i/6);
            i++;
        }
        return listPane;
    }
}
