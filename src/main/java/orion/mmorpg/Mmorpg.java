package orion.mmorpg;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.ticxo.modelengine.api.ModelEngineAPI;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
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
import orion.mmorpg.items.enchants.CustomEnchants;
import orion.mmorpg.items.enchants.EnchantmentWrapper;
import orion.mmorpg.listeners.*;
import orion.mmorpg.managers.*;
import orion.mmorpg.modelengine.OrionScriptReader;
import orion.mmorpg.models.NPCManager;
import orion.mmorpg.mythicsmob.MythicMobListener;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.GameState;
import orion.mmorpg.utils.OrionGson;
import orion.mmorpg.utils.Utils;
import orion.mmorpg.utils.WorldGuardUtils;

import java.io.*;
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
    private MobManager mobManager;
    private MythicMob mythicMob;
    ModelEngineAPI modelEngineAPI;

    @Getter
    WorldManager worldManager;
    ChunkManager chunkManager;

    @Getter
    RessourceManager ressourceManager;
    @Getter
    NPCManager npcManager;

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        Mmorpg.debug = debug;
    }

    public static OrionGson getOrionGson() {
        return orionGson;
    }

    @Override
    public void onLoad() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            registry.register(WorldGuardUtils.Flag.NO_SPELLS);
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get(WorldGuardUtils.Flag.NO_SPELLS.getName());
            if (existing instanceof StateFlag) {
                WorldGuardUtils.Flag.NO_SPELLS = (StateFlag) existing;
            } else {
                e.printStackTrace();
            }
        }

        //transferResourcesToPluginFolder();
    }

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

        PluginManager pm = Bukkit.getPluginManager();
        luckPerms = LuckPermsProvider.get();

        mongoClient = new Client(this);

        playerManager = new PlayerManager(this);
        guildManager = new GuildManager(this);
        worldManager = new WorldManager(this);
        chunkManager = new ChunkManager(this);
        effectManager = new EffectManager(this);
        partyManager = new PartyManager(this);
        ressourceManager = new RessourceManager(this);
        npcManager = new NPCManager(this);

        CustomEnchants.register();

        if(Bukkit.getPluginManager().getPlugin("MythicMobs") == null) {
            mobManager = new MobManager(this);
            Objects.requireNonNull(getCommand("mob")).setExecutor(new MobCommand(this));
            getLogger().log(Level.INFO, "MythicMobs is not installed, enabling MobManager");
        } else {
            mythicMob = MythicBukkit.inst().getMobManager().getMythicMob("OrionMob").orElse(null);
            pm.registerEvents(new MythicMobListener(this), this);
            getLogger().log(Level.WARNING, "MythicMobs is installed, disabling MobManager");
        }

        if(Bukkit.getPluginManager().getPlugin("ModelEngine") == null) {
            getLogger().log(Level.WARNING, "ModelEngine is not installed, disabling ModelEngineAPI");
        } else {
            modelEngineAPI = ModelEngineAPI.api;
            ModelEngineAPI.getScriptReaderRegistry().register("orion", new OrionScriptReader());
            getLogger().log(Level.INFO, "ModelEngine is installed, enabling ModelEngineAPI");
        }

        pm.registerEvents(new PlayerJoinListener(this), this);
        pm.registerEvents(new PlayerPickupListener(this), this);
        pm.registerEvents(new ChunkListener(this), this);
        pm.registerEvents(new InventoryClickListener(this), this);
        pm.registerEvents(new PlayerUseItemEvent(this), this);
        pm.registerEvents(new PlayerStatsListener(this), this);
        pm.registerEvents(new PlayerChatListener(), this);
        pm.registerEvents(new ServerListener(this), this);
        pm.registerEvents(new SpellListener(), this);
        pm.registerEvents(new WorldListener(this), this);
        pm.registerEvents(new FishingListener(this), this);

        Objects.requireNonNull(getCommand("menu")).setExecutor(new MenuCommand(this));
        Objects.requireNonNull(getCommand("data")).setExecutor(new DataCommand(this));
        Objects.requireNonNull(getCommand("item")).setExecutor(new ItemCommand());
        Objects.requireNonNull(getCommand("party")).setExecutor(new PartyCommand(this));
        Objects.requireNonNull(getCommand("admin")).setExecutor(new AdminCommand(this));
        Objects.requireNonNull(getCommand("support")).setExecutor(new SupportCommand(this));
        Objects.requireNonNull(getCommand("pet")).setExecutor(new PetCommand(this));
        Objects.requireNonNull(getCommand("mount")).setExecutor(new MountCommand(this));
        Objects.requireNonNull(getCommand("ressource")).setExecutor(new RessourceCommand(this));

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
        if(mobManager != null) {
            mobManager.loadMobs();
            Bukkit.getScheduler().runTaskTimer(this, mobManager, 20L, 20L);
        }

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
        if(mobManager != null)
            getMobManager().despawnMobs();
        Bukkit.getBossBars().forEachRemaining(BossBar::removeAll);
    }

    private void transferResourcesToPluginFolder() {
        File pluginFolder = this.getDataFolder();
        if (!pluginFolder.exists()) {
            pluginFolder.mkdir();
        }
        for (String fileName : new String[]{"pentagram.png","img/alchemy.png","img/alchemy_1.png","img/alchemy_2.png"}) {
            File file = new File(pluginFolder, fileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    InputStream in = this.getClass().getResourceAsStream("/" + fileName);
                    OutputStream out = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                    in.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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

    public MythicMob getMythicMob() {
        return mythicMob;
    }

    public ModelEngineAPI getModelEngineAPI() {
        return modelEngineAPI;
    }
}
