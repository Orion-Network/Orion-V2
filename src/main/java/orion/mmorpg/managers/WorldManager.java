package orion.mmorpg.managers;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.entities.samples.AbstractMob;

import javax.sound.sampled.Port;
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

        portalStones.put("CAMPEMENT_STONE", new PortalStone("Camp", new Location(mmorpg.getServer().getWorld("world"), 403, 25, 927), Material.STONE));
        addPortalStone("ARENA_STONE", "Arène", new Location(mmorpg.getServer().getWorld("world"), 462, 42, 1217), Material.STONE);
        addPortalStone("ARENA_SPAWN_STONE", "Arène (Spawn)", new Location(mmorpg.getServer().getWorld("world"), 438, 68, 1217), Material.STONE);

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
