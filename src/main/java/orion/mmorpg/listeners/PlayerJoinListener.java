package orion.mmorpg.listeners;

import fr.mrmicky.fastboard.FastBoard;
import net.minecraft.world.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.GameState;
import orion.mmorpg.utils.Utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class PlayerJoinListener implements Listener {
    Mmorpg mmorpg;

    public PlayerJoinListener(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent e) {
        if (mmorpg.getGameState() == GameState.STARTING) {
            e.getPlayer().kickPlayer("Server is starting, please wait.");
            return;
        } else if (mmorpg.getGameState() == GameState.ENDING) {
            e.getPlayer().kickPlayer("Server is shutting down, please wait.");
            return;
        }

        Player player = e.getPlayer();
        if(mmorpg.getPlayerManager().getPlayersMap().containsKey(e.getPlayer().getUniqueId())) {
            OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(e.getPlayer().getUniqueId());
            orionPlayer.setFastBoard(new FastBoard(player));
            return;
        }
        if (mmorpg.getMongoClient().hasPlayer(player.getUniqueId())) {
            try {
                OrionPlayer orionPlayer = mmorpg.getMongoClient().loadPlayer(player.getUniqueId());
                mmorpg.getPlayerManager().addPlayer(player.getUniqueId(), orionPlayer);
                if(orionPlayer.getHealth() <= 0) {
                    orionPlayer.respawn();
                }
                orionPlayer.updateHealthbar();
                Utils.sendInfoMessage(player, "Data loaded !");
                if(orionPlayer.getMainSupport() != null)
                    orionPlayer.getMainSupport().applyEffects(orionPlayer, -1);
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
        OrionPlayer player = mmorpg.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
        player.getFastBoard().delete();
        if(player.getPet() != null)
            player.getPet().setRemoved(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
        if(event.getPlayer().isInsideVehicle()) {
            Objects.requireNonNull(event.getPlayer().getVehicle()).remove();
            event.getPlayer().leaveVehicle();
        }
    }
}
