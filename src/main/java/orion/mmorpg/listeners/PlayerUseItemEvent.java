package orion.mmorpg.listeners;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.OrionPlayer;

public class PlayerUseItemEvent implements Listener {
    Mmorpg mmorpg;

    public PlayerUseItemEvent(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @EventHandler
    public void onPlayerUseItem(PlayerInteractEvent e) {
        OrionPlayer player = mmorpg.getPlayerManager().getPlayer(e.getPlayer().getUniqueId());
            if(e.getItem().getItemMeta().getPersistentDataContainer().has(NamespacedKey.minecraft("internal_id"), PersistentDataType.STRING)) {
                PersistentDataContainer container = e.getItem().getItemMeta().getPersistentDataContainer();
                BasicItem item = ItemList.valueOf(container.get(NamespacedKey.minecraft("internal_id"), PersistentDataType.STRING)).getItem();
                item.onUseItem(mmorpg, player, e);
            }

    }
}
