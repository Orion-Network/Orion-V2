package orion.mmorpg.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.gui.MenuGui;
import orion.mmorpg.utils.Utils;

public class InventoryClickListener implements Listener {
    Mmorpg mmorpg;

    public InventoryClickListener(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getInventory().getType() == InventoryType.CRAFTING && e.getSlot() >= 0 && e.getSlot() <= 4 && ((Player)e.getWhoClicked()).getGameMode() == GameMode.ADVENTURE) {
            e.setCancelled(true);
            if(e.getSlot() == 0)
                new MenuGui((Player) e.getWhoClicked(), this.mmorpg.getPlayerManager().getPlayer(((Player) e.getWhoClicked()).getUniqueId()));
        }
        if(e.getSlotType() == InventoryType.SlotType.ARMOR) {
            e.setCancelled(true);
            Utils.sendInfoMessage(e.getWhoClicked(), "You need to open /menu to change armor");
        }
        if(e.isShiftClick())
            e.setCancelled(true);
    }
}
