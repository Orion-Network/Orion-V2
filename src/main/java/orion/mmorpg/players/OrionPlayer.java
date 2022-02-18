package orion.mmorpg.players;

import de.slikey.effectlib.Effect;
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
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.events.OnSpellSense;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.SpellList;
import orion.mmorpg.players.abilities.Support;
import orion.mmorpg.players.parties.Party;
import orion.mmorpg.players.races.Race;
import orion.mmorpg.utils.Infos;
import orion.mmorpg.utils.Stats;
import orion.mmorpg.utils.Utils;

import java.util.*;

/**
 * A class representing a Player in Orion.
 */
public class OrionPlayer {
//TODO Add leveling system
    private Mode playerMode = Mode.PLAYER;
    private Infos infos;
    private Stats baseStats;
    private final Stats stats;
    private UUID uniqueId;

    private double health;
    private int statsPoints, level, mana;
    private final LinkedHashSet<String> portalStones;

    private ItemStack[] equipements;

    transient private KeyedBossBar healthBar;
    transient private final KeyedBossBar mobHealthBar = Bukkit.createBossBar(NamespacedKey.minecraft("mobhealth"), "HealthBar", BarColor.PURPLE, BarStyle.SEGMENTED_10);
    transient LinkedHashMap<String, Spell> spells = new LinkedHashMap<>();
    transient LinkedHashMap<String, Long> cooldownSpells;
    transient LinkedHashMap<Support, Long> supportMap;
    transient Party party;

    transient HashMap<String, Effect> effectHashMap = new HashMap<>();
    String[] selectedSpells;

    private Support support;
    private Race race;

    /**
     * Default Constructor of OrionPlayer class used on first logging.
     * @param infos Infos of the new player {@link Infos}
     * @param baseStats Base stats of the new player {@link Stats}
     * @param uniqueId UniqueId of the new player {@link Player#getUniqueId()}
     * @param statsPoints Default stats point of new player
     * @param level Level of the new player
     */
    public OrionPlayer(Infos infos, Stats baseStats, UUID uniqueId, int statsPoints, int level) {
        this.infos = infos;
        this.uniqueId = uniqueId;
        this.baseStats = baseStats;
        this.equipements = new ItemStack[4];
        this.stats = calculateStats();
        this.health = stats.getVitality();
        this.statsPoints = statsPoints;
        this.level = level;
        this.portalStones = new LinkedHashSet<>();
        portalStones.add(PortalStone.SPAWN_STONE.getId());

        this.mana = (int) this.stats.getIntel();

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

    /**
     * Default Constructor of OrionPlayer class used on new logging retrieving data from Database.
     * @param infos Retrieved Infos of the player
     * @param baseStats Retrieved Stats of the player
     * @param uniqueId Retrieved UniqueId of the player
     * @param health Health before the player log out
     * @param statsPoints Retrieved Stats Points
     * @param level Retrieved Level
     * @param mana Retrieved mana
     * @param portalStones Retrieved Portal Stones
     * @param equipements Retrieved Equipments
     * @param spells Retrieved Spells
     * @param support Retrieved Support
     * @param race Retrieved Race
     */
    public OrionPlayer(Infos infos, Stats baseStats, UUID uniqueId, double health, int statsPoints, int level, int mana, LinkedHashSet<String> portalStones, ItemStack[] equipements, Set<String> spells, Support support, Race race) {
        this.infos = infos;
        this.uniqueId = uniqueId;
        this.baseStats = baseStats;
        this.equipements = equipements;
        this.stats = calculateStats();
        this.health = health;
        this.statsPoints = statsPoints;
        this.level = level;
        this.mana = mana;
        this.portalStones = portalStones;
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
        this.healthBar.setProgress(getHealth()/ getStats().getVitality() < 1 ? getHealth()/ getStats().getVitality() : 1);
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

    public Stats calculateStats(Stats... stats) {
        if(stats.length == 0) {
            Stats playerStats = this.getBaseStats();
            Stats[] armorStatsIter = new Stats[4];
            int i = 0;
            for (ItemStack equipement : equipements) {
                if(equipement != null)
                    armorStatsIter[i] = Mmorpg.getOrionGson().getGSON().fromJson(equipement.getItemMeta().getPersistentDataContainer().get(NamespacedKey.minecraft("stats"), PersistentDataType.STRING), Stats.class);
                i++;
            }
            Stats armorStats = calculateStats(armorStatsIter);
            Stats handStats = new Stats(0,0,0,0,0,0,0,0,0);
            ItemStack item = Bukkit.getPlayer(this.getUniqueId()).getInventory().getItemInMainHand();
            if(item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(NamespacedKey.minecraft("stats"), PersistentDataType.STRING)) {
                handStats = Mmorpg.getOrionGson().getGSON().fromJson(item.getItemMeta().getPersistentDataContainer().get(NamespacedKey.minecraft("stats"), PersistentDataType.STRING), Stats.class);
            }
            return calculateStats(playerStats, armorStats, handStats);
        }
        Stats.Builder builder = new Stats.Builder();
        for(Stats stat : stats) {
            if(stat == null)
                continue;
            builder.withStrength(builder.getStrength()+stat.getStrength());
            builder.withCritical_chance(builder.getCritical_chance()+stat.getCritical_chance());
            builder.withCritical_damage(builder.getCritical_damage()+stat.getCritical_damage());
            builder.withAttack_speed(builder.getAttack_speed()+stat.getAttack_speed());
            builder.withVitality(builder.getVitality()+stat.getVitality());
            builder.withIntel(builder.getIntel()+stat.getIntel());
            builder.withDefence(builder.getDefence()+stat.getDefence());
            builder.withSpeed(builder.getSpeed()+stat.getSpeed());
            builder.withLuck(builder.getLuck()+stat.getLuck());
        }
        return builder.build();
    }

    public Stats getBaseStats() {
        return baseStats;
    }

    public void setBaseStats(Stats baseStats) {
        this.baseStats = baseStats;
    }

    public Stats getStats() {
        return calculateStats();
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

    public HashMap<String, Effect> getEffectHashMap() {
        return effectHashMap;
    }

    public void setEffectHashMap(HashMap<String, Effect> effectHashMap) {
        this.effectHashMap = effectHashMap;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party p) {
        this.party = p;
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
    }

    public void onLaunchSpell(Player player, OrionPlayer stats, Spell s) {
        if(this.getSupport() != null)
            this.getSupport().onLaunchSpell(player, stats, s);

        for(Support support : supportMap.keySet()) {
            if(supportMap.get(support) < System.currentTimeMillis())
                supportMap.remove(support);
            else
                support.onLaunchSpell(player, stats, s);
        }
    }

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
    }

    public void onShootArrow(EntityShootBowEvent event) {
        if(this.getSupport() != null)
            this.getSupport().onShootArrow(event);

        for(Support support : supportMap.keySet()) {
            if(supportMap.get(support) < System.currentTimeMillis())
                supportMap.remove(support);
            else
                support.onShootArrow(event);
        }
    }

    public void onHitByEntity(EntityDamageByEntityEvent entityEvent) {
        if(this.getSupport() != null)
            this.getSupport().onHitByEntity(entityEvent);

        for(Support support : supportMap.keySet()) {
            if(supportMap.get(support) < System.currentTimeMillis())
                supportMap.remove(support);
            else
                support.onHitByEntity(entityEvent);
        }
    }

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
    }

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
    }

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
    }


}
