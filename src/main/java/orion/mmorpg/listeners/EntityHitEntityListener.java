package orion.mmorpg.listeners;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.sample.BasicMeleeWeapon;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.OrionGson;
import orion.mmorpg.utils.Stats;
import orion.mmorpg.utils.Utils;

public class EntityHitEntityListener implements Listener {

    Mmorpg mmorpg;

    public EntityHitEntityListener(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

}
