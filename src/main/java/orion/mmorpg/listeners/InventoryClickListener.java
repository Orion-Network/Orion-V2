package orion.mmorpg.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.gui.MenuGui;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.ItemUtils;
import orion.mmorpg.utils.Utils;

import java.util.Objects;

public class InventoryClickListener implements Listener {
    Mmorpg mmorpg;

    public InventoryClickListener(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getSlot() == -999)
            return;
        OrionPlayer player = mmorpg.getPlayerManager().getPlayer(Objects.requireNonNull(e.getWhoClicked()).getUniqueId());
        if(!e.getInventory().getType().equals(InventoryType.CRAFTING)) return;
        e.getInventory().setItem(0, ItemUtils.generateItemStack("MENU_ITEM", 1));
        if (e.getSlotType() == InventoryType.SlotType.RESULT) {
            e.setCancelled(true);
            new MenuGui((Player) e.getWhoClicked(), this.mmorpg.getPlayerManager().getPlayer(e.getWhoClicked().getUniqueId()));
        }
        if (e.getSlotType() == InventoryType.SlotType.ARMOR) {
            if(Objects.requireNonNull(e.getCurrentItem()).getType() == Material.AIR && Objects.requireNonNull(e.getCursor()).getType() == Material.AIR) return;
            if (Objects.requireNonNull(e.getCurrentItem()).getType().equals(Material.AIR) && e.getCurrentItem() != null) {
                ((BasicArmorItem) ItemUtils.getItemFromItemstack(Objects.requireNonNull(e.getCursor()))).onCosmeticEquip((Player) e.getWhoClicked(), mmorpg.getPlayerManager().getPlayer(e.getWhoClicked().getUniqueId()));
                Utils.sendInfoMessage(e.getWhoClicked(), "Vous avez équipé un cosmétique.");
            } else {
                ((BasicArmorItem) ItemUtils.getItemFromItemstack(e.getCurrentItem())).onCosmeticUnequip((Player) e.getWhoClicked(), mmorpg.getPlayerManager().getPlayer(e.getWhoClicked().getUniqueId()));
                Utils.sendInfoMessage(e.getWhoClicked(), "Vous avez déséquipé un cosmétique.");
            }
        }
        if(player.getSlotTypes().length > e.getSlot())
            return;
        if(e.getSlot() < 9 && player.getSlotTypes()[e.getSlot()] == OrionPlayer.SlotType.RESTRICTED_SLOT)
            e.setCancelled(true);
        if (e.isShiftClick())
            e.setCancelled(true);
    }
}
