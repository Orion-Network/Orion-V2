package orion.mmorpg.listeners;

import org.bukkit.Material;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Utils;

public class PlayerStatsListener implements Listener {
    Mmorpg mmorpg;

    public PlayerStatsListener(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

}
