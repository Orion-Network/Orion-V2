package orion.mmorpg.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.OrionPlayer;

public class OnPlayerPickupRpgItem extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    ItemStack itemStack;
    BasicItem item;
    Player player;
    OrionPlayer orionPlayer;

    public OnPlayerPickupRpgItem(ItemStack itemStack, BasicItem item, Player player, OrionPlayer orionPlayer) {
        this.itemStack = itemStack;
        this.item = item;
        this.player = player;
        this.orionPlayer = orionPlayer;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
