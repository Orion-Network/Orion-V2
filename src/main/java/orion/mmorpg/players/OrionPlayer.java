package orion.mmorpg.players;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.effect.SphereEffect;
import fr.mrmicky.fastboard.FastBoard;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.entities.Mounts;
import orion.mmorpg.entities.Pets;
import orion.mmorpg.entities.samples.AbstractPet;
import orion.mmorpg.players.abilities.OEffect;
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
    @Getter
    private final LinkedHashSet<String> portalStones;
    transient private final KeyedBossBar mobHealthBar = Bukkit.createBossBar(Namespaces.mobhealth, "HealthBar", BarColor.PURPLE, BarStyle.SEGMENTED_10);
    @Getter
    transient LinkedHashMap<Class<? extends Spell>, Knowledge> knownSpells = new LinkedHashMap<>();
    @Getter
    ItemStack[] quiver;
    @Getter
    transient LinkedHashMap<Class<? extends Spell>, Long> cooldownSpells;
    /**
     * A map of all the effects of the player.
     * <p>
     *     The first key is the event name
     *     The second key is a map of all the effects of the event
     *      The key is the id of the effect
     *      The value is the effect
     */
    @Getter
    transient LinkedHashMap<String, LinkedHashMap<String, OEffect>> orionEffects;
    @Getter@Setter
    transient Party party;
    @Getter@Setter
    transient HashMap<String, Effect> effectHashMap = new HashMap<>();
    @Getter
    public SlotType[] slotTypes;
    @Getter
    String[] selectedSpells;
    @Getter
    private Mode playerMode = Mode.PLAYER;
    @Getter@Setter
    private Infos infos;
    @Getter@Setter
    private Stats stats;
    @Getter
    private UUID uniqueId;
    @Getter@Setter
    private double health, exp;
    @Getter@Setter
    private int level, mana;
    @Getter@Setter
    private Equipments equipements;
    @Getter
    transient private KeyedBossBar healthBar;
    @Getter@Setter
    transient FastBoard fastBoard;
    @Getter
    Set<Pets> tamedPets;
    @Getter
    transient AbstractPet pet;
    @Getter
    Set<Mounts> tamedMounts;
    private Support mainSupport;
    @Getter@Setter
    private Race race;

    @Getter
    private Jobs jobs;

    {
        this.orionEffects = new LinkedHashMap<>();
    }

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

        fastBoard = new FastBoard(Bukkit.getPlayer(uniqueId));
        this.jobs = Jobs.getBaseJobs();
    }

    /**
     * Default Constructor of OrionPlayer class used on logging retrieving data from Database.
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
     * @param mainSupport      Retrieved Support
     * @param race         Retrieved Race
     */
    public OrionPlayer(Infos infos, Stats stats, UUID uniqueId, double health, int level, double exp, int mana, LinkedHashSet<String> portalStones, Equipments equipements, LinkedHashMap<Class<? extends Spell>, Knowledge> spells, String[] selectedSpells, ItemStack[] quiver, SlotType[] slotTypes, Support mainSupport, Race race, Jobs jobs, Set<Pets> tamedPets, Set<Mounts> tamedMounts) {
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
        this.mainSupport = mainSupport;
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


    public void addPortalStone(String id) {
        this.getPortalStones().add(id);
    }


    public void removePortalStone(String id) {
        this.getPortalStones().remove(id);
    }


    public void setPet(AbstractPet pet) {
        if(this.pet != null)
            this.pet.setRemoved(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
        this.pet = pet;
    }

    public Support getMainSupport() {
        return mainSupport;
    }

    public boolean setMainSupport(Class<? extends Support> supportClass) {
        Support support;
        try {
            support = (Support) supportClass.getConstructors()[0].newInstance();
            setMainSupport(support);
            return true;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setMainSupport(Support mainSupport) {
        if(this.mainSupport != null) {
            this.mainSupport.removeEffects(this);
        }
        this.mainSupport = mainSupport;
        if(this.mainSupport == null)
            return;
        mainSupport.applyEffects(this, -1);
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
                Utils.sendInfoMessage(p, "Vous avez appris un nouveau sort !");
            }
            Utils.sendInfoMessage(p, "Vous etes montes au niveau :  " + this.getLevel() + " !");
        }

    }

    public void addEffect(String id, OEffect effect) {
        if(!this.getOrionEffects().containsKey(effect.getEvent().getSimpleName()))
            this.getOrionEffects().put(effect.getEvent().getSimpleName(), new LinkedHashMap<>());
        this.getOrionEffects().get(effect.getEvent().getSimpleName()).put(id, effect);
    }

    public void removeEffect(String id, Class<? extends Event> event) {
        if(!this.getOrionEffects().containsKey(event.getSimpleName()))
            return;
        this.getOrionEffects().get(event.getSimpleName()).remove(id);
    }

    public void executeOEffect(Event event) {
        if(!this.getOrionEffects().containsKey(event.getClass().getSimpleName()))
            return;
        this.getOrionEffects().get(event.getEventName()).forEach((id, effect) -> {
            boolean remove = effect.execute(event);
            if(!remove)
                this.getOrionEffects().get(event.getEventName()).remove(id);
        });
    }

    public void onTick() {
        updateFastboard();
        if (mana < this.getStats().get(Stats.StatsInfo.INTELLIGENCE) - this.getStats().get(Stats.StatsInfo.INTELLIGENCE) / 30)
            mana += this.getStats().get(Stats.StatsInfo.INTELLIGENCE) / 30;
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

    public double getExpToNextLevel() {
        return (Math.pow(this.getLevel(), 1.3) * 100) + 25;
    }

    public enum SlotType {
        FREE_SLOT,
        USE_SLOT,
        RESTRICTED_SLOT
    }

    public static OrionPlayer getOrionPlayer(Player player) {
        return Mmorpg.INSTANCE.getPlayerManager().getPlayer(player.getUniqueId());
    }
}
