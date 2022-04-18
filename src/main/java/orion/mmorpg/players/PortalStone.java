package orion.mmorpg.players;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;

/**
 * an Enum of every teleportation location accessible via {@link orion.mmorpg.gui.PortalStoneGui}
 */
public enum PortalStone {
    SPAWN_STONE("Spawn", "SPAWN_STONE", Material.AMETHYST_SHARD, new Location(Bukkit.getWorld("world"), 438, 68, 1217)),
    ARENA_STONE("Arena", "ARENA_STONE", Material.AMETHYST_SHARD, new Location(Bukkit.getWorld("world"), 462, 42, 1217)),
    CAMPMENT_STONE("Campsite", "CAMPMENT_STONE", Material.AMETHYST_SHARD, new Location(Bukkit.getWorld("world"), 403, 25, 927)),
    ;

    String title;
    String id;
    Material material;
    Location location;

    PortalStone(String title, String id, Material material, Location location) {
        this.title = title;
        this.id = id;
        this.material = material;
        this.location = location;
    }

    /**
     * Generate an ItemStack representing the PortalStone to be used in gui
     *
     * @return ItemStack of the PortalStone
     */
    public ItemStack generateItem() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.YELLOW + title);
        List<String> lore = new LinkedList<>();
        lore.add(ChatColor.GRAY + "X : " + ChatColor.YELLOW + location.getBlockX() + ", " +
                ChatColor.GRAY + "Y : " + ChatColor.YELLOW + location.getBlockY() + ", " +
                ChatColor.GRAY + "Z : " + ChatColor.YELLOW + location.getBlockZ());
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public Material getMaterial() {
        return material;
    }

    public Location getLocation() {
        return location;
    }
}
