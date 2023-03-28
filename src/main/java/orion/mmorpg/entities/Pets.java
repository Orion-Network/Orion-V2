package orion.mmorpg.entities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.entities.pets.FunFunGu;
import orion.mmorpg.entities.pets.FunnyWolf;
import orion.mmorpg.entities.samples.AbstractPet;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.utils.Rarity;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.UUID;

public enum Pets {
    FUNFUNGU(FunFunGu.class, Material.PUFFERFISH, "A cute pufferfish"),
    FUNNYWOLF(FunnyWolf.class, Material.WOLF_SPAWN_EGG, "A funny wolf");

    Class<? extends AbstractPet> petClass;
    Material material;
    String description;

    Pets(Class<? extends AbstractPet> petClass, Material material, String description) {
        this.petClass = petClass;
        this.material = material;
        this.description = description;
    }

    public Class<? extends AbstractPet> getPetClass() {
        return petClass;
    }

    public Material getMaterial() {
        return material;
    }

    public String getDescription() {
        return description;
    }

    public ItemStack generateItemStack() {
        return BasicItem.generateTemplateBasicItem(material, name(), 0, Collections.singletonList(description), Rarity.COMMON).generateItem(null, 1);
    }

    public boolean spawn(Player owner) {
        try {
            final AbstractPet mob = (AbstractPet) this.getPetClass().getDeclaredConstructors()[0].newInstance(UUID.randomUUID().toString(), owner.getLocation(), owner);
            mob.spawn();
            return true;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }
}
