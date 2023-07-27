package orion.mmorpg.utils;

import com.google.common.base.Enums;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.Getter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.ItemType;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static orion.mmorpg.utils.Stats.StatsInfo.*;

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

    public static void sendTooltipInfoMessage(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GRAY + message));
    }

    /**
     * Compute damage to inflict based on Strength, Critical Chance, Critical Damages and default damage
     *
     * @param playerStats Stats who will be used for computing damages {@link Stats}
     * @param damage      Default damage to inflict
     * @return Computed damages
     */
    public static float calculateDamage(Stats playerStats, float damage) {
        float strength = playerStats.get(STRENGTH);
        float critical_chance = playerStats.get(CRITICAL_CHANCE);
        float critical_damage = playerStats.get(CRITICAL_DAMAGE);

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
        float intel = playerStats.get(INTELLIGENCE);

        float totalDamage = (5 + damage) * (1 + (intel / 50));
        if (Mmorpg.isDebug())
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Intel: " + ChatColor.RED + intel + "\n" +
                    ChatColor.YELLOW + "Base Damage: " + ChatColor.RED + damage + "\n" +
                    ChatColor.YELLOW + "Total Damage" + ChatColor.RED + totalDamage + "\n");
        return totalDamage;
    }

    public static float calculateDamageReduction(float defence) {
        return 1 + (defence / (defence + 100));
    }

    public static LivingEntity getTargetInFront(LivingEntity entity, int range) {
        RayTraceResult rayTraceResult = entity.getWorld().rayTraceEntities(entity.getEyeLocation().add(entity.getEyeLocation().getDirection().normalize()), entity.getEyeLocation().getDirection().normalize(), range,
                (e) -> (
                        e instanceof LivingEntity
                                && !e.isInvulnerable()
                                && e != entity
                )
        );
        if (rayTraceResult != null) {
            return (LivingEntity) rayTraceResult.getHitEntity();
        }
        return null;
    }

    public static LivingEntity getNearestEntity(LivingEntity entity, int range) {
        LivingEntity nearest = null;
        double distance = Double.MAX_VALUE;
        for (Entity e : entity.getNearbyEntities(range, range, range)) {
            if (e instanceof LivingEntity && e != entity) {
                double d = e.getLocation().distance(entity.getLocation());
                if (d < distance) {
                    distance = d;
                    nearest = (LivingEntity) e;
                }
            }
        }
        return nearest;
    }

    public static String locationToReadableString(Location location) {
        return "X : " + location.getBlockX() + " Y : " + location.getBlockY() + " Z : " + location.getBlockZ();
    }

    public static String itemDisplayName(ItemStack item) {
        return (Objects.requireNonNull(item.getItemMeta()).hasDisplayName() ? item.getItemMeta().getDisplayName() : WordUtils.capitalizeFully(item.getType().name().replaceAll("_"," ")));
    }

    public static boolean isItemValid(ItemStack item, Player player) {
        //Check if item can be converted to BasicItem
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()) return false;

        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        String internal_id = container.get(Namespaces.internal_id, PersistentDataType.STRING);

        if(ItemType.valueOf(container.get(Namespaces.type, PersistentDataType.STRING)) == ItemType.RESSOURCE) return true;

        if(!Enums.getIfPresent(ItemList.class, internal_id).isPresent()) return false;

        if(!player.hasPermission(ItemList.getItemFromItemstack(item).getPERMISSION())) return false;

        return ItemList.isItem(item);
    }

    public static List<String> enumAutocomplete(String input, Class<? extends Enum> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .filter(name -> name.toLowerCase().startsWith(input.toLowerCase()))
                .collect(Collectors.toList());
    }

    public static int getRandomInt(int i, int i1) {
        return ThreadLocalRandom.current().nextInt(i, i1 + 1);
    }

    public static long getRandomLong(int i, long totalWeight) {
        return ThreadLocalRandom.current().nextLong(i, totalWeight + 1);
    }

    public static class ChunkCoords {
        @Getter
        private final int x;
        @Getter
        private final int z;
        @Getter
        private final World world;

        public ChunkCoords(Chunk chunk) {
            this.x = chunk.getX();
            this.z = chunk.getZ();
            this.world = chunk.getWorld();
        }

        public ChunkCoords(int x, int z, World world) {
            this.x = x;
            this.z = z;
            this.world = world;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ChunkCoords that = (ChunkCoords) o;
            return x == that.x &&
                    z == that.z &&
                    world.equals(that.world);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, z, world);
        }
    }

    /**
     * Clamps the rotation angle to [-180, 180]
     */
    public static float clamp(float angle) {
        while (angle < -180.0F) {
            angle += 360.0F;
        }
        while (angle >= 180.0F) {
            angle -= 360.0F;
        }
        return angle;
    }

    private static final TreeMap<Integer, String> treemap = new TreeMap<Integer, String>();
    static {
        treemap.put(1000, "M");
        treemap.put(900, "CM");
        treemap.put(500, "D");
        treemap.put(400, "CD");
        treemap.put(100, "C");
        treemap.put(90, "XC");
        treemap.put(50, "L");
        treemap.put(40, "XL");
        treemap.put(10, "X");
        treemap.put(9, "IX");
        treemap.put(5, "V");
        treemap.put(4, "IV");
        treemap.put(1, "I");

    }

    public static final String integerToRoman5(int number) {
        int l = treemap.floorKey(number);
        if (number == l) {
            return treemap.get(number);
        }
        return treemap.get(l) + integerToRoman5(number - l);
    }

    public static class Pair<K, V> {
        private final K key;
        private final V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}
