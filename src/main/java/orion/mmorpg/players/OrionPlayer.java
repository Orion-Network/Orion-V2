package orion.mmorpg.players;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.effect.SphereEffect;
import fr.mrmicky.fastboard.FastBoard;
import fr.nexomega.orionmob.entities.Mounts;
import fr.nexomega.orionmob.entities.Pets;
import fr.nexomega.orionmob.entities.samples.AbstractPet;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.events.OnGetEffect;
import orion.mmorpg.events.RespawnEvent;
import orion.mmorpg.players.abilities.OEffect;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.SpellList;
import orion.mmorpg.players.abilities.Support;
import orion.mmorpg.players.jobs.Jobs;
import orion.mmorpg.players.parties.Party;
import orion.mmorpg.players.classes.Classe;
import orion.mmorpg.utils.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * A class representing a Player in Orion.
 */
public class OrionPlayer implements Effectable {
    @Getter
    private final LinkedHashSet<String> portalStones;
    @Getter
    transient final LinkedHashMap<Class<? extends Spell>, Knowledge> knownSpells = new LinkedHashMap<>();
    @Getter
    ItemStack[] quiver;
    @Getter
    transient LinkedHashMap<Class<? extends Spell>, Long> cooldownSpells = new LinkedHashMap<>();

    @Getter
    @Setter
    transient Party party;
    @Getter
    transient final HashMap<String, Effect> displayEffectHashMap = new HashMap<>();
    @Getter
    public SlotType[] slotTypes;
    @Getter
    String[] selectedSpells;
    @Getter
    private Mode playerMode = Mode.PLAYER;
    @Getter
    @Setter
    private Infos infos;
    @Getter
    @Setter
    private Stats stats;
    @Getter
    private UUID uniqueId;
    @Getter
    @Setter
    private double health, exp;
    @Getter
    @Setter
    private int level, mana;
    @Getter
    @Setter
    private Equipments equipements;
    @Getter
    transient private KeyedBossBar healthBar;
    @Getter
    @Setter
    transient FastBoard fastBoard;
    @Getter
    Set<Pets> tamedPets;
    @Getter
    transient AbstractPet pet;
    @Getter
    Set<Mounts> tamedMounts;
    @Getter
    private Support mainSupport;
    @Getter
    @Setter
    private Classe classe;

    @Getter
    private Jobs jobs;

    @Getter
    Currencies currencies;

    /**
     * Default Constructor of OrionPlayer class used on first logging.
     *
     * @param infos
     *            Infos of the new player {@link Infos}
     * @param stats
     *            Base stats of the new player {@link Stats}
     * @param uniqueId
     *            UniqueId of the new player {@link Player#getUniqueId()}
     * @param level
     *            Level of the new player
     */
    public OrionPlayer(Infos infos, Stats stats, UUID uniqueId, int level, double exp) {
        this.infos = infos;
        this.uniqueId = uniqueId;
        this.stats = stats;
        this.equipements = new Equipments();
        equipements.setOwner(Bukkit.getPlayer(uniqueId));
        this.health = this.stats.get(Stats.StatsInfo.VITALITY);
        this.level = level;
        this.exp = exp;
        this.portalStones = new LinkedHashSet<>();
        portalStones.add("CAMPEMENT_STONE");
        portalStones.add("ARENA_SPAWN_STONE");
        portalStones.add("ARENA_STONE");

        this.mana = (int) this.stats.get(Stats.StatsInfo.INTELLIGENCE);

        this.initHealthBar();

        selectedSpells = new String[9];
        quiver = new ItemStack[9];
        tamedPets = new LinkedHashSet<>();
        tamedMounts = new LinkedHashSet<>();

        this.slotTypes = new SlotType[] { SlotType.FREE_SLOT, SlotType.FREE_SLOT, SlotType.FREE_SLOT,
                SlotType.FREE_SLOT, SlotType.FREE_SLOT, SlotType.FREE_SLOT, SlotType.FREE_SLOT, SlotType.FREE_SLOT,
                SlotType.USE_SLOT };

        fastBoard = new FastBoard(Bukkit.getPlayer(uniqueId));
        this.jobs = Jobs.getBaseJobs();
        this.currencies = new Currencies();
    }

    /**
     * Default Constructor of OrionPlayer class used on logging retrieving data from Database.
     *
     * @param infos
     *            Retrieved Infos of the player
     * @param stats
     *            Retrieved Stats of the player
     * @param uniqueId
     *            Retrieved UniqueId of the player
     * @param health
     *            Health before the player log out
     * @param level
     *            Retrieved Level
     * @param mana
     *            Retrieved mana
     * @param portalStones
     *            Retrieved Portal Stones
     * @param equipements
     *            Retrieved Equipments
     * @param spells
     *            Retrieved Spells
     * @param mainSupport
     *            Retrieved Support
     * @param classe
     *            Retrieved Classe
     */
    public OrionPlayer(Infos infos, Stats stats, UUID uniqueId, double health, int level, double exp, int mana,
                       LinkedHashSet<String> portalStones, Equipments equipements,
                       LinkedHashMap<Class<? extends Spell>, Knowledge> spells, String[] selectedSpells, ItemStack[] quiver,
                       SlotType[] slotTypes, Support mainSupport, Classe classe, Jobs jobs, Set<Pets> tamedPets,
                       Set<Mounts> tamedMounts, Currencies currencies) {
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
        this.classe = classe;

        this.initHealthBar();

        this.selectedSpells = selectedSpells;
        this.quiver = quiver;
        this.slotTypes = slotTypes;
        this.tamedPets = tamedPets;
        this.tamedMounts = tamedMounts;

        fastBoard = new FastBoard(Bukkit.getPlayer(uniqueId));
        this.jobs = jobs;
        this.currencies = currencies;
    }

    public void initHealthBar() {
        Bukkit.getBossBars().forEachRemaining(bossbar -> {
            if (bossbar.getKey().getKey().equals(uniqueId.toString())) {
                this.healthBar = bossbar;
            }
        });
        if (healthBar == null)
            this.healthBar = Bukkit.createBossBar(NamespacedKey.fromString(uniqueId.toString(), Mmorpg.INSTANCE), "Vie",
                    BarColor.RED, BarStyle.SEGMENTED_10);
        this.healthBar.setProgress(1);
        if (!this.healthBar.getPlayers().contains(Bukkit.getPlayer(uniqueId)))
            this.healthBar.addPlayer(Objects.requireNonNull(Bukkit.getPlayer(uniqueId)));
        this.healthBar.setVisible(true);

        updateHealthbar();
    }

    public void updateHealthbar() {
        this.healthBar.setTitle(
                "Vie : " + (int) this.getHealth() + "/" + (int) this.getStats().get(Stats.StatsInfo.VITALITY));
        double healthValue = this.getHealth() / this.getStats().get(Stats.StatsInfo.VITALITY);
        if (healthValue > 1)
            this.healthBar.setProgress(1);
        else if (healthValue < 0)
            this.healthBar.setProgress(0);
        else
            this.healthBar.setProgress(healthValue);
    }

    public void addPortalStone(String id) {
        this.getPortalStones().add(id);
    }

    public void removePortalStone(String id) {
        this.getPortalStones().remove(id);
    }

    public void setPet(AbstractPet pet) {
        if (this.pet != null) {
            this.getPet().removePet();
        }
        this.pet = pet;
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
        if (this.mainSupport != null) {
            this.mainSupport.removeEffects(this);
        }
        this.mainSupport = mainSupport;
        if (this.mainSupport == null)
            return;
        mainSupport.applyEffects(this, -1);
    }

    public void heal(double health) {
        this.setHealth(this.getHealth() + health < this.getStats().get(Stats.StatsInfo.VITALITY)
                ? this.getHealth() + health : this.getStats().get(Stats.StatsInfo.VITALITY));
        this.updateHealthbar();
    }

    public void respawn() {
        Player p = Bukkit.getPlayer(getUniqueId());
        Bukkit.getServer().getPluginManager().callEvent(new RespawnEvent(p, this));
    }

    public void addExperience(Player p, double exp) {
        this.setExp(this.getExp() + exp);
        if (this.getExp() >= this.getExpToNextLevel()) {
            this.setExp(this.getExp() - this.getExpToNextLevel());
            this.setLevel(this.getLevel() + 1);
            SphereEffect e = new SphereEffect(Mmorpg.effectManager);
            e.particle = Particle.FIREWORKS_SPARK;
            e.duration = 700;
            e.radius = 1;
            e.radiusIncrease = 0.3;
            e.setLocation(p.getLocation());
            e.start();
            if (this.getClasse().getLearningSpells().containsKey(this.getLevel())) {
                Class<? extends Spell> spellClass = this.getClasse().getLearningSpells().get(this.getLevel());
                SpellList.learnSpell(this, spellClass, 100);
                Utils.sendInfoMessage(p, "Vous avez appris un nouveau sort !");
            }
            Utils.sendInfoMessage(p, "Vous etes montes au niveau :  " + this.getLevel() + " !");
        }

    }

    public void onTick() {
        updateFastboard();
        if (mana < this.getStats().get(Stats.StatsInfo.INTELLIGENCE)
                - this.getStats().get(Stats.StatsInfo.INTELLIGENCE) / 30)
            mana += (int) (this.getStats().get(Stats.StatsInfo.INTELLIGENCE) / 30);
    }

    ArrayList<String> board = new ArrayList<>();

    private void updateFastboard() {
        board.clear();
        this.fastBoard.updateTitle("Statistiques");
        board.add(" ");
        if (this.getClasse() != null)
            board.add(ChatColor.WHITE + "Classe : " + ChatColor.GOLD + this.getClasse().getName());
        board.add(ChatColor.WHITE + "Niveau : " + ChatColor.GOLD + this.getLevel() + " ("
                + (int) ((getExp() / getExpToNextLevel()) * 100) + "%)");
        board.add(
                ChatColor.WHITE + "Expérience : " + ChatColor.GOLD + (int) getExp() + "/" + (int) getExpToNextLevel());
        board.add(" ");
        if (this.getCurrencies().hasCurrency("gold")) {
            Currencies.Currency currency = this.getCurrencies().getCurrency("gold");
            board.add(net.md_5.bungee.api.ChatColor.of(currency.getColor()) + currency.getName() + " : "
                    + ChatColor.GOLD + currency.getAmount() + " " + currency.getSymbol());
        }
        board.add(ChatColor.YELLOW + "Quête : " + ChatColor.GRAY + " TBI ");

        if (party != null) {
            board.add(" ");
            board.add(ChatColor.YELLOW + "Groupe : ");
            party.getPlayers().forEach(p -> board.add("- " + Bukkit.getPlayer(p.getUniqueId()).getDisplayName() + " : "
                    + (int) p.getHealth() + "/" + (int) p.getStats().get(Stats.StatsInfo.VITALITY)));
        }
        this.fastBoard.updateLines(board);
    }

    public double getExpToNextLevel() {
        return (Math.pow(this.getLevel(), 1.3) * 100) + 25;
    }

    @Override
    public void addEffect(String id, OEffect effect) {
        if (!orionEffects.containsKey(effect.getEvent().getSimpleName()))
            orionEffects.put(effect.getEvent().getSimpleName(), new LinkedHashMap<>());
        orionEffects.get(effect.getEvent().getSimpleName()).put(id, effect);

        Bukkit.getPluginManager().callEvent(new OnGetEffect(this, effect, effect.getDuration()));
    }

    public HashMap<Class<? extends Spell>, Knowledge> getUsableSpells() {
        HashMap<Class<? extends Spell>, Knowledge> usableSpells = new HashMap<>(knownSpells);

        if (mainSupport != null) {
            mainSupport.getSpells().forEach((spellClass) -> {
                usableSpells.put(spellClass, new Knowledge(1000));

            });
        }
        return usableSpells;
    }

    public enum SlotType {
        FREE_SLOT, USE_SLOT, RESTRICTED_SLOT
    }

    public static OrionPlayer getOrionPlayer(Player player) {
        return Mmorpg.INSTANCE.getPlayerManager().getPlayer(player.getUniqueId());
    }

    public static final class OrionPlayerBuilder {
        private LinkedHashMap<String, LinkedHashMap<String, OEffect>> orionEffects = new LinkedHashMap<>();
        private LinkedHashSet<String> portalStones = new LinkedHashSet<>();
        private LinkedHashMap<Class<? extends Spell>, Knowledge> knownSpells = new LinkedHashMap<>();
        private ItemStack[] quiver = new ItemStack[9];
        private LinkedHashMap<Class<? extends Spell>, Long> cooldownSpells = new LinkedHashMap<>();
        private Party party;
        private HashMap<String, Effect> displayEffectHashMap = new HashMap<>();
        private SlotType[] slotTypes = new SlotType[] { SlotType.FREE_SLOT, SlotType.FREE_SLOT, SlotType.FREE_SLOT,
                SlotType.FREE_SLOT, SlotType.FREE_SLOT, SlotType.FREE_SLOT, SlotType.FREE_SLOT, SlotType.FREE_SLOT,
                SlotType.USE_SLOT };
        private String[] selectedSpells = new String[9];
        private Mode playerMode;
        private Infos infos;
        private Stats stats;
        private UUID uniqueId;
        private double health;
        private double exp;
        private int level;
        private int mana;
        private Equipments equipements;
        private KeyedBossBar healthBar;
        private Set<Pets> tamedPets = new LinkedHashSet<>();
        private Set<Mounts> tamedMounts = new LinkedHashSet<>();
        private AbstractPet pet;
        private Support mainSupport;
        private Classe classe;
        private Jobs jobs = Jobs.getBaseJobs();
        private Currencies currencies = new Currencies();

        public OrionPlayerBuilder() {
        }

        public static OrionPlayerBuilder anOrionPlayer() {
            return new OrionPlayerBuilder();
        }

        public OrionPlayerBuilder withOrionEffects(LinkedHashMap<String, LinkedHashMap<String, OEffect>> orionEffects) {
            this.orionEffects = orionEffects;
            return this;
        }

        public OrionPlayerBuilder withPortalStones(LinkedHashSet<String> portalStones) {
            this.portalStones = portalStones;
            return this;
        }

        public OrionPlayerBuilder withKnownSpells(LinkedHashMap<Class<? extends Spell>, Knowledge> knownSpells) {
            this.knownSpells = knownSpells;
            return this;
        }

        public OrionPlayerBuilder withQuiver(ItemStack[] quiver) {
            this.quiver = quiver;
            return this;
        }

        public OrionPlayerBuilder withCooldownSpells(LinkedHashMap<Class<? extends Spell>, Long> cooldownSpells) {
            this.cooldownSpells = cooldownSpells;
            return this;
        }

        public OrionPlayerBuilder withParty(Party party) {
            this.party = party;
            return this;
        }

        public OrionPlayerBuilder withDisplayEffectHashMap(HashMap<String, Effect> displayEffectHashMap) {
            this.displayEffectHashMap = displayEffectHashMap;
            return this;
        }

        public OrionPlayerBuilder withSlotTypes(SlotType[] slotTypes) {
            this.slotTypes = slotTypes;
            return this;
        }

        public OrionPlayerBuilder withSelectedSpells(String[] selectedSpells) {
            this.selectedSpells = selectedSpells;
            return this;
        }

        public OrionPlayerBuilder withPlayerMode(Mode playerMode) {
            this.playerMode = playerMode;
            return this;
        }

        public OrionPlayerBuilder withInfos(Infos infos) {
            this.infos = infos;
            return this;
        }

        public OrionPlayerBuilder withStats(Stats stats) {
            this.stats = stats;
            return this;
        }

        public OrionPlayerBuilder withUniqueId(UUID uniqueId) {
            this.uniqueId = uniqueId;
            return this;
        }

        public OrionPlayerBuilder withHealth(double health) {
            this.health = health;
            return this;
        }

        public OrionPlayerBuilder withExp(double exp) {
            this.exp = exp;
            return this;
        }

        public OrionPlayerBuilder withLevel(int level) {
            this.level = level;
            return this;
        }

        public OrionPlayerBuilder withMana(int mana) {
            this.mana = mana;
            return this;
        }

        public OrionPlayerBuilder withEquipements(Equipments equipements) {
            this.equipements = equipements;
            return this;
        }

        public OrionPlayerBuilder withHealthBar(KeyedBossBar healthBar) {
            this.healthBar = healthBar;
            return this;
        }

        public OrionPlayerBuilder withTamedPets(Set<Pets> tamedPets) {
            this.tamedPets = tamedPets;
            return this;
        }

        public OrionPlayerBuilder withPet(AbstractPet pet) {
            this.pet = pet;
            return this;
        }

        public OrionPlayerBuilder withTamedMounts(Set<Mounts> tamedMounts) {
            this.tamedMounts = tamedMounts;
            return this;
        }

        public OrionPlayerBuilder withMainSupport(Support mainSupport) {
            this.mainSupport = mainSupport;
            return this;
        }

        public OrionPlayerBuilder withClasse(Classe classe) {
            this.classe = classe;
            return this;
        }

        public OrionPlayerBuilder withJobs(Jobs jobs) {
            this.jobs = jobs;
            return this;
        }

        public OrionPlayerBuilder withCurrencies(Currencies currencies) {
            this.currencies = currencies;
            return this;
        }

        public OrionPlayer build() {
            OrionPlayer orionPlayer = new OrionPlayer(infos, stats, uniqueId, health, level, exp, mana, portalStones,
                    equipements, knownSpells, selectedSpells, quiver, slotTypes, mainSupport, classe, jobs, tamedPets,
                    tamedMounts, currencies);
            orionPlayer.setHealth(health);
            orionPlayer.setParty(party);
            orionPlayer.setPet(pet);
            orionPlayer.displayEffectHashMap.putAll(this.displayEffectHashMap);
            orionPlayer.playerMode = this.playerMode;
            orionPlayer.cooldownSpells = this.cooldownSpells;
            orionPlayer.healthBar = this.healthBar;
            orionPlayer.initHealthBar();
            orionPlayer.orionEffects.putAll(this.orionEffects);
            return orionPlayer;
        }
    }
}
