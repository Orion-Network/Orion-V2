package orion.mmorpg.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.events.OnSpellSense;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.items.sample.BasicMeleeWeapon;
import orion.mmorpg.items.sample.BasicRangedWeapon;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.SpellList;
import orion.mmorpg.utils.Knowledge;
import orion.mmorpg.utils.Stats;
import orion.mmorpg.utils.Utils;

import java.lang.reflect.InvocationTargetException;

public class PlayerStatsListener implements Listener {
    Mmorpg mmorpg;

    public PlayerStatsListener(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(mmorpg, () -> mmorpg.getPlayerManager().getPlayersMap().values().forEach(OrionPlayer::onTick), 0, 20);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
        orionPlayer.onInteract(event);
        Player p = event.getPlayer();
        p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(orionPlayer.getStats().getAttack_speed() / 20);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerHitEntity(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) return;
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(p.getUniqueId());
            p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(orionPlayer.getStats().getAttack_speed() / 20);
            if (p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(NamespacedKey.minecraft("internal_id"), PersistentDataType.STRING)) {
                PersistentDataContainer container = p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer();
                BasicItem item = ItemList.valueOf(container.get(NamespacedKey.minecraft("internal_id"), PersistentDataType.STRING)).getItem();
                if (container.has(NamespacedKey.minecraft("damage"), PersistentDataType.FLOAT))
                    e.setDamage(container.get(NamespacedKey.minecraft("damage"), PersistentDataType.FLOAT));
                item.onHitWithItem(e);
                if (ItemList.valueOf(container.get(NamespacedKey.minecraft("internal_id"), PersistentDataType.STRING)).getItemClass().equals(BasicMeleeWeapon.class)) {
                    orionPlayer.onHit(e, orionPlayer);
                    float totalDamage = Utils.calculateDamage(orionPlayer.getStats(), (float) e.getDamage()) * p.getAttackCooldown();
                    e.setDamage(totalDamage);
                    if (!(e.getEntity() instanceof Player)) {
                        LivingEntity entity = (LivingEntity) e.getEntity();
                        orionPlayer.updateMobHealthBar(entity.getHealth() - totalDamage, entity.getMaxHealth(), entity.getName());
                    } else {
                        OrionPlayer target = mmorpg.getPlayerManager().getPlayer(e.getEntity().getUniqueId());
                        orionPlayer.updateMobHealthBar(target.getHealth() - totalDamage, target.getStats().getVitality(), e.getEntity().getName());
                    }
                }
            }
        }
        if (e.getDamager() instanceof Arrow) {
            Arrow entity = (Arrow) e.getDamager();
            if (entity.hasMetadata("damage")) {
                e.setDamage(entity.getMetadata("damage").get(0).asFloat());
            }
        }

        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(p.getUniqueId());
            orionPlayer.onHitByEntity(e);
            float reduction = Utils.calculateDamageReduction(orionPlayer.getStats().getDefence());
            orionPlayer.setHealth(orionPlayer.getHealth() - (e.getDamage() / reduction));

            if (orionPlayer.getHealth() <= 0)
                orionPlayer.onDie();
            else
                orionPlayer.updateHealthbar();

            Utils.sendDebugMessage(p, "Base Damage : " + e.getDamage() + " Defense : " + reduction + " Calc. Damages : " + e.getDamage() / reduction + " health : " + orionPlayer.getHealth() + " / " + orionPlayer.getStats().getVitality());
            e.setDamage(0);
        }
    }

    @EventHandler
    public void useBow(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(e.getEntity().getUniqueId());
            if (p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(NamespacedKey.minecraft("internal_id"), PersistentDataType.STRING)) {
                PersistentDataContainer container = p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer();
                if (ItemList.valueOf(container.get(NamespacedKey.minecraft("internal_id"), PersistentDataType.STRING)).getItemClass().equals(BasicRangedWeapon.class)) {
                    BasicRangedWeapon basicRangedWeapon = (BasicRangedWeapon) ItemList.valueOf(container.get(NamespacedKey.minecraft("internal_id"), PersistentDataType.STRING)).getItem();
                    float arrowDamage = 1;
                    if (e.getConsumable().getItemMeta().getPersistentDataContainer().has(NamespacedKey.minecraft("damage"), PersistentDataType.FLOAT))
                        arrowDamage = e.getConsumable().getItemMeta().getPersistentDataContainer().get(NamespacedKey.minecraft("damage"), PersistentDataType.FLOAT);
                    MetadataValue damage = new FixedMetadataValue(mmorpg, Utils.calculateDamage(orionPlayer.getStats(), basicRangedWeapon.getDAMAGE() + arrowDamage));
                    e.getProjectile().setMetadata("damage", damage);
                    if (e.getConsumable().getItemMeta().getPersistentDataContainer().has(NamespacedKey.minecraft("spell"), PersistentDataType.STRING)) {
                        String spell = e.getConsumable().getItemMeta().getPersistentDataContainer().get(NamespacedKey.minecraft("spell"), PersistentDataType.STRING);
                        e.getProjectile().setMetadata("spell", new FixedMetadataValue(mmorpg, spell));
                    }
                    ((Arrow) e.getProjectile()).setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                    orionPlayer.onShootArrow(e);
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
        if (e.getEntity() instanceof Player) {
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL || e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || EntityDamageEvent.DamageCause.FIRE == e.getCause() || EntityDamageEvent.DamageCause.LAVA == e.getCause() || EntityDamageEvent.DamageCause.FIRE_TICK == e.getCause())
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void onExpChange(PlayerExpChangeEvent e) {
        Player p = e.getPlayer();
        OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(p.getUniqueId());
        orionPlayer.addExperience(p, e.getAmount());
        e.setAmount(0);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(e.getPlayer().getUniqueId());
        float speed = (200 + orionPlayer.getStats().getSpeed()) / Stats.StatsInfo.SPEED.getCap();
        e.getPlayer().setWalkSpeed((speed > 1 ? 1 : (speed < -1 ? -1 : speed)));
    }

    @EventHandler
    public void onPlayerSwitchSlot(PlayerItemHeldEvent e) {
        OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(e.getPlayer().getUniqueId());
        orionPlayer.onSwitchSlot(e.getPlayer(), orionPlayer, e);
        if(e.getPlayer().getInventory().getItem(e.getNewSlot()) == null) return;
        if(orionPlayer.getSlotTypes()[e.getNewSlot()] == OrionPlayer.SlotType.USE_SLOT) {
            Block block = e.getPlayer().getTargetBlock(null, 10);
            Bukkit.getPluginManager().callEvent(new PlayerInteractEvent(e.getPlayer(), Action.RIGHT_CLICK_AIR, e.getPlayer().getInventory().getItem(e.getNewSlot()), block, ( block != null ? block.getFace(block) : null)));
            e.getPlayer().getInventory().setHeldItemSlot(e.getPreviousSlot());
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onHungerDeplete(FoodLevelChangeEvent e) {
        e.setCancelled(true);
        e.setFoodLevel(19);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (e.getHitEntity() != null)
            Utils.sendDebugMessage((Entity) e.getEntity().getShooter(), e.getHitEntity().getName());
        if (e.getHitBlock() != null)
            Utils.sendDebugMessage((Entity) e.getEntity().getShooter(), e.getHitBlock() + "");
        if (e.getEntity().hasMetadata("spell")) {
            if (e.getEntity().getShooter() instanceof Player) {
                Player p = (Player) e.getEntity().getShooter();
                Location location = (e.getHitBlock() != null ? e.getHitBlock().getLocation().clone().add(0, 1.1, 0) : e.getHitEntity().getLocation().clone().add(0, 0.1, 0));
                location.setDirection(p.getEyeLocation().getDirection());
                try {
                    Spell s = (Spell) SpellList.valueOf(e.getEntity().getMetadata("spell").get(0).asString()).getSpellClass().getConstructors()[0].newInstance();
                    s.setCaster(p);
                    s.launch(mmorpg, location, new Knowledge(200));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                    ex.printStackTrace();
                }
            }
        }

        if(e.getEntity().getShooter() instanceof Player) {
            Player p = (Player) e.getEntity().getShooter();
            OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(p.getUniqueId());
            if(e.getHitEntity() != null && e.getEntity() instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity) e.getHitEntity();
                orionPlayer.updateMobHealthBar(entity.getHealth(), entity.getMaxHealth(), entity.getName());
            }

        }
        e.getEntity().remove();
    }
}
