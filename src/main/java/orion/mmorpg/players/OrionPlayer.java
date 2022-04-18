package orion.mmorpg.players;

import de.slikey.effectlib.Effect;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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

import java.time.LocalDateTime;
import java.util.*;

/**
 * A class representing a Player in Orion.
 */
public class OrionPlayer {
    private final Stats stats;
    private final LinkedHashSet<String> portalStones;
    transient private final KeyedBossBar mobHealthBar = Bukkit.createBossBar(NamespacedKey.minecraft("mobhealth"), "HealthBar", BarColor.PURPLE, BarStyle.SEGMENTED_10);
    transient LinkedHashMap<String, Spell> spells = new LinkedHashMap<>();
    ItemStack[] quiver;
    transient LinkedHashMap<String, Long> cooldownSpells;
    transient LinkedHashMap<Support, Long> supportMap;
    transient Party party;
    transient HashMap<String, Effect> effectHashMap = new HashMap<>();
    public SlotType[] slotTypes;
    String[] selectedSpells;
    //TODO Add leveling system
    private Mode playerMode = Mode.PLAYER;
    private Infos infos;
    private Stats baseStats;
    private UUID uniqueId;
    private double health, exp;
    private int statsPoints, spentStatsPoint, level, mana;
    private ItemStack[] equipements;
    transient private KeyedBossBar healthBar;
    transient FastBoard fastBoard;
    private Support support;
    private Race race;

    /**
     * Default Constructor of OrionPlayer class used on first logging.
     *
     * @param infos       Infos of the new player {@link Infos}
     * @param baseStats   Base stats of the new player {@link Stats}
     * @param uniqueId    UniqueId of the new player {@link Player#getUniqueId()}
     * @param statsPoints Default stats point of new player
     * @param level       Level of the new player
     */
    public OrionPlayer(Infos infos, Stats baseStats, UUID uniqueId, int statsPoints, int spentStatsPoint, int level, double exp) {
        this.infos = infos;
        this.uniqueId = uniqueId;
        this.baseStats = baseStats;
        this.equipements = new ItemStack[4];
        this.stats = calculateStats();
        this.health = stats.getVitality();
        this.statsPoints = statsPoints;
        this.spentStatsPoint = spentStatsPoint;
        this.level = level;
        this.exp = exp;
        this.portalStones = new LinkedHashSet<>();
        portalStones.add(PortalStone.SPAWN_STONE.getId());
        portalStones.add(PortalStone.ARENA_STONE.getId());

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
        quiver = new ItemStack[9];

        this.slotTypes = new SlotType[]{SlotType.FREE_SLOT,
                SlotType.FREE_SLOT,
                SlotType.FREE_SLOT,
                SlotType.FREE_SLOT,
                SlotType.FREE_SLOT,
                SlotType.FREE_SLOT,
                SlotType.FREE_SLOT,
                SlotType.FREE_SLOT,
                SlotType.USE_SLOT};

        supportMap = new LinkedHashMap<>();
        fastBoard = new FastBoard(Bukkit.getPlayer(uniqueId));
    }

    /**
     * Default Constructor of OrionPlayer class used on new logging retrieving data from Database.
     *
     * @param infos        Retrieved Infos of the player
     * @param baseStats    Retrieved Stats of the player
     * @param uniqueId     Retrieved UniqueId of the player
     * @param health       Health before the player log out
     * @param statsPoints  Retrieved Stats Points
     * @param level        Retrieved Level
     * @param mana         Retrieved mana
     * @param portalStones Retrieved Portal Stones
     * @param equipements  Retrieved Equipments
     * @param spells       Retrieved Spells
     * @param support      Retrieved Support
     * @param race         Retrieved Race
     */
    public OrionPlayer(Infos infos, Stats baseStats, UUID uniqueId, double health, int statsPoints, int spentStatsPoint, int level, double exp, int mana, LinkedHashSet<String> portalStones, ItemStack[] equipements, Set<String> spells, String[] selectedSpells, ItemStack[] quiver, SlotType[] slotTypes, Support support, Race race) {
        this.infos = infos;
        this.uniqueId = uniqueId;
        this.baseStats = baseStats;
        this.equipements = equipements;
        this.stats = calculateStats();
        this.health = health;
        this.statsPoints = statsPoints;
        this.spentStatsPoint = spentStatsPoint;
        this.level = level;
        this.exp = exp;
        this.mana = mana;
        this.portalStones = portalStones;
        spells.forEach(spellID -> SpellList.learnSpell(this, spellID));
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
        this.selectedSpells = selectedSpells;
        this.quiver = quiver;
        this.slotTypes = slotTypes;

        supportMap = new LinkedHashMap<>();
        fastBoard = new FastBoard(Bukkit.getPlayer(uniqueId));
    }

    public static OrionPlayer generateSamplePlayer(Player p) {
        Infos infos = new Infos(p.getName(), LocalDateTime.now(), LocalDateTime.now(), p.getPlayerTime());
        Stats stats = new Stats(1, 10, 50, 100, 20, 0, 0, 25, 0);
        return new OrionPlayer(infos, stats, p.getUniqueId(), 0, 0, 0, 0);
    }

    public void updateHealthbar() {
        this.healthBar.setTitle("Vie : " + (int) this.getHealth() + "/" + (int) this.stats.getVitality());
        double healthValue = this.getHealth() / this.stats.getVitality();
        if(healthValue > 1)
            this.healthBar.setProgress(1);
        else if (healthValue < 0)
            this.healthBar.setProgress(0);
        else
            this.healthBar.setProgress(healthValue);
    }

    public void updateMobHealthBar(double mobHealth, double maxHealth, String title) {
        if (mobHealth <= 0) {
            this.mobHealthBar.setVisible(false);
            this.mobHealthBar.setProgress(1);
        } else {
            this.mobHealthBar.setVisible(true);
            this.mobHealthBar.setTitle(title);
            this.mobHealthBar.setProgress(mobHealth / maxHealth < 1 ? mobHealth / maxHealth : 1);
        }
    }

    public Infos getInfos() {
        return infos;
    }

    public void setInfos(Infos infos) {
        this.infos = infos;
    }

    public Stats calculateStats(Stats... stats) {
        if (stats.length == 0) {
            Stats playerStats = this.getBaseStats();
            Stats[] armorStatsIter = new Stats[4];
            int i = 0;
            for (ItemStack equipement : equipements) {
                if (equipement != null)
                    armorStatsIter[i] = Mmorpg.getOrionGson().getGSON().fromJson(equipement.getItemMeta().getPersistentDataContainer().get(NamespacedKey.minecraft("stats"), PersistentDataType.STRING), Stats.class);
                i++;
            }
            Stats armorStats = calculateStats(armorStatsIter);
            Stats handStats = new Stats(0, 0, 0, 0, 0, 0, 0, 0, 0);
            ItemStack item = Bukkit.getPlayer(this.getUniqueId()).getInventory().getItemInMainHand();
            if (item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(NamespacedKey.minecraft("stats"), PersistentDataType.STRING)) {
                handStats = Mmorpg.getOrionGson().getGSON().fromJson(item.getItemMeta().getPersistentDataContainer().get(NamespacedKey.minecraft("stats"), PersistentDataType.STRING), Stats.class);
            }
            return calculateStats(playerStats, armorStats, handStats);
        }
        Stats.Builder builder = new Stats.Builder();
        for (Stats stat : stats) {
            if (stat == null)
                continue;
            builder.withStrength(builder.getStrength() + stat.getStrength());
            builder.withCritical_chance(builder.getCritical_chance() + stat.getCritical_chance());
            builder.withCritical_damage(builder.getCritical_damage() + stat.getCritical_damage());
            builder.withAttack_speed(builder.getAttack_speed() + stat.getAttack_speed());
            builder.withVitality(builder.getVitality() + stat.getVitality());
            builder.withIntel(builder.getIntel() + stat.getIntel());
            builder.withDefence(builder.getDefence() + stat.getDefence());
            builder.withSpeed(builder.getSpeed() + stat.getSpeed());
            builder.withLuck(builder.getLuck() + stat.getLuck());
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

    public int getSpentStatsPoint() {
        return spentStatsPoint;
    }

    public void setSpentStatsPoint(int spentStatsPoint) {
        this.spentStatsPoint = spentStatsPoint;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getExp() {
        return exp;
    }

    public void setExp(double exp) {
        this.exp = exp;
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

    public ItemStack[] getQuiver() {
        return quiver;
    }

    public SlotType[] getSlotTypes() {
        return slotTypes;
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

    public FastBoard getFastBoard() {
        return fastBoard;
    }

    public void setFastBoard(FastBoard fastBoard) {
        this.fastBoard = fastBoard;
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
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 2, 1));
        p.teleport(PortalStone.SPAWN_STONE.getLocation());
        setHealth(this.getStats().getVitality());
        Utils.sendInfoMessage(p, "Vous êtes mort.");
        updateHealthbar();
        this.mobHealthBar.setVisible(false);
    }

    public void addExperience(Player p, double exp) {
        this.setExp(this.getExp() + exp);
        if(this.getExp() >= this.getExpToNextLevel()) {
            this.setExp(this.getExp() - this.getExpToNextLevel());
            this.setLevel(this.getLevel() + 1);
            Utils.sendInfoMessage(p, "You have leveled up to level " + this.getLevel() + " !");
            if(this.getLevel() % 3 == 0 && this.getLevel() > this.getStatsPoints()) {
                this.setStatsPoints(this.getStatsPoints() + 3);
                Utils.sendInfoMessage(p, "You have gained 3 stats points !");
            }
        }

    }

    public void onInteract(PlayerInteractEvent event) {
        if (this.getSupport() != null)
            this.getSupport().onInteract(event);

        for (Support support : supportMap.keySet()) {
            if (supportMap.get(support) < System.currentTimeMillis())
                supportMap.remove(support);
            else
                support.onInteract(event);
        }
    }

    public void onLaunchSpell(Player player, OrionPlayer stats, Spell s) {
        if (this.getSupport() != null)
            this.getSupport().onLaunchSpell(player, stats, s);

        for (Support support : supportMap.keySet()) {
            if (supportMap.get(support) < System.currentTimeMillis())
                supportMap.remove(support);
            else
                support.onLaunchSpell(player, stats, s);
        }
    }

    public void onHit(EntityDamageByEntityEvent event, OrionPlayer stats) {
        if (this.getSupport() != null)
            this.getSupport().onHit(event, stats);

        for (Support support : supportMap.keySet()) {
            if (supportMap.get(support) < System.currentTimeMillis())
                supportMap.remove(support);
            else {
                support.onHit(event, stats);
            }
        }
    }

    public void onShootArrow(EntityShootBowEvent event) {
        if (this.getSupport() != null)
            this.getSupport().onShootArrow(event);

        for (Support support : supportMap.keySet()) {
            if (supportMap.get(support) < System.currentTimeMillis())
                supportMap.remove(support);
            else
                support.onShootArrow(event);
        }
    }

    public void onHitByEntity(EntityDamageByEntityEvent entityEvent) {
        if (this.getSupport() != null)
            this.getSupport().onHitByEntity(entityEvent);

        for (Support support : supportMap.keySet()) {
            if (supportMap.get(support) < System.currentTimeMillis())
                supportMap.remove(support);
            else
                support.onHitByEntity(entityEvent);
        }
    }

    /*public void onHitBySpell(onHitBySpell e, Player player, PlayerStats stats, Entity caster, SpellType type, Spell s) {};*/
    public void onSpellSense(OnSpellSense e) {
        if (this.getSupport() != null)
            this.getSupport().onSpellSense(e);

        for (Support support : supportMap.keySet()) {
            if (supportMap.get(support) < System.currentTimeMillis())
                supportMap.remove(support);
            else
                support.onSpellSense(e);
        }
    }

    public void onTick() {
        updateFastboard();
        if (mana < this.getStats().getIntel() - this.getStats().getIntel() / 30)
            mana += this.getStats().getIntel() / 30;

        if (this.getSupport() != null)
            this.getSupport().onTick();

        for (Support support : supportMap.keySet()) {
            if (supportMap.get(support) < System.currentTimeMillis())
                supportMap.remove(support);
            else
                support.onTick();
        }
    }

    ArrayList<String> board = new ArrayList<>();
    private void updateFastboard() {
        board.clear();
        this.fastBoard.updateTitle("Statistiques");
        if(this.getRace() != null)
            board.add(ChatColor.WHITE + "Race : " + ChatColor.GOLD + this.getRace().getName());
        board.add(ChatColor.WHITE + "Niveau : " + ChatColor.GOLD + this.getLevel() + " (" + (int) ((getExp() / getExpToNextLevel())*100) + "%)" );
        board.add(ChatColor.YELLOW + "Quête : " + ChatColor.GRAY + " TBI ");

        if(party != null) {
            board.add(ChatColor.YELLOW + "Groupe : ");
            party.getPlayers().forEach(p -> board.add(Bukkit.getPlayer(p.getUniqueId()).getDisplayName() + " : " + p.getHealth() + "/" + p.getStats().getVitality()));
        }
        this.fastBoard.updateLines(board);
    }

    public void onDie() {
        if (this.getSupport() != null)
            this.getSupport().onDie();

        for (Support support : supportMap.keySet()) {
            if (supportMap.get(support) < System.currentTimeMillis())
                supportMap.remove(support);
            else
                support.onDie();
        }

        respawn();
    }

    public double getExpToNextLevel() {
        return (Math.pow(this.getLevel(), 2) * 100) + 25;
    }

    public enum SlotType {
        FREE_SLOT,
        USE_SLOT
    }
}
