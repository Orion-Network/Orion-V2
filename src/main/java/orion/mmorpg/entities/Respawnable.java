package orion.mmorpg.entities;

import org.bukkit.Location;
import orion.mmorpg.managers.ChunkManager;

public interface Respawnable {
    void respawn(String uuid, Location location, ChunkManager manager);
}
