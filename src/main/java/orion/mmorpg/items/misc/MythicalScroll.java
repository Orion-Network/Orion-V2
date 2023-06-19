package orion.mmorpg.items.misc;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.config.AssociationConfig;
import orion.mmorpg.items.sample.BasicConsumableItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Support;
import orion.mmorpg.players.abilities.SupportList;
import orion.mmorpg.utils.Namespaces;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

public class MythicalScroll extends BasicConsumableItem {
    public MythicalScroll() {
        super(Material.PAPER, "Parchemin Mythique", "MYTHICAL_SCROLL", AssociationConfig.getModel("misc/paper/scroll"), Collections.singletonList("Un parchemin pouvant vous offrir un immense pouvoir."), Rarity.MYTHIC);
    }

    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        String support = e.getItem().getItemMeta().getPersistentDataContainer().get(Namespaces.support, PersistentDataType.STRING);
        if(support == null) {
            e.getItem().setAmount(0);
            e.getPlayer().updateInventory();
            Utils.sendErrorMessage(e.getPlayer(), "Parchemin invalide, suppression de l'item.");
            return;
        }

        SupportList.valueOf(support.toUpperCase());
        try {
            data.setMainSupport((Support) SupportList.valueOf(support.toUpperCase()).getSupport().getDeclaredConstructors()[0].newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException error) {
            e.getItem().setAmount(0);
            e.getPlayer().updateInventory();
            Utils.sendErrorMessage(e.getPlayer(), "Parchemin invalide, suppression de l'item.");
        }
        super.onUseItem(mmorpg, data, e);
    }

    public ItemStack generateScroll(String support_id) {
        support_id = support_id.toUpperCase();
        ItemStack i = generateItem(null, 1);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(this.RARITY.getChatColor() + this.NAME + " " + SupportList.valueOf(support_id));
        meta.getPersistentDataContainer().set(Namespaces.support, PersistentDataType.STRING, support_id);
        i.setItemMeta(meta);
        return i;
    }
}
