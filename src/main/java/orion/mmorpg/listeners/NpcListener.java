package orion.mmorpg.listeners;

import net.minecraft.server.level.EntityPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.events.RightClickNpcEvent;
import orion.mmorpg.models.NPC;
import orion.mmorpg.models.PacketReader;

public class NpcListener implements Listener {
    private final Mmorpg plugin;

    public NpcListener(Mmorpg plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClickNpc(RightClickNpcEvent event) {
        Player player = event.getPlayer();
        EntityPlayer npc = event.getNpc();
        player.sendMessage("You right clicked " + npc.getName());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PacketReader pr = new PacketReader(event.getPlayer());
        pr.inject();
    }
}
