package orion.mmorpg.database;

import orion.mmorpg.players.OrionPlayer;

import java.util.UUID;

public abstract class SaveStrategy {
    public abstract void savePlayer(OrionPlayer player);

    public abstract OrionPlayer loadPlayer(UUID uniqueId);
}
