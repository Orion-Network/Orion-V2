package orion.mmorpg.players;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import orion.mmorpg.items.ItemList;

import java.util.LinkedList;
import java.util.List;

public enum PortalStone {
    SPAWN_STONE("Spawn", "SPAWN_STONE", Material.AMETHYST_SHARD, new Location(Bukkit.getWorld("world"),-280,185,-432))
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
