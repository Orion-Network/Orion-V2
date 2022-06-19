package orion.mmorpg.managers;

import org.bukkit.Bukkit;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;

import java.util.LinkedHashMap;
import java.util.UUID;

public class PlayerManager extends AbstractManager {
    private final LinkedHashMap<UUID, OrionPlayer> playersMap;

    public PlayerManager(Mmorpg mmorpg) {
        super(mmorpg);
        this.playersMap = new LinkedHashMap<>();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(mmorpg, new Runnable() {
            @Override
            public void run() {
                System.out.println("Saving Players");
                savePlayers();
            }
        }, 1L, 300* 20);
    }

    public boolean addPlayer(UUID uuid, OrionPlayer orionPlayer) {
        if (playersMap.containsKey(uuid)) {
            return false;
        } else {
            playersMap.put(uuid, orionPlayer);
            return true;
        }
    }

    public OrionPlayer getPlayer(UUID uuid) {
        return playersMap.get(uuid);
    }

    public LinkedHashMap<UUID, OrionPlayer> getPlayersMap() {
        return playersMap;
    }

    public void savePlayers() {
        playersMap.forEach((uuid, player) -> {
            mmorpg.getMongoClient().savePlayer(player);
            if(Bukkit.getPlayer(uuid) == null)
                playersMap.remove(player);
        });
    }

}
