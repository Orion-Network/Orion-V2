package orion.mmorpg.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.events.OnSpellSense;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.items.sample.BasicMeleeWeapon;
import orion.mmorpg.items.sample.BasicRangedWeapon;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.OrionGson;
import orion.mmorpg.utils.Stats;
import orion.mmorpg.utils.Utils;

public class PlayerStatsListener implements Listener {
    Mmorpg mmorpg;

    public PlayerStatsListener(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(mmorpg, new Runnable() {
            @Override
            public void run() {
                mmorpg.getPlayerManager().getPlayersMap().values().forEach(OrionPlayer::onTick);
            }
        }, 0, 20);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
        orionPlayer.onInteract(event);
    }

    @EventHandler
    public void onPlayerHitEntity(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(p.getUniqueId());
            if(p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(NamespacedKey.minecraft("internal_id"), PersistentDataType.STRING)) {
                PersistentDataContainer container = p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer();
                BasicItem item = ItemList.valueOf(container.get(NamespacedKey.minecraft("internal_id"), PersistentDataType.STRING)).getItem();
                if(container.has(NamespacedKey.minecraft("damage"), PersistentDataType.FLOAT))
                    e.setDamage(container.get(NamespacedKey.minecraft("damage"), PersistentDataType.FLOAT));
                item.onHitWithItem(e);
                if(ItemList.valueOf(container.get(NamespacedKey.minecraft("internal_id"), PersistentDataType.STRING)).getItemClass().equals(BasicMeleeWeapon.class)) {
                    Stats stats = Mmorpg.getOrionGson().getGSON().fromJson(container.get(NamespacedKey.minecraft("stats"), PersistentDataType.STRING), Stats.class);
                    orionPlayer.onHit(e, orionPlayer);
                    float totalDamage = Utils.calculateDamage(orionPlayer.getStats(), stats, (float) e.getDamage());
                    e.setDamage(totalDamage);

                    LivingEntity entity = (LivingEntity) e.getEntity();
                    orionPlayer.updateMobHealthBar(entity.getHealth() - totalDamage, entity.getMaxHealth(), entity.getName());

                }
            }
        }
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(p.getUniqueId());
            orionPlayer.onHitByEntity(e);
            e.setCancelled(true);
            float reduction =  1 + (orionPlayer.getStats().getDefence() / (orionPlayer.getStats().getDefence() + 100));
            orionPlayer.setHealth(orionPlayer.getHealth() - (e.getDamage() / reduction));

            if(orionPlayer.getHealth() <= 0)
                orionPlayer.onDie();
            else
                orionPlayer.updateHealthbar();

            Utils.sendDebugMessage(p, "Base Damage : " + e.getDamage() + " Defense : " + reduction + " Calc. Damages : " + e.getDamage() / reduction);
        }
    }

    @EventHandler
    public void useBow(EntityShootBowEvent e) {
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(e.getEntity().getUniqueId());
            if(p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(NamespacedKey.minecraft("internal_id"), PersistentDataType.STRING)) {
                PersistentDataContainer container = p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer();
                if(ItemList.valueOf(container.get(NamespacedKey.minecraft("internal_id"), PersistentDataType.STRING)).getItemClass().equals(BasicRangedWeapon.class)) {
                    orionPlayer.onShootArrow(e);
                    BasicRangedWeapon basicRangedWeapon = (BasicRangedWeapon) ItemList.valueOf(container.get(NamespacedKey.minecraft("internal_id"), PersistentDataType.STRING)).getItem();
                    basicRangedWeapon.onShootBow(e);
                }
            }
        }
    }

    @EventHandler
    public void onSpellSense(OnSpellSense e) {
        mmorpg.getPlayerManager().getPlayer(e.getTarget().getUniqueId()).onSpellSense(e);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player) {
            if(e.getCause() == EntityDamageEvent.DamageCause.FALL || e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK)
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void onLevelUp(PlayerLevelChangeEvent e) {
        OrionPlayer op = mmorpg.getPlayerManager().getPlayer(e.getPlayer().getUniqueId());
        if(e.getPlayer().getLevel() > op.getLevel())
            op.levelUp(e.getPlayer(), e.getNewLevel());
    }
}
