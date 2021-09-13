package orion.mmorpg.listeners;

import com.google.common.base.Enums;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.misc.Gift;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.Mode;
import orion.mmorpg.utils.Utils;

import java.util.Objects;

public class PlayerPickupListener implements Listener {
    Mmorpg mmorpg;

    public PlayerPickupListener(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @EventHandler
    public void onPlayerPickup(EntityPickupItemEvent e) {
        if(e.getEntity() instanceof Player) {
            if(mmorpg.getPlayerManager().getPlayer(e.getEntity().getUniqueId()).getPlayerMode() != Mode.PLAYER)
                return;
            ItemMeta meta = e.getItem().getItemStack().getItemMeta();
            if(meta.getPersistentDataContainer().has(NamespacedKey.minecraft("internal_id"), PersistentDataType.STRING)) {
                String internal_id = meta.getPersistentDataContainer().get(NamespacedKey.minecraft("internal_id"), PersistentDataType.STRING);
                if(Enums.getIfPresent(ItemList.class, internal_id).isPresent()) {
                    if(((Player)e.getEntity()).hasPermission(ItemList.valueOf(internal_id).getItem().PERMISSION)) {
                        ItemList item = ItemList.valueOf(internal_id);
                        ItemStack itemStack = item.getProcessedItem(e.getItem().getItemStack().getAmount());
                        e.getItem().setItemStack(itemStack);
                        item.getItem().onPickupItem(e, itemStack);
                    }
                    else {
                        Utils.sendErrorMessage(e.getEntity(), "You don't have the permission to use this item");
                        e.setCancelled(true);
                    }
                } else {
                    Utils.sendErrorMessage(e.getEntity(), "Unimplemented item " + internal_id);
                    e.setCancelled(true);
                    e.getItem().remove();
                }
            } else {
                String internal_id = "MINECRAFT_"+e.getItem().getItemStack().getType();
                if(!Enums.getIfPresent(ItemList.class, internal_id).isPresent()) {
                    e.setCancelled(true);
                    e.getItem().remove();
                    Utils.sendErrorMessage(e.getEntity(), "This item is blacklisted");
                } else {
                    e.getItem().setItemStack(ItemList.valueOf(internal_id).getProcessedItem(e.getItem().getItemStack().getAmount()));
                }
            }
        }
    }
}
