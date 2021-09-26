package orion.mmorpg;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import orion.mmorpg.commands.ItemCommand;
import orion.mmorpg.commands.MenuCommand;
import orion.mmorpg.commands.StatsCommand;
import orion.mmorpg.database.Client;
import orion.mmorpg.listeners.*;
import orion.mmorpg.managers.PlayerManager;

import java.util.Objects;

public final class Mmorpg extends JavaPlugin {

    public static boolean debug = true;
    Client mongoClient = new Client();
    PlayerManager playerManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        playerManager = new PlayerManager();

        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new PlayerJoinListener(this), this);
        pm.registerEvents(new PlayerPickupListener(this), this);
        pm.registerEvents(new EntityHitEntityListener(this), this);
        pm.registerEvents(new InventoryClickListener(), this);
        pm.registerEvents(new PlayerUseItemEvent(this), this);
        pm.registerEvents(new PlayerStatsListener(this), this);

        Objects.requireNonNull(getCommand("menu")).setExecutor(new MenuCommand(this));
        Objects.requireNonNull(getCommand("stats")).setExecutor(new StatsCommand());
        Objects.requireNonNull(getCommand("item")).setExecutor(new ItemCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }


    public static boolean isDebug() {
        return debug;
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
