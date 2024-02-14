package orion.mmorpg.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.ItemUtils;
import orion.mmorpg.utils.Namespaces;

public class PlayerUseItemEvent implements Listener {
    Mmorpg mmorpg;

    public PlayerUseItemEvent(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @EventHandler
    public void onPlayerUseItem(PlayerInteractEvent e) {
        OrionPlayer player = mmorpg.getPlayerManager().getPlayer(e.getPlayer().getUniqueId());
        if (e.getItem() == null)
            return;
        if (e.getItem().getItemMeta().getPersistentDataContainer().has(Namespaces.internal_id,
                PersistentDataType.STRING)) {
            if (!ItemUtils.isItem(e.getItem()))
                return;
            PersistentDataContainer container = e.getItem().getItemMeta().getPersistentDataContainer();
            BasicItem item = ItemUtils
                    .generateBasicItem(container.get(Namespaces.internal_id, PersistentDataType.STRING));
            item.onUseItem(mmorpg, player, e);
        }
    }
}
