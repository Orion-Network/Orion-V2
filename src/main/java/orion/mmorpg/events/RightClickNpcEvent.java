package orion.mmorpg.events;

import net.minecraft.server.level.EntityPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import orion.mmorpg.models.NPC;

public class RightClickNpcEvent extends Event {
    private final Player player;
    private final EntityPlayer npc;

    private static final HandlerList handlers = new HandlerList();

    public RightClickNpcEvent(Player player, EntityPlayer npc) {
        this.player = player;
        this.npc = npc;
    }

    public Player getPlayer() {
        return player;
    }

    public EntityPlayer getNpc() {
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
