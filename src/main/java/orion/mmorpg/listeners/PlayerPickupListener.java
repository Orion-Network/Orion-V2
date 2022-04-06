package orion.mmorpg.listeners;

import com.google.common.base.Enums;
import net.md_5.bungee.api.ChatColor;
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
import orion.mmorpg.items.misc.MasterScrollItem;
import orion.mmorpg.items.misc.SpellScroll;
import orion.mmorpg.players.Mode;
import orion.mmorpg.utils.Utils;

public class PlayerPickupListener implements Listener {
    Mmorpg mmorpg;

    public PlayerPickupListener(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @EventHandler
    public void onPlayerPickup(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            if (mmorpg.getPlayerManager().getPlayer(e.getEntity().getUniqueId()).getPlayerMode() != Mode.PLAYER)
                return;
            ItemMeta meta = e.getItem().getItemStack().getItemMeta();
            if (ChatColor.stripColor(meta.getDisplayName()).startsWith("%mm%:") || meta.getPersistentDataContainer().has(NamespacedKey.minecraft("internal_id"), PersistentDataType.STRING)) {
                String internal_id = "";
                if(ChatColor.stripColor(meta.getDisplayName()).startsWith("%mm%:"))
                    internal_id = ChatColor.stripColor(meta.getDisplayName()).substring(5);
                else
                    internal_id = meta.getPersistentDataContainer().get(NamespacedKey.minecraft("internal_id"), PersistentDataType.STRING);
                if (Enums.getIfPresent(ItemList.class, internal_id).isPresent()) {
                    if (e.getEntity().hasPermission(ItemList.valueOf(internal_id).getItem().getPERMISSION())) {
                        ItemList item = ItemList.valueOf(internal_id);
                        ItemStack itemStack = item.getProcessedItem(e.getItem().getItemStack().getAmount());
                        if(item.getItem().getINTERNAL_ID().equalsIgnoreCase("MASTER_SCROLL"))
                            itemStack = ((MasterScrollItem)item.getItem()).generateScroll(e.getItem().getItemStack().getItemMeta().getPersistentDataContainer().get(NamespacedKey.minecraft("spell_id"), PersistentDataType.STRING));
                        else if(item.getItem().getINTERNAL_ID().endsWith("SPELL_SCROLL"))
                            itemStack = ((SpellScroll)item.getItem()).generateScroll(e.getItem().getItemStack().getItemMeta().getPersistentDataContainer().get(NamespacedKey.minecraft("spell_id"), PersistentDataType.STRING));
                        e.getItem().setItemStack(itemStack);
                        item.getItem().onPickupItem(e, itemStack);
                    } else {
                        Utils.sendErrorMessage(e.getEntity(), "You don't have the permission to use this item");
                        e.setCancelled(true);
                    }
                } else {
                    Utils.sendErrorMessage(e.getEntity(), "Unimplemented item " + internal_id);
                    e.setCancelled(true);
                    e.getItem().remove();
                }
            } else {
                String internal_id = "MINECRAFT_" + e.getItem().getItemStack().getType();
                if (!Enums.getIfPresent(ItemList.class, internal_id).isPresent()) {
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
