package orion.mmorpg.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Utils;

public class MountGui extends ChestGui {

    Player owner;
    OrionPlayer orionPlayer;

    public MountGui(Player owner, OrionPlayer orionPlayer) {
        super(3, "Menu des montures");
        this.owner = owner;
        this.orionPlayer = orionPlayer;

        OutlinePane mountPane = new OutlinePane(0, 0, 9, 3);
        orionPlayer.getTamedMounts().forEach(mount -> {
            Utils.sendInfoMessage(owner, mount.name());
            mountPane.addItem(new GuiItem(mount.generateItemStack(), e -> {
                e.setCancelled(true);
                owner.closeInventory();
                mount.spawn(owner);
            }));
        });
        this.addPane(mountPane);
    }
}
