package orion.mmorpg.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import orion.mmorpg.Mmorpg;

import java.util.Random;

public class Utils {
    /**
     * Send info message to entity with good formatting.
     *
     * @param e       The entity to send message
     * @param message The message to send
     */
    public static void sendInfoMessage(Entity e, String message) {
        e.sendMessage(ChatColor.YELLOW + message);
    }

    /**
     * Send Error message to entity with good formating
     *
     * @param e       The entity to send message
     * @param message The message to send
     */
    public static void sendErrorMessage(Entity e, String message) {
        e.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + message);
    }

    /**
     * Send debug message if debugging is enabled
     *
     * @param e       The entity to send message
     * @param message The message to send
     */
    public static void sendDebugMessage(Entity e, String message) {
        if (Mmorpg.isDebug())
            e.sendMessage(ChatColor.GREEN + "" + ChatColor.ITALIC + message);
    }

    /**
     * Compute damage to inflict based on Strength, Critical Chance, Critical Damages and default damage
     *
     * @param playerStats Stats who will be used for computing damages {@link Stats}
     * @param damage      Default damage to inflict
     * @return Computed damages
     */
    public static float calculateDamage(Stats playerStats, float damage) {
        float strength = playerStats.getStrength();
        float critical_chance = playerStats.getCritical_chance();
        float critical_damage = playerStats.getCritical_damage();

        float totalDamage = (5 + damage) * (1 + (strength / 100));
        Random r = new Random();
        int randomized = (r.nextInt(100 - 1) + 1);
        float critical = (randomized < critical_chance ? 1 : 0);
        if (Mmorpg.isDebug())
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Strength: " + ChatColor.RED + strength + "\n" +
                    ChatColor.YELLOW + "Random: " + ChatColor.RED + randomized + "\n" +
                    ChatColor.YELLOW + "Critical Chance: " + ChatColor.RED + critical_chance + "\n" +
                    ChatColor.YELLOW + "Critical Damage: " + ChatColor.RED + critical_damage + "\n" +
                    ChatColor.YELLOW + "Base Damage: " + ChatColor.RED + damage + "\n" +
                    ChatColor.YELLOW + "Total Damage" + ChatColor.RED + totalDamage + "\n" +
                    ChatColor.YELLOW + "Is Critical : " + ChatColor.RED + (critical == 1) + "\n" +
                    ChatColor.YELLOW + "Critical Multiplier: " + ChatColor.RED + (1 + ((critical_damage * critical) / 100)) + "\n" +
                    ChatColor.YELLOW + "Total: " + ChatColor.RED + totalDamage * (1 + ((critical_damage * critical) / 100)));
        return totalDamage * (1 + ((critical_damage * critical) / 100));
    }

    /**
     * Compute magic damage to inflict based ond Intel
     *
     * @param playerStats Stats who will be used to compute magic damages {@link Stats}
     * @param damage      Default damages to inflict
     * @return Computed magic damages
     */
    public static float calculateMagicDamage(Stats playerStats, float damage) {
        float intel = playerStats.getIntel();

        float totalDamage = (5 + damage) * (1 + (intel / 50));
        if (Mmorpg.isDebug())
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Intel: " + ChatColor.RED + intel + "\n" +
                    ChatColor.YELLOW + "Base Damage: " + ChatColor.RED + damage + "\n" +
                    ChatColor.YELLOW + "Total Damage" + ChatColor.RED + totalDamage + "\n");
        return totalDamage;
    }

    public static boolean canPvp(Entity entity) {
        RegionManager manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(entity.getWorld()));
        for(ProtectedRegion region : manager.getApplicableRegions(BukkitAdapter.asBlockVector(entity.getLocation()))) {
            if(region.getFlag(Flags.PVP).equals(StateFlag.State.DENY)) {
                return false;
            } else
                return true;
        }
        return false;
    }
}
