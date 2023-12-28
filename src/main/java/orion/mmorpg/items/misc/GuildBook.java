package orion.mmorpg.items.misc;


import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Namespaces;
import orion.mmorpg.utils.Rarity;

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
                    .itemLeft(Mmorpg.INSTANCE.getItemManager().createItemstack("ACCEPT_BUTTON_PLACEHOLDER", 1))
                    .onClick((slot, snapshot) -> {
                        if(slot != AnvilGUI.Slot.OUTPUT) return List.of(AnvilGUI.ResponseAction.close());
                        String text = snapshot.getText();
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
