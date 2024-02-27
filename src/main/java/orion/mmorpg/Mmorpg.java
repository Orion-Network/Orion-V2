package orion.mmorpg;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
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
import orion.mmorpg.listeners.*;
import orion.mmorpg.managers.*;
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
    @Getter
    private GameState gameState = GameState.STARTING;
    @Getter
    public static boolean debug;
    @Getter
    public static OrionGson orionGson = new OrionGson();
    public static EffectManager effectManager;
    @Getter
    public static LuckPerms luckPerms;
    public final FileConfiguration config = this.getConfig();
    @Getter
    Client mongoClient;
    @Getter
    PlayerManager playerManager;

    @Getter
    GuildManager guildManager;
    @Getter
    PartyManager partyManager;
    @Getter
    public ItemManager itemManager;

    public static void setDebug(boolean debug) {
        Mmorpg.debug = debug;
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

        // transferResourcesToPluginFolder();
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
        effectManager = new EffectManager(this);
        partyManager = new PartyManager(this);
        System.out.println("registering items");
        itemManager = new ItemManager(this);

        CustomEnchants.register();

        pm.registerEvents(new PlayerJoinListener(this), this);
        pm.registerEvents(new PlayerPickupListener(this), this);
        pm.registerEvents(new InventoryClickListener(this), this);
        pm.registerEvents(new PlayerUseItemEvent(this), this);
        pm.registerEvents(new PlayerStatsListener(this), this);
        pm.registerEvents(new PlayerChatListener(), this);
        pm.registerEvents(new ServerListener(this), this);
        pm.registerEvents(new SpellEffectListener(), this);

        Objects.requireNonNull(getCommand("menu")).setExecutor(new MenuCommand(this));
        Objects.requireNonNull(getCommand("data")).setExecutor(new DataCommand(this));
        Objects.requireNonNull(getCommand("item")).setExecutor(new ItemCommand());
        Objects.requireNonNull(getCommand("party")).setExecutor(new PartyCommand(this));
        Objects.requireNonNull(getCommand("admin")).setExecutor(new AdminCommand(this));
        Objects.requireNonNull(getCommand("support")).setExecutor(new SupportCommand(this));

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (getMongoClient().hasPlayer(p.getUniqueId())) {
                OrionPlayer orionPlayer = getMongoClient().loadPlayer(p.getUniqueId());
                getPlayerManager().addPlayer(p.getUniqueId(), orionPlayer);
                orionPlayer.updateHealthbar();
                Utils.sendInfoMessage(p, "Data loaded !");
                if (orionPlayer.getRace() == null)
                    p.sendTitle("Bienvenu", "Selectionnez votre race via le /menu", 10, 70, 20);
                return;
            } else {
                OrionPlayer orionPlayer = Utils.generateStarterPlayer(p);
                getPlayerManager().addPlayer(p.getUniqueId(), orionPlayer);
                p.sendTitle("Bienvenu", "Selectionnez votre classe via le /menu", 10, 70, 20);
            }
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
        Bukkit.getBossBars().forEachRemaining(BossBar::removeAll);
    }

    private void transferResourcesToPluginFolder() {
        File pluginFolder = this.getDataFolder();
        if (!pluginFolder.exists()) {
            pluginFolder.mkdir();
        }
        for (String fileName : new String[] { "pentagram.png", "img/alchemy.png", "img/alchemy_1.png",
                "img/alchemy_2.png" }) {
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

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

}
