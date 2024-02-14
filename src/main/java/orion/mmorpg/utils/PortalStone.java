package orion.mmorpg.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;

public class PortalStone {
    @Getter
    final String title;
    @Getter
    @Setter
    String description;
    @Getter
    final Location location;
    @Getter
    final Material material;

    public PortalStone(String title, Location location, Material material) {
        this.title = title;
        this.location = location;
        this.material = material;
    }

    public ItemStack generateItem() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.YELLOW + title);
        List<String> lore = new LinkedList<>();
        if (description != null)
            lore.add(ChatColor.GRAY + description);
        lore.add(ChatColor.GRAY + "X : " + ChatColor.YELLOW + location.getBlockX() + ", " + ChatColor.GRAY + "Y : "
                + ChatColor.YELLOW + location.getBlockY() + ", " + ChatColor.GRAY + "Z : " + ChatColor.YELLOW
                + location.getBlockZ());
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }
}
