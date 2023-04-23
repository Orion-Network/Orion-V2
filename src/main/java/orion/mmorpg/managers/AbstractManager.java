package orion.mmorpg.managers;

import org.bukkit.scheduler.BukkitRunnable;
import orion.mmorpg.Mmorpg;

public abstract class AbstractManager {
    Mmorpg mmorpg;

    public AbstractManager(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }
}
