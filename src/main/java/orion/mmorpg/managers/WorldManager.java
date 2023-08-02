package orion.mmorpg.managers;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.entities.samples.AbstractMob;

import javax.sound.sampled.Port;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class WorldManager extends AbstractManager {
    private LinkedHashMap<World, AbstractMob> bosses;
    @Getter
    private LinkedHashMap<String, PortalStone> portalStones;

    public WorldManager(Mmorpg mmorpg) {
        super(mmorpg);
        bosses = new LinkedHashMap<>();
        portalStones = new LinkedHashMap<>();

        ConfigurationSection section = mmorpg.getConfig().getConfigurationSection("portal_stones");
        assert section != null;
        section.getKeys(false).forEach(e -> {
            ConfigurationSection portalSection = section.getConfigurationSection(e);
            addPortalStone(e.toUpperCase(), new PortalStone(portalSection.getString("title"), new Location(mmorpg.getServer().getWorld(portalSection.getString("location.world")), portalSection.getInt("location.x"), portalSection.getInt("location.y"), portalSection.getInt("location.z")), Material.valueOf(portalSection.getString("material"))));
        });
    }

    public void spawn() {

    }

    public void addBoss(World world, AbstractMob mob) {
        bosses.put(world, mob);
    }

    public AbstractMob getBoss(World world) {
        return bosses.get(world);
    }

    public PortalStone getPortalStone(String title) {
        return portalStones.get(title);
    }

    public void addPortalStone(String id, String title, Location location, Material material) {
        addPortalStone(id, new PortalStone(title, location, material));
    }

    public void addPortalStone(String id, PortalStone stone) {
        portalStones.put(id, stone);
    }

    public static class PortalStone {
        @Getter
        final String title;
        @Getter@Setter
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
            if(description != null)
                lore.add(ChatColor.GRAY + description);
            lore.add(ChatColor.GRAY + "X : " + ChatColor.YELLOW + location.getBlockX() + ", " +
                    ChatColor.GRAY + "Y : " + ChatColor.YELLOW + location.getBlockY() + ", " +
                    ChatColor.GRAY + "Z : " + ChatColor.YELLOW + location.getBlockZ());
            meta.setLore(lore);

            item.setItemMeta(meta);
            return item;
        }
    }
}
