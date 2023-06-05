package orion.mmorpg.players;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.effect.SphereEffect;
import fr.mrmicky.fastboard.FastBoard;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.entities.Mounts;
import orion.mmorpg.entities.Pets;
import orion.mmorpg.events.OnSpellSense;
import orion.mmorpg.entities.samples.AbstractPet;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.SpellList;
import orion.mmorpg.players.abilities.Support;
import orion.mmorpg.players.jobs.Jobs;
import orion.mmorpg.players.parties.Party;
import orion.mmorpg.players.races.Race;
import orion.mmorpg.utils.*;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * A class representing a Player in Orion.
 */
public class OrionPlayer {
    private final LinkedHashSet<String> portalStones;
    transient private final KeyedBossBar mobHealthBar = Bukkit.createBossBar(Namespaces.mobhealth, "HealthBar", BarColor.PURPLE, BarStyle.SEGMENTED_10);
    transient LinkedHashMap<Class<? extends Spell>, Knowledge> knownSpells = new LinkedHashMap<>();
    ItemStack[] quiver;
    transient LinkedHashMap<Class<? extends Spell>, Long> cooldownSpells;
    transient LinkedHashMap<Support, Long> supportMap;
    transient Party party;
    transient HashMap<String, Effect> effectHashMap = new HashMap<>();
    public SlotType[] slotTypes;
    String[] selectedSpells;
    private Mode playerMode = Mode.PLAYER;
    private Infos infos;
    private Stats stats;
    private UUID uniqueId;
    private double health, exp;
    private int level, mana;
    private Equipments equipements;
    transient private KeyedBossBar healthBar;
    transient FastBoard fastBoard;
    Set<Pets> tamedPets;
    transient AbstractPet pet;
    Set<Mounts> tamedMounts;
    private Support support;
    private Race race;

    private Jobs jobs;

    /**
     * Default Constructor of OrionPlayer class used on first logging.
     *
     * @param infos       Infos of the new player {@link Infos}
     * @param stats   Base stats of the new player {@link Stats}
     * @param uniqueId    UniqueId of the new player {@link Player#getUniqueId()}
     * @param level       Level of the new player
     */
    public OrionPlayer(Infos infos, Stats stats, UUID uniqueId,int level, double exp) {
        this.infos = infos;
        this.uniqueId = uniqueId;
        this.stats = stats;
        this.equipements = new Equipments();
        equipements.setOwner(Bukkit.getPlayer(uniqueId));
        this.health = this.stats.get(Stats.StatsInfo.VITALITY);
        this.level = level;
        this.exp = exp;
        this.portalStones = new LinkedHashSet<>();
        portalStones.add(PortalStone.CAMPEMENT_STONE.getId());
        portalStones.add(PortalStone.ARENA_SPAWN_STONE.getId());
        portalStones.add(PortalStone.ARENA_STONE.getId());

        this.mana = (int) this.stats.get(Stats.StatsInfo.INTELLIGENCE);

        this.initHealthBar();
        this.mobHealthBar.setProgress(1);
        this.mobHealthBar.addPlayer(Objects.requireNonNull(Bukkit.getPlayer(uniqueId)));
        this.mobHealthBar.setVisible(false);

        cooldownSpells = new LinkedHashMap<>();
        selectedSpells = new String[9];
        quiver = new ItemStack[9];
        tamedPets = new LinkedHashSet<>();
        tamedMounts = new LinkedHashSet<>();

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
        this.jobs = Jobs.getBaseJobs();
    }

    /**
     * Default Constructor of OrionPlayer class used on new logging retrieving data from Database.
     *
     * @param infos        Retrieved Infos of the player
     * @param stats    Retrieved Stats of the player
     * @param uniqueId     Retrieved UniqueId of the player
     * @param health       Health before the player log out
     * @param level        Retrieved Level
     * @param mana         Retrieved mana
     * @param portalStones Retrieved Portal Stones
     * @param equipements  Retrieved Equipments
     * @param spells       Retrieved Spells
     * @param support      Retrieved Support
     * @param race         Retrieved Race
     */
    public OrionPlayer(Infos infos, Stats stats, UUID uniqueId, double health, int level, double exp, int mana, LinkedHashSet<String> portalStones, Equipments equipements, LinkedHashMap<Class<? extends Spell>, Knowledge> spells, String[] selectedSpells, ItemStack[] quiver, SlotType[] slotTypes, Support support, Race race, Jobs jobs, Set<Pets> tamedPets, Set<Mounts> tamedMounts) {
        this.infos = infos;
        this.uniqueId = uniqueId;
        this.stats = stats;
        this.equipements = equipements;
        equipements.setOwner(Bukkit.getPlayer(uniqueId));
        this.health = health;
        this.level = level;
        this.exp = exp;
        this.mana = mana;
        this.portalStones = portalStones;
        spells.forEach((spellClass, knowledge) -> getKnownSpells().put(spellClass, knowledge));
        this.support = support;
        this.race = race;

        this.initHealthBar();
        this.mobHealthBar.setProgress(1);
        this.mobHealthBar.addPlayer(Objects.requireNonNull(Bukkit.getPlayer(uniqueId)));
        this.mobHealthBar.setVisible(false);

        cooldownSpells = new LinkedHashMap<>();
        this.selectedSpells = selectedSpells;
        this.quiver = quiver;
        this.slotTypes = slotTypes;
        this.tamedPets = tamedPets;
        this.tamedMounts = tamedMounts;

        supportMap = new LinkedHashMap<>();
        fastBoard = new FastBoard(Bukkit.getPlayer(uniqueId));
        this.jobs = jobs;
    }

    public void initHealthBar() {
        //Check if bossbar exist
        Bukkit.getBossBars().forEachRemaining(bossbar -> {
            if (bossbar.getKey().getKey().equals(uniqueId.toString())) {
                this.healthBar = bossbar;
            }
        });
        if(healthBar == null)
            this.healthBar = Bukkit.createBossBar(NamespacedKey.fromString(uniqueId.toString(), Mmorpg.INSTANCE), "Vie", BarColor.RED, BarStyle.SEGMENTED_10);
        this.healthBar.setProgress(1);
        if(!this.healthBar.getPlayers().contains(Bukkit.getPlayer(uniqueId)))
            this.healthBar.addPlayer(Objects.requireNonNull(Bukkit.getPlayer(uniqueId)));
        this.healthBar.setVisible(true);
    }

    public static OrionPlayer generateSamplePlayer(Player p) {
        Infos infos = new Infos(p.getName(), LocalDateTime.now(), LocalDateTime.now(), p.getPlayerTime());
        Stats stats = new Stats.Builder().setBase(Stats.StatsInfo.STRENGTH, 1)
                .setBase(Stats.StatsInfo.CRITICAL_CHANCE, 10)
                .setBase(Stats.StatsInfo.CRITICAL_DAMAGE, 50)
                .setBase(Stats.StatsInfo.ATTACK_SPEED, 100)
                .setBase(Stats.StatsInfo.VITALITY, 20)
                .setBase(Stats.StatsInfo.INTELLIGENCE, 0)
                .setBase(Stats.StatsInfo.DEFENCE, 0)
                .setBase(Stats.StatsInfo.SPEED, 25)
                .setBase(Stats.StatsInfo.LUCK, 0)
                .build();
        return new OrionPlayer(infos, stats, p.getUniqueId(), 0, 0);
    }

    public void updateHealthbar() {
        this.healthBar.setTitle("Vie : " + (int) this.getHealth() + "/" + (int) this.getStats().get(Stats.StatsInfo.VITALITY));
        double healthValue = this.getHealth() / this.getStats().get(Stats.StatsInfo.VITALITY);
        if(healthValue > 1)
            this.healthBar.setProgress(1);
        else if (healthValue < 0)
            this.healthBar.setProgress(0);
        else
            this.healthBar.setProgress(healthValue);
    }

    public void updateMobHealthBar(double mobHealth, double maxHealth, String title) {
        /*
        if (mobHealth <= 0) {
            this.mobHealthBar.setVisible(false);
            this.mobHealthBar.setProgress(1);
        } else {
            this.mobHealthBar.setVisible(true);
            this.mobHealthBar.setTitle(title);
            this.mobHealthBar.setProgress(mobHealth / maxHealth < 1 ? mobHealth / maxHealth : 1);
        }*/
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


    public void removePortalStone(String id) {
        this.getPortalStones().remove(id);
    }

    public Equipments getEquipements() {
        return equipements;
    }

    public void setEquipements(Equipments equipements) {
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

    public LinkedHashMap<Class<? extends Spell>, Knowledge> getKnownSpells() {
        return knownSpells;
    }

    public LinkedHashMap<Class<? extends Spell>, Long> getCooldownSpells() {
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

    public Set<Pets> getTamedPets() {
        return tamedPets;
    }

    public Set<Mounts> getTamedMounts() {
        return tamedMounts;
    }

    public AbstractPet getPet() {
        return pet;
    }

    public void setPet(AbstractPet pet) {
        if(this.pet != null)
            this.pet.setRemoved(Entity.RemovalReason.a);
        this.pet = pet;
    }

    public Support getSupport() {
        return support;
    }

    public boolean setSupport(Class<? extends Support> supportClass) {
        Support support;
        try {
            support = (Support) supportClass.getConstructors()[0].newInstance();
            setSupport(support);
            return true;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setSupport(Support support) {
        if(this.support != null)
            this.support.onDeselected(Bukkit.getPlayer(this.uniqueId), this);
        this.support = support;
        support.onSelected(Bukkit.getPlayer(this.getUniqueId()), this);
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

    public Jobs getJobs() {
        return jobs;
    }

    public void heal(double health) {
        this.setHealth(this.getHealth() + health < this.getStats().get(Stats.StatsInfo.VITALITY) ? this.getHealth() + health : this.getStats().get(Stats.StatsInfo.VITALITY));
        this.updateHealthbar();
    }

    public void respawn() {
        Player p = Bukkit.getPlayer(getUniqueId());
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 2, 1));
        p.teleport(PortalStone.ARENA_SPAWN_STONE.getLocation());
        setHealth(this.getStats().get(Stats.StatsInfo.VITALITY));
        Utils.sendInfoMessage(p, "Vous êtes mort.");
        updateHealthbar();
        this.mobHealthBar.setVisible(false);
    }

    public void addExperience(Player p, double exp) {
        this.setExp(this.getExp() + exp);
        if(this.getExp() >= this.getExpToNextLevel()) {
            this.setExp(this.getExp() - this.getExpToNextLevel());
            this.setLevel(this.getLevel() + 1);
            SphereEffect e = new SphereEffect(Mmorpg.effectManager);
            e.particle = Particle.FIREWORKS_SPARK;
            e.duration = 700;
            e.radius = 1;
            e.radiusIncrease = 0.3;
            e.setLocation(p.getLocation());
            e.start();
            if(this.getRace().getLearningSpells().containsKey(this.getLevel())) {
                Class<? extends Spell> spellClass = this.getRace().getLearningSpells().get(this.getLevel());
                SpellList.learnSpell(this, spellClass, 100);
                Utils.sendInfoMessage(p, "Vous avez appris le sort ");
            }
            Utils.sendInfoMessage(p, "You have leveled up to level " + this.getLevel() + " !");
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

    public void onSwitchSlot(Player player, OrionPlayer stats, PlayerItemHeldEvent event) {
        if (this.getSupport() != null)
            this.getSupport().onSwitchSlot(player, stats, event);

        for (Support support : supportMap.keySet()) {
            if (supportMap.get(support) < System.currentTimeMillis())
                supportMap.remove(support);
            else
                support.onSwitchSlot(player, stats, event);
        }
    }

    public void onTick() {
        updateFastboard();
        if (mana < this.getStats().get(Stats.StatsInfo.INTELLIGENCE) - this.getStats().get(Stats.StatsInfo.INTELLIGENCE) / 30)
            mana += this.getStats().get(Stats.StatsInfo.INTELLIGENCE) / 30;

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
        board.add(" ");
        if(this.getRace() != null)
            board.add(ChatColor.WHITE + "Race : " + ChatColor.GOLD + this.getRace().getName());
        board.add(ChatColor.WHITE + "Niveau : " + ChatColor.GOLD + this.getLevel() + " (" + (int) ((getExp() / getExpToNextLevel())*100) + "%)" );
        board.add(ChatColor.YELLOW + "Quête : " + ChatColor.GRAY + " TBI ");

        if(party != null) {
            board.add(" ");
            board.add(ChatColor.YELLOW + "Groupe : ");
            party.getMembers().forEach(p -> board.add("- " + Bukkit.getPlayer(p.getUniqueId()).getDisplayName() + " : " + (int)p.getHealth() + "/" + (int)p.getStats().get(Stats.StatsInfo.VITALITY)));
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
        return (Math.pow(this.getLevel(), 1.3) * 100) + 25;
    }

    public enum SlotType {
        FREE_SLOT,
        USE_SLOT,
        RESTRICTED_SLOT
    }
}
