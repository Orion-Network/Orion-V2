package orion.mmorpg.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class PlayerJoinListener implements Listener {
    Mmorpg mmorpg;

    public PlayerJoinListener(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (mmorpg.getMongoClient().hasPlayer(player.getUniqueId())) {
            try {
                OrionPlayer orionPlayer = mmorpg.getMongoClient().loadPlayer(player.getUniqueId());
                mmorpg.getPlayerManager().addPlayer(player.getUniqueId(), orionPlayer);
                if(orionPlayer.getHealth() <= 0) {
                    orionPlayer.respawn();
                }
                orionPlayer.updateHealthbar();
                Utils.sendInfoMessage(player, "Data loaded !");
                if (orionPlayer.getRace() == null)
                    player.sendTitle("Bienvenu", "Selectionnez votre race via le /menu", 10, 70, 20);
                return;
            } catch (IOException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
                player.kickPlayer("Error in loading");
            }
        }

        OrionPlayer orionPlayer = OrionPlayer.generateSamplePlayer(player);

        mmorpg.getPlayerManager().addPlayer(e.getPlayer().getUniqueId(), orionPlayer);
        player.sendTitle("Bienvenu", "Selectionnez votre classe via le /menu", 10, 70, 20);
    }

    @EventHandler
    public void playerQuitEvent(PlayerQuitEvent event) {
        mmorpg.getMongoClient().savePlayer(mmorpg.getPlayerManager().getPlayer(event.getPlayer().getUniqueId()));
        mmorpg.getPlayerManager().getPlayersMap().remove(event.getPlayer().getUniqueId());
    }
}
