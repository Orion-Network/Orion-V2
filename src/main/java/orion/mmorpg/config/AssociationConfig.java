package orion.mmorpg.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import orion.mmorpg.Mmorpg;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class AssociationConfig {
    private static File file;
    private static FileConfiguration fileConfiguration;

    public static void setup(Mmorpg mmorpg) {
        file = new File(mmorpg.getDataFolder(), "association.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public static void save() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reload() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public static int getModel(String identifier) {
        if (fileConfiguration.contains("item." + identifier))
            return fileConfiguration.getInt("item." + identifier);
        else {
            JavaPlugin.getPlugin(Mmorpg.class).getLogger().log(Level.WARNING, "Model not found for " + identifier);
            return 0;
        }
    }
}
