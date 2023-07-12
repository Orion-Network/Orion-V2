package orion.mmorpg.events;

import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RightClickNpcEvent extends Event {
    private final Player player;
    private final ServerPlayer npc;

    private static final HandlerList handlers = new HandlerList();

    public RightClickNpcEvent(Player player, ServerPlayer npc) {
        this.player = player;
        this.npc = npc;
    }

    public Player getPlayer() {
        return player;
    }

    public ServerPlayer getNpc() {
        return npc;
    }


    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
