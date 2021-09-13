package orion.mmorpg.managers;

import orion.mmorpg.players.OrionPlayer;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.UUID;

public class PlayerManager {
    private LinkedHashMap<UUID, OrionPlayer> playersMap;

    public PlayerManager() {
        this.playersMap = new LinkedHashMap<>();
    }

    public boolean addPlayer(UUID uuid, OrionPlayer orionPlayer) {
        if(playersMap.containsKey(uuid)) {
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
}
