package orion.mmorpg.listeners;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.server.ServerListPingEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.utils.GameState;

public class ServerListener implements Listener {
    Mmorpg mmorpg;

    public ServerListener(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @EventHandler
    public void onServerStart(ServerListPingEvent event) {
        if(mmorpg.getGameState() == GameState.STARTING) {
            event.setMotd(ChatColor.RED + "Server is starting, please wait, it can take up to 5 minutes.");
        } else if(mmorpg.getGameState() == GameState.ENDING) {
            event.setMotd(ChatColor.RED + "Server is shutting down, please wait.");
        }
        event.setMotd(
                "                       " +
                ChatColor.GRAY + "play.orionrpg.fr" +
                "   " +
                ChatColor.GREEN + "v" + mmorpg.getDescription().getVersion() +
                "                     " +
                        ChatColor.BOLD + "Minecraft Open World RPG"
        );
    }

    @EventHandler
    public void onResourcePack(PlayerResourcePackStatusEvent event) {
        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
            event.getPlayer().kickPlayer(ChatColor.RED + "Resource pack failed to download.");
        } else if (event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED) {
            event.getPlayer().kickPlayer(ChatColor.RED + "Resource pack declined.");
        } else if (event.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
            event.getPlayer().sendMessage(ChatColor.GREEN + "Resource pack successfully loaded.");
        } else if (event.getStatus() == PlayerResourcePackStatusEvent.Status.ACCEPTED) {
            event.getPlayer().sendMessage(ChatColor.GREEN + "Resource pack accepted.");
        }
    }
}
