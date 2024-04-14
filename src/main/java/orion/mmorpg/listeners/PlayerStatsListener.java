package orion.mmorpg.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.events.OnSpellSense;
import orion.mmorpg.events.PlayerDamageEntityEvent;
import orion.mmorpg.events.PlayerDamagedByEntityEvent;
import orion.mmorpg.events.RespawnEvent;
import orion.mmorpg.items.Modifiers;
import orion.mmorpg.items.enchants.CustomEnchants;
import orion.mmorpg.items.sample.*;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.SpellList;
import orion.mmorpg.players.jobs.Alchemist;
import orion.mmorpg.players.jobs.JobType;
import orion.mmorpg.utils.*;

import java.lang.reflect.InvocationTargetException;

public class PlayerStatsListener implements Listener {
    Mmorpg mmorpg;

    public PlayerStatsListener(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(mmorpg,
                () -> mmorpg.getPlayerManager().getPlayersMap().values().forEach(player -> {
                    if (Bukkit.getPlayer(player.getUniqueId()) == null)
                        return;
                    player.onTick();
                }), 0, 20);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
        orionPlayer.executeOEffect(event);
        Player p = event.getPlayer();
        p.getAttribute(Attribute.GENERIC_ATTACK_SPEED)
                .setBaseValue(orionPlayer.getStats().get(Stats.StatsInfo.ATTACK_SPEED) / 20);

        if (event.hasBlock() && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (event.getClickedBlock().getType().equals(Material.SMITHING_TABLE)) {
                event.setCancelled(true);
                // TODO Correct gui to give back items if closed
                // ((Smithing) orionPlayer.getJobs().getJob(JobType.SMITHING)).getUpgradeGui().show(event.getPlayer());
            } else if (event.getClickedBlock().getType().equals(Material.BREWING_STAND)) {
                event.setCancelled(true);
                ((Alchemist) orionPlayer.getJobs().getJob(JobType.ALCHEMIST)).getBrewingPane().show(event.getPlayer());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerHitEntity(EntityDamageByEntityEvent e) {
        if (e.isCancelled())
            return;
        if (e.getDamager() instanceof Player) {
            OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(e.getDamager().getUniqueId());
            Bukkit.getPluginManager().callEvent(new PlayerDamageEntityEvent(e));
        }
        if (e.getDamager() instanceof Arrow || e.getDamager() instanceof DragonFireball
                || e.getDamager() instanceof Fireball) {
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
        p.getAttribute(Attribute.GENERIC_ATTACK_SPEED)
                .setBaseValue(orionPlayer.getStats().get(Stats.StatsInfo.ATTACK_SPEED) / 20);
        if (p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInMainHand().getItemMeta()
                .getPersistentDataContainer().has(Namespaces.internal_id, PersistentDataType.STRING)) {
            ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
            PersistentDataContainer container = meta.getPersistentDataContainer();
            if (container.has(Namespaces.damage, PersistentDataType.FLOAT))
                e.setDamage(container.get(Namespaces.damage, PersistentDataType.FLOAT));

            if (meta.hasEnchant(CustomEnchants.WRATH)) {
                e.setDamage(e.getDamage() + (e.getDamage() * (1 + (meta.getEnchantLevel(CustomEnchants.WRATH) * 0.1))));
                Utils.sendInfoMessage(p, "Wrath: " + (1 + (meta.getEnchantLevel(CustomEnchants.WRATH) * 0.1)));
            }
            if (mmorpg.getItemManager().getItem(container.get(Namespaces.internal_id, PersistentDataType.STRING))
                    .getSuperclass().equals(BasicMeleeWeapon.class)) {
                float totalDamage = Utils.calculateDamage(orionPlayer.getStats(), (float) e.getDamage())
                        * p.getAttackCooldown();
                Utils.sendDebugMessage(p, "Total damage: " + totalDamage);
                e.setDamage(totalDamage);
            }
        }
    }

    @EventHandler
    public void playerDamagedByEntity(PlayerDamagedByEntityEvent e) {

        Player p = e.getPlayer();
        OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(p.getUniqueId());
        orionPlayer.executeOEffect(e);

        if (e.isCancelled())
            return;
        float reduction = Utils.calculateDamageReduction(orionPlayer.getStats().get(Stats.StatsInfo.DEFENCE));
        orionPlayer.setHealth(orionPlayer.getHealth() - (e.getDamage() / reduction));

        if (orionPlayer.getHealth() <= 0)
            orionPlayer.respawn();
        else
            orionPlayer.updateHealthbar();

        Utils.sendDebugMessage(p,
                "Base Damage : " + e.getDamage() + " Defense : " + reduction + " Calc. Damages : "
                        + e.getDamage() / reduction + " health : " + orionPlayer.getHealth() + " / "
                        + orionPlayer.getStats().get(Stats.StatsInfo.VITALITY));
        e.setDamage(0);
    }

    @EventHandler
    public void useBow(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(e.getEntity().getUniqueId());
            if (p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInMainHand().getItemMeta()
                    .getPersistentDataContainer().has(Namespaces.internal_id, PersistentDataType.STRING)) {
                PersistentDataContainer container = p.getInventory().getItemInMainHand().getItemMeta()
                        .getPersistentDataContainer();
                if (BasicRangedWeapon.class.isAssignableFrom(mmorpg.getItemManager()
                        .getItem(container.get(Namespaces.internal_id, PersistentDataType.STRING)))) {
                    BasicRangedWeapon basicRangedWeapon = (BasicRangedWeapon) mmorpg.getItemManager()
                            .createItem(container.get(Namespaces.internal_id, PersistentDataType.STRING));
                    float arrowDamage = 1;
                    if (e.getConsumable().getItemMeta().getPersistentDataContainer().has(Namespaces.damage,
                            PersistentDataType.FLOAT))
                        arrowDamage = e.getConsumable().getItemMeta().getPersistentDataContainer()
                                .get(Namespaces.damage, PersistentDataType.FLOAT);
                    MetadataValue damage = new FixedMetadataValue(mmorpg,
                            Utils.calculateDamage(orionPlayer.getStats(), basicRangedWeapon.getDAMAGE() + arrowDamage));
                    e.getProjectile().setMetadata("damage", damage);
                    if (e.getConsumable().getItemMeta().getPersistentDataContainer().has(Namespaces.spell,
                            PersistentDataType.STRING)) {
                        String spell = e.getConsumable().getItemMeta().getPersistentDataContainer()
                                .get(Namespaces.spell, PersistentDataType.STRING);
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
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL
                    || e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK
                    || EntityDamageEvent.DamageCause.FIRE == e.getCause()
                    || EntityDamageEvent.DamageCause.LAVA == e.getCause())
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

        if (orionPlayer.getClasse() == null) {
            Utils.sendInfoMessage(e.getPlayer(), "Vous devez choisir une classe dans votre /menu");
            e.setCancelled(true);
            return;
        }

        float speed = (200 + orionPlayer.getStats().get(Stats.StatsInfo.SPEED)) / Stats.StatsInfo.SPEED.getCap();
        e.getPlayer().setWalkSpeed((speed > 1 ? 1 : (speed < -1 ? -1 : speed)));
    }

    @EventHandler
    public void onPlayerSwitchSlot(PlayerItemHeldEvent e) {

        OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(e.getPlayer().getUniqueId());
        orionPlayer.executeOEffect(e);

        orionPlayer.getStats().removeModifiers("MAIN_HAND");

        if (e.getPlayer().getInventory().getItem(e.getNewSlot()) != null
                && e.getPlayer().getInventory().getItem(e.getNewSlot()).hasItemMeta()) {
            BasicItem basicItem = mmorpg.getItemManager()
                    .getItemFromItemstack(e.getPlayer().getInventory().getItem(e.getNewSlot()));
            if (basicItem instanceof Modifiers) {
                orionPlayer.getStats().addModifiers("MAIN_HAND",
                        ((BasicEquipmentItem) basicItem).getModifiersWithReforge());
            }
        }

        if (e.getPlayer().getInventory().getItem(e.getNewSlot()) == null)
            return;
        if (orionPlayer.getSlotTypes()[e.getNewSlot()] == OrionPlayer.SlotType.USE_SLOT) {
            Block block = e.getPlayer().getTargetBlock(null, 10);
            Bukkit.getPluginManager()
                    .callEvent(new PlayerInteractEvent(e.getPlayer(), Action.RIGHT_CLICK_AIR,
                            e.getPlayer().getInventory().getItem(e.getNewSlot()), block,
                            (block != null ? block.getFace(block) : null)));
            e.getPlayer().getInventory().setHeldItemSlot(e.getPreviousSlot());
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void playerSwapHand(PlayerSwapHandItemsEvent e) {
        OrionPlayer orionPlayer = OrionPlayer.getOrionPlayer(e.getPlayer());

        orionPlayer.getStats().removeModifiers("OFF_HAND");
        orionPlayer.getStats().removeModifiers("MAIN_HAND");

        if (e.getMainHandItem() != null && e.getMainHandItem().hasItemMeta()) {
            BasicItem basicItem = mmorpg.getItemManager().getItemFromItemstack(e.getMainHandItem());
            if (basicItem instanceof Modifiers) {
                orionPlayer.getStats().addModifiers("MAIN_HAND",
                        ((BasicEquipmentItem) basicItem).getModifiersWithReforge());
            }
        }

        if (e.getOffHandItem() != null && e.getOffHandItem().hasItemMeta()) {
            BasicItem basicItem = mmorpg.getItemManager().getItemFromItemstack(e.getOffHandItem());
            if (basicItem instanceof Modifiers) {
                orionPlayer.getStats().addModifiers("OFF_HAND",
                        ((BasicEquipmentItem) basicItem).getModifiersWithReforge());
            }
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
                Location location = (e.getHitBlock() != null ? e.getHitBlock().getLocation().clone().add(0, 1.1, 0)
                        : e.getHitEntity().getLocation().clone().add(0, 0.1, 0));
                location.setDirection(p.getEyeLocation().getDirection());
                try {
                    Spell s = (Spell) SpellList.valueOf(e.getEntity().getMetadata("spell").get(0).asString())
                            .getSpellClass().getConstructors()[0].newInstance();
                    s.setCaster(p);
                    s.launch(mmorpg, location, new Knowledge(200));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                    ex.printStackTrace();
                }
            }
        }
        e.getEntity().remove();
    }

    @EventHandler
    public void onRespawn(RespawnEvent event) {
        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 2, 1));
        // p.teleport(Mmorpg.INSTANCE.getWorldManager().getPortalStone("SPAWN").getLocation()); delegate
        // to OrionWorld
        event.getPlayer().teleport(new Location(Bukkit.getWorld("world"), 400, 25, 925));
        Utils.sendInfoMessage(event.getPlayer(), "Vous êtes mort.");
        event.getOrionPlayer().setHealth(event.getOrionPlayer().getStats().get(Stats.StatsInfo.VITALITY));
        event.getOrionPlayer().updateHealthbar();
    }
}
