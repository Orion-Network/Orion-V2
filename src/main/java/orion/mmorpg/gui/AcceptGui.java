package orion.mmorpg.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Utils;

import java.util.function.Consumer;

public class AcceptGui extends ChestGui {
    public AcceptGui(@NotNull String title, Player p, OrionPlayer orionPlayer, Runnable r, @NotNull String message) {
        super(1, title);

        this.setOnTopClick(e -> e.setCancelled(true));

        StaticPane staticPane = new StaticPane(0,0,9,1);
        staticPane.addItem(new GuiItem(ItemList.ACCEPT_BUTTON_PLACEHOLDER.getProcessedItem(1), e -> {
            r.run();
            p.sendMessage(message);
            p.closeInventory();
        }), 1,0);
        staticPane.addItem(new GuiItem(ItemList.REFUSE_BUTTON_PLACEHOLDER.getProcessedItem(1), e -> {
            Utils.sendErrorMessage(p, "Vous avez refusé");
            p.closeInventory();
        }), 7,0);

        this.addPane(staticPane);

        show(p);
    }
}
