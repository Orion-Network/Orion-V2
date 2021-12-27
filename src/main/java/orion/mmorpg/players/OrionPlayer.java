package orion.mmorpg.players;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.events.OnSpellSense;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.SpellList;
import orion.mmorpg.players.abilities.Support;
import orion.mmorpg.players.races.Race;
import orion.mmorpg.utils.Infos;
import orion.mmorpg.utils.Stats;
import orion.mmorpg.utils.Utils;

import java.util.*;

public class OrionPlayer {
//TODO Add leveling system
    private Mode playerMode = Mode.PLAYER;
    private Infos infos;
    private Stats stats;
    private UUID uniqueId;

    private double health;
    private int statsPoints, level, mana;
    private LinkedHashSet<String> portalStones;

    private ItemStack[] equipements;

    transient private KeyedBossBar healthBar;
    transient private KeyedBossBar mobHealthBar = Bukkit.createBossBar(NamespacedKey.minecraft("mobhealth"), "HealthBar", BarColor.PURPLE, BarStyle.SEGMENTED_10);;
    transient LinkedHashMap<String, Spell> spells = new LinkedHashMap<>();
    transient LinkedHashMap<String, Long> cooldownSpells;
    transient LinkedHashMap<Support, Long> supportMap;
    String[] selectedSpells;

    private Support support;
    private Race race;

    public OrionPlayer(Infos infos, Stats stats, UUID uniqueId, int statsPoints, int level) {
        this.infos = infos;
        this.stats = stats;
        this.uniqueId = uniqueId;
        this.health = stats.getVitality();
        this.statsPoints = statsPoints;
        this.level = level;
        this.portalStones = new LinkedHashSet<>();
        portalStones.add(PortalStone.SPAWN_STONE.getId());

        this.equipements = new ItemStack[4];

        this.mana = (int) this.getStats().getIntel();

        this.healthBar = Bukkit.createBossBar(NamespacedKey.minecraft("health"), "Vie", BarColor.RED, BarStyle.SEGMENTED_10);
        this.healthBar.setProgress(1);
        this.healthBar.addPlayer(Objects.requireNonNull(Bukkit.getPlayer(uniqueId)));
        this.healthBar.setVisible(true);
        this.mobHealthBar.setProgress(1);
        this.mobHealthBar.addPlayer(Objects.requireNonNull(Bukkit.getPlayer(uniqueId)));
        this.mobHealthBar.setVisible(false);

        //SpellList.learnSpell(this, "BERSERKMODE");
        cooldownSpells = new LinkedHashMap<>();
        selectedSpells = new String[9];

        supportMap = new LinkedHashMap<>();
    }

    public OrionPlayer(Infos infos, Stats stats, UUID uniqueId, double health, int statsPoints, int level, int mana, LinkedHashSet<String> portalStones, ItemStack[] equipements, Set<String> spells, Support support, Race race) {
        this.infos = infos;
        this.stats = stats;
        this.uniqueId = uniqueId;
        this.health = health;
        this.statsPoints = statsPoints;
        this.level = level;
        this.mana = mana;
        this.portalStones = portalStones;
        this.equipements = equipements;
        spells.forEach(spellID ->  SpellList.learnSpell(this, spellID));
        this.support = support;
        this.race = race;

        this.healthBar = Bukkit.createBossBar(NamespacedKey.minecraft("health"), "Vie", BarColor.RED, BarStyle.SEGMENTED_10);
        this.healthBar.setProgress(1);
        this.healthBar.addPlayer(Objects.requireNonNull(Bukkit.getPlayer(uniqueId)));
        this.healthBar.setVisible(true);
        this.mobHealthBar.setProgress(1);
        this.mobHealthBar.addPlayer(Objects.requireNonNull(Bukkit.getPlayer(uniqueId)));
        this.mobHealthBar.setVisible(false);

        cooldownSpells = new LinkedHashMap<>();
        selectedSpells = new String[9];

        supportMap = new LinkedHashMap<>();
    }

    public void updateHealthbar() {
        this.healthBar.setProgress(getHealth()/getStats().getVitality() < 1 ? getHealth()/getStats().getVitality() : 1);
    }

    public void updateMobHealthBar(double mobHealth, double maxHealth, String title) {
        Bukkit.broadcastMessage(mobHealth + " h");
        if(mobHealth<=0) {
            this.mobHealthBar.setVisible(false);
            this.mobHealthBar.setProgress(1);
        } else {
            this.mobHealthBar.setVisible(true);
            this.mobHealthBar.setTitle(title);
            this.mobHealthBar.setProgress(mobHealth/maxHealth < 1 ? mobHealth/maxHealth : 1);
        }
    }

    public Infos getInfos() {
        return infos;
    }

    public void setInfos(Infos infos) {
        this.infos = infos;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public int getStatsPoints() {
        return statsPoints;
    }

    public void setStatsPoints(int statsPoints) {
        this.statsPoints = statsPoints;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public LinkedHashSet<String> getPortalStones() {
        return portalStones;
    }

    public void addPortalStone(String id) {
        this.getPortalStones().add(id);
    }

    public ItemStack[] getEquipements() {
        return equipements;
    }

    public void setEquipements(ItemStack[] equipements) {
        this.equipements = equipements;
    }

    public Mode getPlayerMode() {
        return playerMode;
    }

    public void setPlayerMode(Mode playerMode) {
        this.playerMode = playerMode;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public KeyedBossBar getHealthBar() {
        return healthBar;
    }

    public void setHealthBar(KeyedBossBar healthBar) {
        this.healthBar = healthBar;
    }

    public LinkedHashMap<String, Spell> getSpells() {
        return spells;
    }

    public LinkedHashMap<String, Long> getCooldownSpells() {
        return cooldownSpells;
    }

    public String[] getSelectedSpells() {
        return selectedSpells;
    }

    public Support getSupport() {
        return support;
    }

    public void setSupport(Support support) {
        this.support = support;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public LinkedHashMap<Support, Long> getSupportMap() {
        return supportMap;
    }

    public void levelUp(Player p, int level) {
        this.setLevel(level);
        Utils.sendInfoMessage(p, "Bravo, vous êtes monté de niveau !");
    }

    public void heal(double health) {
        this.setHealth(this.getHealth() + health < this.getStats().getVitality() ? this.getHealth() + health : this.getStats().getVitality());
        this.updateHealthbar();
    }

    //TODO Respawn
    public void respawn() {
        Player p = Bukkit.getPlayer(getUniqueId());
        heal(this.getStats().getVitality());
        Utils.sendInfoMessage(p, "Vous êtes mort.");
    }

    public void onInteract(PlayerInteractEvent event) {
        if(this.getSupport() != null)
            this.getSupport().onInteract(event);

        for(Support support : supportMap.keySet()) {
            if(supportMap.get(support) < System.currentTimeMillis())
                supportMap.remove(support);
            else
                support.onInteract(event);
        }
    };
    public void onLaunchSpell(Player player, OrionPlayer stats, Spell s) {
        if(this.getSupport() != null)
            this.getSupport().onLaunchSpell(player, stats, s);

        for(Support support : supportMap.keySet()) {
            if(supportMap.get(support) < System.currentTimeMillis())
                supportMap.remove(support);
            else
                support.onLaunchSpell(player, stats, s);
        }
    };
    public void onHit(EntityDamageByEntityEvent event, OrionPlayer stats) {
        if(this.getSupport() != null)
            this.getSupport().onHit(event, stats);

        for(Support support : supportMap.keySet()) {
            if(supportMap.get(support) < System.currentTimeMillis())
                supportMap.remove(support);
            else {
                support.onHit(event, stats);
            }
        }
    };
    public void onShootArrow(EntityShootBowEvent event) {
        if(this.getSupport() != null)
            this.getSupport().onShootArrow(event);

        for(Support support : supportMap.keySet()) {
            if(supportMap.get(support) < System.currentTimeMillis())
                supportMap.remove(support);
            else
                support.onShootArrow(event);
        }
    };
    public void onHitByEntity(EntityDamageByEntityEvent entityEvent) {
        if(this.getSupport() != null)
            this.getSupport().onHitByEntity(entityEvent);

        for(Support support : supportMap.keySet()) {
            if(supportMap.get(support) < System.currentTimeMillis())
                supportMap.remove(support);
            else
                support.onHitByEntity(entityEvent);
        }
    };
    /*public void onHitBySpell(onHitBySpell e, Player player, PlayerStats stats, Entity caster, SpellType type, Spell s) {};*/
    public void onSpellSense(OnSpellSense e) {
        if(this.getSupport() != null)
            this.getSupport().onSpellSense(e);

        for(Support support : supportMap.keySet()) {
            if(supportMap.get(support) < System.currentTimeMillis())
                supportMap.remove(support);
            else
                support.onSpellSense(e);
        }
    };
    public void onTick() {
        if(mana < this.getStats().getIntel() - this.getStats().getIntel()/30)
            mana += this.getStats().getIntel()/30;

        if(this.getSupport() != null)
            this.getSupport().onTick();

        for(Support support : supportMap.keySet()) {
            if(supportMap.get(support) < System.currentTimeMillis())
                supportMap.remove(support);
            else
                support.onTick();
        }
    };
    public void onDie() {
        if(this.getSupport() != null)
            this.getSupport().onDie();

        for(Support support : supportMap.keySet()) {
            if(supportMap.get(support) < System.currentTimeMillis())
                supportMap.remove(support);
            else
                support.onDie();
        }

        respawn();
    };


}
