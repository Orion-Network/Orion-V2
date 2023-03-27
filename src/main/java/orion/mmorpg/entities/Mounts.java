package orion.mmorpg.entities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.entities.mounts.FlyingFish;
import orion.mmorpg.entities.mounts.TestWolf;
import orion.mmorpg.entities.samples.AbstractMount;
import orion.mmorpg.entities.samples.AbstractPet;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.utils.Rarity;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.UUID;

public enum Mounts {

    TEST_WOLF(TestWolf.class),
    FLYING_FISH(FlyingFish.class)
    ;

    Class<? extends AbstractMount> mountClass;

    Mounts(Class<? extends AbstractMount> mobClass) {
        this.mountClass = mobClass;
    }

    public Class<? extends AbstractMount> getMountClass() {
        return mountClass;
    }

    public boolean spawn(Player owner) {
        try {
            final AbstractMount mob = (AbstractMount) this.getMountClass().getDeclaredConstructors()[0].newInstance(UUID.randomUUID().toString(), owner.getLocation(), owner);
            mob.spawn();
            return true;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ItemStack generateItemStack() {
        return BasicItem.generateTemplateBasicItem(Material.SADDLE, this.name(), 0, Collections.singletonList(ChatColor.BLUE + "Cliquez pour invoquer cette monture"), Rarity.COMMON).generateItem(null, 1);
    }
}
