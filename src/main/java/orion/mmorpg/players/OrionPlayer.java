package orion.mmorpg.players;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import jdk.nashorn.internal.objects.LinkedMap;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.SpellList;
import orion.mmorpg.utils.Infos;
import orion.mmorpg.utils.Stats;
import orion.mmorpg.utils.Utils;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.UUID;

public class OrionPlayer {

    private Mode playerMode = Mode.PLAYER;
    private Infos infos;
    private Stats stats;
    private UUID uniqueId;

    private double health;
    private int statsPoints, level;
    private LinkedList<String> portalStones;

    private ItemStack[] equipements;

    transient private KeyedBossBar healthBar;
    transient LinkedHashMap<String, Spell> spells;
    transient LinkedHashMap<String, Long> cooldownSpells;
    String[] selectedSpells;

    public OrionPlayer(Infos infos, Stats stats, UUID uniqueId, int statsPoints, int level) {
        this.infos = infos;
        this.stats = stats;
        this.uniqueId = uniqueId;
        this.health = stats.getVitality();
        this.statsPoints = statsPoints;
        this.level = level;
        this.portalStones = new LinkedList<>();
        portalStones.add(PortalStone.SPAWN_STONE.getId());

        this.equipements = new ItemStack[4];

        this.healthBar = Bukkit.createBossBar(NamespacedKey.minecraft("health"), "Vie", BarColor.RED, BarStyle.SEGMENTED_10);
        this.healthBar.setProgress(1);
        this.healthBar.addPlayer(Objects.requireNonNull(Bukkit.getPlayer(uniqueId)));
        this.healthBar.setVisible(true);

        spells = new LinkedHashMap<>();
        SpellList.learnSpell(this, "TEST_SPELL");
        cooldownSpells = new LinkedHashMap<>();
        selectedSpells = new String[9];
    }

    public void updateHealthbar() {
        this.healthBar.setProgress(getHealth()/getStats().getVitality() < 1 ? getHealth()/getStats().getVitality() : 1);
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

    public LinkedList<String> getPortalStones() {
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

    public void respawn() {
        Player p = Bukkit.getPlayer(getUniqueId());
        this.setHealth(this.getStats().getVitality());
        this.updateHealthbar();
        Utils.sendInfoMessage(p, "Vous êtes mort.");
    }

    public void onDie() {
    }
}
