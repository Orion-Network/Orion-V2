package orion.mmorpg.managers;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.reflections.Reflections;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.ExcludeRegister;
import orion.mmorpg.items.gui.PetPlaceholder;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.utils.Namespaces;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

@Getter
public class ItemManager extends AbstractManager {

    private final HashMap<String, Class<? extends BasicItem>> items;
    public ItemManager(Mmorpg mmorpg) {
        super(mmorpg);

        items = new HashMap<>();

        System.out.println("registering pet placeholder");
        registerItems();
    }

    public void registerItems() {
        System.out.println("registering items");

        for (Class<? extends BasicItem> item : new Reflections("orion.mmorpg.items").getSubTypesOf(BasicItem.class)) {
            BasicItem basicItem = null;
            if(item.isAnnotationPresent(ExcludeRegister.class) || item.getCanonicalName() == null)
                continue;
            try {
                basicItem = item.getConstructor().newInstance();
                registerItem(basicItem.getINTERNAL_ID(), item);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public void registerItem(String name, Class<? extends BasicItem> item) {
        items.put(name, item);
        System.out.println("registering item " + name);
    }

    public Class<? extends BasicItem> getItem(String name) {
        return items.get(name);
    }

    public boolean isItem(String name) {
        return items.containsKey(name);
    }

    public BasicItem createItem(String name) {
        try {
            return items.get(name).getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public BasicItem getItemFromItemstack(ItemStack itemStack) {
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        if(container.has(Namespaces.internal_id, PersistentDataType.STRING) && isItem(container.get(Namespaces.internal_id, PersistentDataType.STRING))) {
            try {
                return (BasicItem) getItem(container.get(Namespaces.internal_id, PersistentDataType.STRING)).getSuperclass().getDeclaredConstructor(ItemStack.class).newInstance(itemStack);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ItemStack createItemstack(String name, int i) {
        if(createItem(name) == null)
            return null;
        return createItem(name).generateItem(null, i);
    }

}


