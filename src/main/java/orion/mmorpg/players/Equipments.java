package orion.mmorpg.players;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.util.Consumer;
import orion.mmorpg.items.gui.*;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.utils.Stats;

import java.util.HashMap;

public class Equipments {
    private HashMap<EquipmentSlot, BasicArmorItem> equipments = new HashMap<>();
    private Player owner;

    public boolean addEquipment(BasicArmorItem item) {
        EquipmentSlot slot = isSlotFree(item.getARMORTYPE());
        if(slot == null)
            return false;
        equipments.put(slot, item);
        return true;
    }

    public boolean removeEquipment(EquipmentSlot slot) {
        if(!equipments.containsKey(slot))
            return false;
        owner.getInventory().addItem(equipments.get(slot).generateItem(null, 1));
        owner.updateInventory();
        equipments.remove(slot);
        return true;
    }

    public BasicArmorItem getEquipmentFromSlot(EquipmentSlot slot) {
        if(equipments.containsKey(slot))
            return equipments.get(slot);
        return null;
    }

    private EquipmentSlot isSlotFree(BasicArmorItem.ArmorType armorType) {
        switch (armorType) {
            case HELMET -> {
                if (!equipments.containsKey(EquipmentSlot.HELMET))
                    return EquipmentSlot.HELMET;
            }
            case CHESTPLATE -> {
                if (!equipments.containsKey(EquipmentSlot.CHEST_PLATE))
                    return EquipmentSlot.CHEST_PLATE;
            }
            case LEGGINGS -> {
                if (!equipments.containsKey(EquipmentSlot.LEGGINGS))
                    return EquipmentSlot.LEGGINGS;
            }
            case BOOTS -> {
                if (!equipments.containsKey(EquipmentSlot.BOOTS))
                    return EquipmentSlot.BOOTS;
            }
            case BELT -> {
                if (!equipments.containsKey(EquipmentSlot.BELT))
                    return EquipmentSlot.BELT;
            }
            case RING -> {
                if (!equipments.containsKey(EquipmentSlot.LEFT_RING))
                    return EquipmentSlot.LEFT_RING;
                else if (!equipments.containsKey(EquipmentSlot.RIGHT_RING))
                    return EquipmentSlot.RIGHT_RING;
            }
        }
        return null;
    }
    private final Consumer<InventoryClickEvent> removeEquipment = event -> {

    };
    public StaticPane getEquipmentPane(Gui gui) {
        StaticPane pane = new StaticPane(0,0,3,4);

        if(equipments.containsKey(EquipmentSlot.HELMET))
            pane.addItem(new GuiItem(equipments.get(EquipmentSlot.HELMET).generateItem(null, 1)), 0, 0);
        else
            pane.addItem(new GuiItem(new HelmetPlaceholderItem().generateItem(null, 1)), 0, 0);

        if(equipments.containsKey(EquipmentSlot.CHEST_PLATE))
            pane.addItem(new GuiItem(equipments.get(EquipmentSlot.CHEST_PLATE).generateItem(null, 1)), 1, 0);
        else
            pane.addItem(new GuiItem(new ChestplatePlaceholderItem().generateItem(null, 1)), 1, 0);

        if(equipments.containsKey(EquipmentSlot.LEGGINGS))
            pane.addItem(new GuiItem(equipments.get(EquipmentSlot.LEGGINGS).generateItem(null, 1)), 2, 0);
        else
            pane.addItem(new GuiItem(new LeggingsPlaceholderItem().generateItem(null, 1)), 2, 0);

        if(equipments.containsKey(EquipmentSlot.BOOTS))
            pane.addItem(new GuiItem(equipments.get(EquipmentSlot.BOOTS).generateItem(null, 1)), 3, 0);
        else
            pane.addItem(new GuiItem(new BootsPlaceholder().generateItem(null, 1)), 3, 0);

        if(equipments.containsKey(EquipmentSlot.BELT))
            pane.addItem(new GuiItem(equipments.get(EquipmentSlot.BELT).generateItem(null, 1)), 2, 2);
        else
            pane.addItem(new GuiItem(new BeltPlaceholder().generateItem(null, 1)), 2, 2);

        if(equipments.containsKey(EquipmentSlot.LEFT_RING))
            pane.addItem(new GuiItem(equipments.get(EquipmentSlot.LEFT_RING).generateItem(null, 1)), 0, 1);
        else
            pane.addItem(new GuiItem(new BeltPlaceholder().generateItem(null, 1)), 0, 1);

        if(equipments.containsKey(EquipmentSlot.RIGHT_RING))
            pane.addItem(new GuiItem(equipments.get(EquipmentSlot.RIGHT_RING).generateItem(null, 1)), 0, 2);
        else
            pane.addItem(new GuiItem(new RightButtonPlaceholder().generateItem(null, 1)), 0, 2);

        return pane;
    }

    public HashMap<EquipmentSlot, BasicArmorItem> getEquipments() {
        return equipments;
    }

    //TODO Implement gui in class

    public enum EquipmentSlot {
        HELMET,
        CHEST_PLATE,
        LEGGINGS,
        BOOTS,
        LEFT_RING,
        RIGHT_RING,
        BELT
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
