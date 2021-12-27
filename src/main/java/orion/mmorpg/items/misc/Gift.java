package orion.mmorpg.items.misc;

import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Utils;

public class Gift extends BasicItem {
    public Gift() {
        super(Material.PLAYER_HEAD, "Gift", "gift", 0, null, Rarity.COMMON);
    }

    public ItemStack generateGift(String internal_id, String gifter) {
        ItemStack item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/d2ac1c51807e261c12c4f2adbad36b8b2c497c277f7223ec244cb4608c59c");
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(NamespacedKey.minecraft("internal_id"), PersistentDataType.STRING, internal_id);
        meta.getPersistentDataContainer().set(NamespacedKey.minecraft("gifter"), PersistentDataType.STRING, gifter);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void onPickupItem(EntityPickupItemEvent e, ItemStack item) {
        super.onPickupItem(e, item);
        Bukkit.broadcastMessage("test 2");
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        Utils.sendInfoMessage(e.getEntity(), "Vous avez reçus " + item.getAmount() + " " + item.getItemMeta().getDisplayName() + " de " + container.get(NamespacedKey.minecraft("gifter"), PersistentDataType.STRING));
    }
}
