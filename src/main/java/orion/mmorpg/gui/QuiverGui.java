package orion.mmorpg.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;

public class QuiverGui extends ChestGui {
    Mmorpg mmorpg;
    Player player;
    OrionPlayer orionPlayer;
    OutlinePane quiverPane = new OutlinePane(0, 0, 9, 1);

    public QuiverGui(Mmorpg mmorpg, Player player, OrionPlayer orionPlayer) {
        super(1, ChatColor.YELLOW + "Quiver");

        this.mmorpg = mmorpg;
        this.player = player;
        this.orionPlayer = orionPlayer;
        this.addPane(getQuiverPane());
        this.setOnBottomClick(e -> {
            if (!e.getCurrentItem().getType().equals(Material.ARROW)
                    && !e.getCurrentItem().getType().equals(Material.SPECTRAL_ARROW)
                    && !e.getCurrentItem().getType().equals(Material.TIPPED_ARROW))
                return;
            e.setCancelled(true);
            for (int i = 0; i < orionPlayer.getQuiver().length; i++) {
                if (orionPlayer.getQuiver()[i] == null) {
                    orionPlayer.getQuiver()[i] = e.getCursor();
                    getQuiverPane().insertItem(new GuiItem(e.getCurrentItem()), i);
                    e.setCurrentItem(null);
                    this.update();
                    return;
                }
            }
        });
    }

    public OutlinePane getQuiverPane() {
        for (int i = 0; i < orionPlayer.getQuiver().length; i++) {
            if (orionPlayer.getQuiver()[i] != null)
                quiverPane.insertItem(new GuiItem(orionPlayer.getQuiver()[i]), i);
        }
        this.update();
        quiverPane.setOnClick(e -> {
            e.setCancelled(true);
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(orionPlayer.getQuiver()[e.getSlot()]);
                quiverPane.removeItem(quiverPane.getItems().get(e.getSlot()));
                orionPlayer.getQuiver()[e.getSlot()] = null;
                this.update();
            }
        });
        return quiverPane;
    }
}
