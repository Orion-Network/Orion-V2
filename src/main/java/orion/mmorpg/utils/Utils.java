package orion.mmorpg.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import orion.mmorpg.Mmorpg;

import java.util.Random;

public class Utils {

    public static void sendInfoMessage(Entity e, String message) {
        e.sendMessage(ChatColor.YELLOW + message);
    }
    public static void sendErrorMessage(Entity e, String message) {
        e.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + message);
    }
    public static void sendDebugMessage(Entity e, String message) {
        if(Mmorpg.isDebug())
            e.sendMessage(ChatColor.GREEN + "" + ChatColor.ITALIC + message);
    }

    public static float calculateDamage(Stats playerStats, Stats weaponStats, float damage) {
        float strength = playerStats.getStrength() + weaponStats.getStrength();
        float critical_chance = playerStats.getCritical_chance() + weaponStats.getCritical_chance();
        float critical_damage = playerStats.getCritical_damage() + weaponStats.getCritical_damage();

        float totalDamage = (5 + damage)*(1+(strength/100));
        Random r = new Random();
        int randomized = (r.nextInt(100-1) + 1);
        float critical = (randomized < critical_chance ? 1 : 0);
        if(Mmorpg.isDebug())
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Strength: " + ChatColor.RED + strength + "\n" +
                ChatColor.YELLOW + "Random: " + ChatColor.RED + randomized + "\n" +
                ChatColor.YELLOW + "Critical Chance: " + ChatColor.RED + critical_chance + "\n" +
                ChatColor.YELLOW + "Critical Damage: " + ChatColor.RED + critical_damage + "\n" +
                ChatColor.YELLOW + "Base Damage: " + ChatColor.RED + damage + "\n" +
                ChatColor.YELLOW + "Total Damage" + ChatColor.RED + totalDamage + "\n" +
                ChatColor.YELLOW + "Is Critical : " + ChatColor.RED + (critical == 1)  + "\n" +
                ChatColor.YELLOW + "Critical Multiplier: " + ChatColor.RED + (1 + ((critical_damage*critical)/100)) + "\n" +
                ChatColor.YELLOW + "Total: " + ChatColor.RED + String.valueOf(totalDamage*(1 + ((critical_damage*critical)/100))));
        return totalDamage*(1 + ((critical_damage*critical)/100));
    }
}
