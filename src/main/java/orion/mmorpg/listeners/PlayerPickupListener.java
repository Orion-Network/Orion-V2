package orion.mmorpg.listeners;

import com.google.common.base.Enums;
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
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.Mode;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Namespaces;
import orion.mmorpg.utils.Utils;

public class PlayerPickupListener implements Listener {
    Mmorpg mmorpg;

    public PlayerPickupListener(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @EventHandler
    public void onPlayerPickup(EntityPickupItemEvent e) {
        if(!(e.getEntity() instanceof Player))
            return;

        Player player = (Player) e.getEntity();
        OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(player.getUniqueId());

        if(orionPlayer.getPlayerMode() != Mode.PLAYER)
            return;

        ItemStack item = e.getItem().getItemStack();
        ItemMeta meta = item.getItemMeta();

        if(!meta.getPersistentDataContainer().has(Namespaces.internal_id, PersistentDataType.STRING)) {
            String internal_id = "MINECRAFT_" + e.getItem().getItemStack().getType();
            if (!Enums.getIfPresent(ItemList.class, internal_id).isPresent()) {
                e.setCancelled(true);
                e.getItem().remove();
                Utils.sendErrorMessage(e.getEntity(), "Cet objet est sur liste noir");
            } else {
                e.getItem().setItemStack(ItemList.getItemFromItemstack(item));
            }
            return;
        }

        String internal_id = meta.getPersistentDataContainer().get(Namespaces.internal_id, PersistentDataType.STRING);
        if(!Enums.getIfPresent(ItemList.class, internal_id).isPresent()) {
            Utils.sendErrorMessage(player, "Objet non implémenté.");
            e.setCancelled(true);
            return;
        }

        if (player.hasPermission(ItemList.getItemFromItemstack(item).getPERMISSION())) {

            BasicItem basicItem = ItemList.getItemFromItemstack(item);
            ItemStack itemStack = e.getItem().getItemStack();
            if(meta.getPersistentDataContainer().has(Namespaces.uuid, PersistentDataType.STRING)) {
                itemStack.setItemMeta(item.getItemMeta());
                return;
            }

            if(basicItem.getINTERNAL_ID().equalsIgnoreCase("MASTER_SCROLL"))
                itemStack = ((MasterScrollItem)basicItem).generateScroll(e.getItem().getItemStack().getItemMeta().getPersistentDataContainer().get(Namespaces.spell_id, PersistentDataType.STRING), e.getItem().getItemStack().getItemMeta().getPersistentDataContainer().get(Namespaces.knowledge, PersistentDataType.INTEGER));
            else if(basicItem.getINTERNAL_ID().endsWith("SPELL_SCROLL"))
                itemStack = ((SpellScroll)basicItem).generateScroll(e.getItem().getItemStack().getItemMeta().getPersistentDataContainer().get(Namespaces.spell_id, PersistentDataType.STRING));

            e.getItem().setItemStack(itemStack);
            basicItem.onPickupItem(e, itemStack);
        } else {
            Utils.sendErrorMessage(e.getEntity(), "Nous n'avez pas la permission d'utiliser cet objet");
            e.setCancelled(true);
        }

        ((Player) e.getEntity()).updateInventory();
    }
}
