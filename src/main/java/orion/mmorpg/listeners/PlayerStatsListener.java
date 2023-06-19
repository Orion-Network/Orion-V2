package orion.mmorpg.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.entities.samples.AbstractMount;
import orion.mmorpg.events.OnSpellSense;
import orion.mmorpg.events.PlayerDamageEntityEvent;
import orion.mmorpg.events.PlayerDamagedByEntityEvent;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.Modifiers;
import orion.mmorpg.items.sample.*;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.SpellList;
import orion.mmorpg.players.jobs.JobType;
import orion.mmorpg.players.jobs.Smithing;
import orion.mmorpg.utils.*;

import java.lang.reflect.InvocationTargetException;

public class PlayerStatsListener implements Listener {
    Mmorpg mmorpg;

    public PlayerStatsListener(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(mmorpg, () -> mmorpg.getPlayerManager().getPlayersMap().values().forEach(player -> {
            if(Bukkit.getPlayer(player.getUniqueId()) == null) return;
            player.onTick();
        }), 0, 20);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
        orionPlayer.executeOEffect(event);
        Player p = event.getPlayer();
        p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(orionPlayer.getStats().get(Stats.StatsInfo.ATTACK_SPEED) / 20);

        if(event.hasBlock() && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if(event.getClickedBlock().getType().equals(Material.SMITHING_TABLE)) {
                event.setCancelled(true);
                ((Smithing)orionPlayer.getJobs().getJob(JobType.SMITHING)).getUpgradeGui().show(event.getPlayer());
            }
        }
    }

    //TODO Remake this mess to divide it into multiple events
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerHitEntity(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) return;
        if (e.getDamager() instanceof Player) {
            OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(e.getDamager().getUniqueId());
            Bukkit.getPluginManager().callEvent(new PlayerDamageEntityEvent(e));
        }
        if (e.getDamager() instanceof Arrow || e.getDamager() instanceof DragonFireball ||e.getDamager() instanceof Fireball) {
            Projectile entity = (Projectile) e.getDamager();
            if (entity.hasMetadata("damage")) {
                e.setDamage(entity.getMetadata("damage").get(0).asFloat());
            }
        }

        if (e.getEntity() instanceof Player) {
            Bukkit.getPluginManager().callEvent(new PlayerDamagedByEntityEvent(e));
        }
    }

    @EventHandler
    public void playerDamageEntityEvent(PlayerDamageEntityEvent e) {
        Player p = e.getPlayer();
        OrionPlayer orionPlayer = e.getOrionPlayer();
        orionPlayer.executeOEffect(e);
        p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(orionPlayer.getStats().get(Stats.StatsInfo.ATTACK_SPEED) / 20);
        if (p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(Namespaces.internal_id, PersistentDataType.STRING)) {
            PersistentDataContainer container = p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer();
            if (container.has(Namespaces.damage, PersistentDataType.FLOAT))
                e.setDamage(container.get(Namespaces.damage, PersistentDataType.FLOAT));
            if (ItemList.valueOf(container.get(Namespaces.internal_id, PersistentDataType.STRING)).getItemClass().getSuperclass().equals(BasicMeleeWeapon.class)) {
                float totalDamage = Utils.calculateDamage(orionPlayer.getStats(), (float) e.getDamage()) * p.getAttackCooldown();
                Utils.sendDebugMessage(p, "Total damage: " + totalDamage);
                e.setDamage(totalDamage);
                if (!(e.getTarget() instanceof Player)) {
                    LivingEntity entity = (LivingEntity) e.getTarget();
                    orionPlayer.updateMobHealthBar(entity.getHealth() - totalDamage, entity.getMaxHealth(), entity.getName());
                } else {
                    OrionPlayer target = mmorpg.getPlayerManager().getPlayer(e.getTarget().getUniqueId());
                    orionPlayer.updateMobHealthBar(target.getHealth() - totalDamage, target.getStats().get(Stats.StatsInfo.VITALITY), e.getTarget().getName());
                }
            }
        }
    }

    @EventHandler
    public void playerDamagedByEntity(PlayerDamagedByEntityEvent e) {

        Player p = e.getPlayer();
        OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(p.getUniqueId());
        orionPlayer.executeOEffect(e);

        if(e.isCancelled()) return;
        float reduction = Utils.calculateDamageReduction(orionPlayer.getStats().get(Stats.StatsInfo.DEFENCE));
        orionPlayer.setHealth(orionPlayer.getHealth() - (e.getDamage() / reduction));

        if (orionPlayer.getHealth() <= 0)
            orionPlayer.respawn();
        else
            orionPlayer.updateHealthbar();

        Utils.sendDebugMessage(p, "Base Damage : " + e.getDamage() + " Defense : " + reduction + " Calc. Damages : " + e.getDamage() / reduction + " health : " + orionPlayer.getHealth() + " / " + orionPlayer.getStats().get(Stats.StatsInfo.VITALITY));
        e.setDamage(0);

    }

    @EventHandler
    public void useBow(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(e.getEntity().getUniqueId());
            if (p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(Namespaces.internal_id, PersistentDataType.STRING)) {
                PersistentDataContainer container = p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer();
                if (BasicRangedWeapon.class.isAssignableFrom(ItemList.valueOf(container.get(Namespaces.internal_id, PersistentDataType.STRING)).getItemClass())) {
                    BasicRangedWeapon basicRangedWeapon = (BasicRangedWeapon) ItemList.valueOf(container.get(Namespaces.internal_id, PersistentDataType.STRING)).generateItem();
                    float arrowDamage = 1;
                    if (e.getConsumable().getItemMeta().getPersistentDataContainer().has(Namespaces.damage, PersistentDataType.FLOAT))
                        arrowDamage = e.getConsumable().getItemMeta().getPersistentDataContainer().get(Namespaces.damage, PersistentDataType.FLOAT);
                    MetadataValue damage = new FixedMetadataValue(mmorpg, Utils.calculateDamage(orionPlayer.getStats(), basicRangedWeapon.getDAMAGE() + arrowDamage));
                    e.getProjectile().setMetadata("damage", damage);
                    if (e.getConsumable().getItemMeta().getPersistentDataContainer().has(Namespaces.spell, PersistentDataType.STRING)) {
                        String spell = e.getConsumable().getItemMeta().getPersistentDataContainer().get(Namespaces.spell, PersistentDataType.STRING);
                        e.getProjectile().setMetadata("spell", new FixedMetadataValue(mmorpg, spell));
                    }
                    ((Arrow) e.getProjectile()).setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                    orionPlayer.executeOEffect(e);
                    basicRangedWeapon.onShootBow(e);
                }
            }
        }
    }

    @EventHandler
    public void onSpellSense(OnSpellSense e) {
        OrionPlayer.getOrionPlayer(e.getTarget()).executeOEffect(e);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL || e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || EntityDamageEvent.DamageCause.FIRE == e.getCause() || EntityDamageEvent.DamageCause.LAVA == e.getCause())
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
        float speed = (200 + orionPlayer.getStats().get(Stats.StatsInfo.SPEED)) / Stats.StatsInfo.SPEED.getCap();
        e.getPlayer().setWalkSpeed((speed > 1 ? 1 : (speed < -1 ? -1 : speed)));
    }

    @EventHandler
    public void onPlayerSwitchSlot(PlayerItemHeldEvent e) {

        OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(e.getPlayer().getUniqueId());
        orionPlayer.executeOEffect(e);

        orionPlayer.getStats().removeModifiers("HAND");

        if(e.getPlayer().getInventory().getItem(e.getNewSlot()) != null && e.getPlayer().getInventory().getItem(e.getNewSlot()).hasItemMeta()) {
            BasicItem basicItem = ItemList.getItemFromItemstack(e.getPlayer().getInventory().getItem(e.getNewSlot()));
            if(basicItem instanceof Modifiers) {
                orionPlayer.getStats().addModifiers("HAND", ((BasicEquipmentItem) basicItem).getModifiersWithReforge());
            }
        }

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

    @EventHandler
    public void onDismount(EntityDismountEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if(((CraftEntity)event.getDismounted() ).getHandle() instanceof AbstractMount) {
                Utils.sendInfoMessage(player, "You have dismounted your mount");
                event.getDismounted().remove();
            }
        }
    }
}
