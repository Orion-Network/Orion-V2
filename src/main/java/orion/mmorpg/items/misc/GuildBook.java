package orion.mmorpg.items.misc;

import com.github.stefvanschie.inventoryframework.gui.type.AnvilGui;
import net.minecraft.world.item.ItemMapEmpty;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Namespaces;
import orion.mmorpg.utils.Rarity;
import oshi.util.tuples.Pair;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GuildBook extends BasicItem {
    public GuildBook(ItemStack item) {
        super(item);
    }

    public GuildBook() {
        super(Material.WRITABLE_BOOK, "Guild Book", "GUILD_BOOK", 1, Collections.singletonList("Permet de créér une guilde"), Rarity.RARE);
    }

    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        ItemMeta meta = item.getItemMeta();
        if(!meta.getPersistentDataContainer().has(Namespaces.guild_name, PersistentDataType.STRING)) {
            new AnvilGUI.Builder()
                    .title("Inscrivez le nom de la guilde")
                    .itemLeft(ItemList.ACCEPT_BUTTON_PLACEHOLDER.generateItemStack(1))
                    .onComplete((p, text) -> {
                        meta.getPersistentDataContainer().set(Namespaces.guild_name, PersistentDataType.STRING, text);
                        item.setItemMeta(meta);
                        return AnvilGUI.Response.close();
                    })
                    .plugin(mmorpg)
                    .open(e.getPlayer());
        }
        super.onUseItem(mmorpg, data, e);
    }
}
