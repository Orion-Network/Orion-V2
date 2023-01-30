package orion.mmorpg;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.boss.BossBar;
import orion.mmorpg.config.AssociationConfig;
import de.slikey.effectlib.EffectManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import orion.mmorpg.commands.*;
import orion.mmorpg.database.Client;
import orion.mmorpg.listeners.*;
import orion.mmorpg.managers.*;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.GameState;
import orion.mmorpg.utils.OrionGson;
import orion.mmorpg.utils.Utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.logging.Level;

public final class Mmorpg extends JavaPlugin {

    public static Mmorpg INSTANCE;
    private GameState gameState = GameState.STARTING;
    public static boolean debug;
    public static OrionGson orionGson = new OrionGson();
    public static EffectManager effectManager;
    public static LuckPerms luckPerms;
    public final FileConfiguration config = this.getConfig();
    Client mongoClient;
    PlayerManager playerManager;

    GuildManager guildManager;
    PartyManager partyManager;
    MobManager mobManager;
    ChunkManager chunkManager;

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        Mmorpg.debug = debug;
    }

    public static OrionGson getOrionGson() {
        return orionGson;
    }

    /**
     * Create default configuration or load it, initialize managers, register listeners and commands when plugin is enabled.
     */
    @Override
    public void onEnable() {
        INSTANCE = this;

        config.addDefault("mongo.username", "root");
        config.addDefault("mongo.password", "");
        config.addDefault("mongo.auth", "mongo");
        config.addDefault("misc.debugging", false);

        config.options().copyDefaults(true);
        saveConfig();

        setDebug(config.getBoolean("misc.debugging"));

        AssociationConfig.setup(this);

        mongoClient = new Client(this);

        playerManager = new PlayerManager(this);
        guildManager = new GuildManager(this);
        chunkManager = new ChunkManager(this);
        effectManager = new EffectManager(this);
        partyManager = new PartyManager(this);
        mobManager = new MobManager(this);

        PluginManager pm = Bukkit.getPluginManager();
        luckPerms = LuckPermsProvider.get();

        pm.registerEvents(new PlayerJoinListener(this), this);
        pm.registerEvents(new PlayerPickupListener(this), this);
        pm.registerEvents(new ChunkListener(this), this);
        pm.registerEvents(new InventoryClickListener(this), this);
        pm.registerEvents(new PlayerUseItemEvent(this), this);
        pm.registerEvents(new PlayerStatsListener(this), this);
        pm.registerEvents(new PlayerChatListener(), this);
        pm.registerEvents(new ServerListener(this), this);

        Objects.requireNonNull(getCommand("menu")).setExecutor(new MenuCommand(this));
        Objects.requireNonNull(getCommand("data")).setExecutor(new DataCommand(this));
        Objects.requireNonNull(getCommand("item")).setExecutor(new ItemCommand());
        Objects.requireNonNull(getCommand("party")).setExecutor(new PartyCommand(this));
        Objects.requireNonNull(getCommand("admin")).setExecutor(new AdminCommand(this));
        Objects.requireNonNull(getCommand("mob")).setExecutor(new MobCommand(this));
        Objects.requireNonNull(getCommand("support")).setExecutor(new SupportCommand(this));

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (getMongoClient().hasPlayer(p.getUniqueId())) {
                try {
                    OrionPlayer orionPlayer = getMongoClient().loadPlayer(p.getUniqueId());
                    getPlayerManager().addPlayer(p.getUniqueId(), orionPlayer);
                    orionPlayer.updateHealthbar();
                    Utils.sendInfoMessage(p, "Data loaded !");
                    if (orionPlayer.getRace() == null)
                        p.sendTitle("Bienvenu", "Selectionnez votre race via le /menu", 10, 70, 20);
                    return;
                } catch (IOException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
                    ex.printStackTrace();
                    p.kickPlayer("Error in loading");
                }
            } else {
                OrionPlayer orionPlayer = OrionPlayer.generateSamplePlayer(p);
                getPlayerManager().addPlayer(p.getUniqueId(), orionPlayer);
                p.sendTitle("Bienvenu", "Selectionnez votre classe via le /menu", 10, 70, 20);
            }
        }
        mobManager.loadMobs();
        Bukkit.getScheduler().runTaskTimer(this, mobManager, 20L, 20L);

        setGameState(GameState.RUNNING);
    }

    /**
     * Save orion.mmorpg.config when plugin is disabled
     */
    @Override
    public void onDisable() {
        for (OrionPlayer p : getPlayerManager().getPlayersMap().values()) {
            JavaPlugin.getPlugin(Mmorpg.class).getLogger().log(Level.FINE, "Saving : " + p.getUniqueId().toString());
            p.getHealthBar().removeAll();
            getMongoClient().savePlayer(p);
            getPlayerManager().getPlayersMap().remove(p.getUniqueId());
        }

        getMobManager().despawnMobs();
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public GuildManager getGuildManager() {
        return guildManager;
    }

    public ChunkManager getChunkManager() {
        return chunkManager;
    }

    public PartyManager getPartyManager() {
        return partyManager;
    }

    public MobManager getMobManager() {
        return mobManager;
    }

    public static LuckPerms getLuckPerms() {
        return luckPerms;
    }

    public Client getMongoClient() {
        return mongoClient;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
