package orion.mmorpg;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import orion.mmorpg.commands.ItemCommand;
import orion.mmorpg.commands.MenuCommand;
import orion.mmorpg.commands.StatsCommand;
import orion.mmorpg.database.Client;
import orion.mmorpg.listeners.*;
import orion.mmorpg.managers.PlayerManager;
import orion.mmorpg.utils.OrionGson;

import java.util.Objects;

public final class Mmorpg extends JavaPlugin {

    public static boolean debug = true;
    public static OrionGson orionGson = new OrionGson();
    Client mongoClient = new Client();
    PlayerManager playerManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        FileConfiguration config = this.getConfig();
        config.options().copyDefaults(true);
        saveConfig();

        config.addDefault("username", "root");
        config.addDefault("password", "");
        playerManager = new PlayerManager();

        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new PlayerJoinListener(this), this);
        pm.registerEvents(new PlayerPickupListener(this), this);
        pm.registerEvents(new EntityHitEntityListener(this), this);
        pm.registerEvents(new InventoryClickListener(this), this);
        pm.registerEvents(new PlayerUseItemEvent(this), this);
        pm.registerEvents(new PlayerStatsListener(this), this);

        Objects.requireNonNull(getCommand("menu")).setExecutor(new MenuCommand(this));
        Objects.requireNonNull(getCommand("stats")).setExecutor(new StatsCommand());
        Objects.requireNonNull(getCommand("item")).setExecutor(new ItemCommand());
    }

    @Override
    public void onDisable() {
        this.saveConfig();
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static OrionGson getOrionGson() {
        return orionGson;
    }

    public static void setDebug(boolean debug) {
        Mmorpg.debug = debug;
    }

    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public Client getMongoClient() {
        return mongoClient;
    }
}
