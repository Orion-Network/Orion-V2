package orion.mmorpg.items.enchants;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import orion.mmorpg.utils.Namespaces;

import java.lang.reflect.Field;
import java.util.Arrays;

public class CustomEnchants {

    public static final Enchantment WRATH = new EnchantmentWrapper(Namespaces.wrath, "Wrath", 3);

    public static void register() {
        boolean registered = Arrays.stream(Enchantment.values()).anyMatch(enchantment -> enchantment.getKey().getKey().equals("wrath"));
        if (!registered) {
            registerEnchantment(WRATH);
        }
    }

    public static void registerEnchantment(Enchantment enchantment) {
        boolean registered = true;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (Exception e) {
            registered = false;
            e.printStackTrace();
        }
        if (registered) {
            System.out.println("Registered enchantment " + enchantment.getKey().getKey());
        }
    }
}
