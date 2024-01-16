package orion.mmorpg.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.sample.BasicItem;

public class ItemUtils {

    public static BasicItem getItemFromItemstack(ItemStack item) {
        return Mmorpg.INSTANCE.getItemManager().getItemFromItemstack(item);
    }

    public static ItemStack generateItemStack(String name, int i) {
        if(!Mmorpg.INSTANCE.getItemManager().isItem(name)) {
            System.out.println("item " + name + " doesn't exist");
            // TODO return default item
            return null;
        }
        return Mmorpg.INSTANCE.getItemManager().createItemstack(name, i);
    }

    public static BasicItem generateBasicItem(String name) {
        if(!Mmorpg.INSTANCE.getItemManager().isItem(name)) {
            System.out.println("item " + name + " doesn't exist");
            // TODO return default item
            return null;
        }
        return Mmorpg.INSTANCE.getItemManager().createItem(name);
    }

    public static boolean isItem(String name) {
        return Mmorpg.INSTANCE.getItemManager().isItem(name);
    }

    public static boolean isItem(ItemStack item) {
        String internal_id = item.getItemMeta().getPersistentDataContainer().get(Namespaces.internal_id, PersistentDataType.STRING);
        return Mmorpg.INSTANCE.getItemManager().isItem(internal_id);
    }
}
