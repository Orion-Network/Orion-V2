package orion.mmorpg.listeners;

import org.bukkit.event.Listener;
import orion.mmorpg.Mmorpg;

public class EntityHitEntityListener implements Listener {

    Mmorpg mmorpg;

    public EntityHitEntityListener(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

}
