package orion.mmorpg.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Infos;
import orion.mmorpg.utils.Stats;

import java.time.LocalDateTime;

public class PlayerJoinListener implements Listener {
    Mmorpg mmorpg;

    public PlayerJoinListener(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent e) {
        Infos infos = new Infos(e.getPlayer().getName(), LocalDateTime.now(), LocalDateTime.now(), e.getPlayer().getPlayerTime());
        Stats stats = new Stats(1,10,50,100,20,0,0,25,0);
        OrionPlayer orionPlayer = new OrionPlayer(infos, stats, e.getPlayer().getUniqueId(), 0, 0);

        mmorpg.getPlayerManager().addPlayer(e.getPlayer().getUniqueId(), orionPlayer);
    }
}
