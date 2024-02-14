package orion.mmorpg.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.Mode;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.ItemUtils;
import orion.mmorpg.utils.Utils;

public class PlayerPickupListener implements Listener {
    Mmorpg mmorpg;

    public PlayerPickupListener(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @EventHandler
    public void onPlayerPickup(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player))
            return;

        Player player = (Player) e.getEntity();
        OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(player.getUniqueId());

        if (orionPlayer.getPlayerMode() != Mode.PLAYER)
            return;

        ItemStack item = e.getItem().getItemStack();

        if (!Utils.isItemValid(item, player)) {
            e.setCancelled(true);
            e.getItem().remove();
            Utils.sendErrorMessage(e.getEntity(), "Cet objet n'est pas valide");
            return;
        }

        if (ItemUtils.isItem(item)) {
            BasicItem basicItem = ItemUtils.getItemFromItemstack(item);
            basicItem.onPickupItem(e, item);
        }

        ((Player) e.getEntity()).updateInventory();
    }
}
