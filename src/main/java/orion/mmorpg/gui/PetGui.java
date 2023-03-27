package orion.mmorpg.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.entities.samples.AbstractPet;
import orion.mmorpg.players.OrionPlayer;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class PetGui extends ChestGui {
    Player player;
    OrionPlayer orionPlayer;
    //Todo add good item visibility
    public PetGui(Player player, OrionPlayer orionPlayer) {
        super(3, "Menu des familiers");
        this.player = player;
        this.orionPlayer = orionPlayer;
        OutlinePane pane = new OutlinePane(0, 0, 9, 3);
        orionPlayer.getTamedPets().forEach(pet -> {
            pane.addItem(new GuiItem(new ItemStack(Material.LEATHER),e -> {
                e.setCancelled(true);
                try {
                    AbstractPet samplePetMob = (AbstractPet) pet.getClass().getDeclaredConstructors()[0].newInstance(UUID.randomUUID().toString(), player.getLocation(), player);
                    samplePetMob.spawn();
                } catch (InstantiationException | IllegalAccessException |
                         InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                }
            }));
        });

        this.addPane(pane);
    }
}
