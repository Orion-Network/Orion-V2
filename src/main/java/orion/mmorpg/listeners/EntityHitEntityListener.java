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

    @EventHandler
    public void onPlayerHitEntity(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(p.getUniqueId());
            if(p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(NamespacedKey.minecraft("internal_id"), PersistentDataType.STRING)) {
                PersistentDataContainer container = p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer();
                if(ItemList.valueOf(container.get(NamespacedKey.minecraft("internal_id"), PersistentDataType.STRING)).getItemClass().equals(BasicMeleeWeapon.class)) {
                    Stats stats = OrionGson.GSON.fromJson(container.get(NamespacedKey.minecraft("stats"), PersistentDataType.STRING), Stats.class);
                    float damage = container.get(NamespacedKey.minecraft("damage"), PersistentDataType.FLOAT);
                    float totalDamage = Utils.calculateDamage(orionPlayer.getStats(), stats, damage);
                    e.setDamage(totalDamage);
                }
            }
        }
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(p.getUniqueId());
            e.setCancelled(true);
            float reduction =  1 + (orionPlayer.getStats().getDefence() / (orionPlayer.getStats().getDefence() + 100));
            orionPlayer.setHealth(orionPlayer.getHealth() - (e.getDamage() / reduction));
            orionPlayer.updateHealthbar();
            Utils.sendDebugMessage(p, "Base Damage : " + e.getDamage() + " Defense : " + reduction + " Calc. Damages : " + e.getDamage() / reduction);

            if(orionPlayer.getHealth() <= 0)
                orionPlayer.onDie();
                orionPlayer.respawn();
        }
    }
}
