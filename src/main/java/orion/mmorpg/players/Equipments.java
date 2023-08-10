package orion.mmorpg.players;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.InventoryComponent;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.PatternPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.utils.Utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;

public class Equipments {
    private HashMap<EquipmentSlot, BasicArmorItem> equipments = new HashMap<>();
    private Player owner;

    public Utils.Pair<Boolean, String> addEquipment(BasicArmorItem item) {
        EquipmentSlot slot = isSlotFree(item.getARMORTYPE());
        if(slot == null)
            return new Utils.Pair<>(false, "le slot de cet objet n'est pas libre.");
        equipments.put(slot, item);
        //TODO add stats
        return new Utils.Pair<>(true, "l'objet a bien été équipé.");
    }

    public Utils.Pair<Boolean, String> removeEquipment(EquipmentSlot slot) {
        if(!equipments.containsKey(slot))
            return new Utils.Pair<>(false, "le slot est vide.");
        equipments.remove(slot);
        //TODO Remove stats
        return new Utils.Pair<>(true, "l'objet a bien été retiré.");
    }

    public BasicArmorItem getEquipmentFromSlot(EquipmentSlot slot) {
        System.out.println("slot: " + slot);
        equipments.forEach((k,v) -> System.out.println(k + " " + v));
        if(equipments.containsKey(slot))
            return equipments.get(slot);
        return null;
    }

    public EquipmentSlot getEquipementSlotFromArmorType(BasicArmorItem.ArmorType armorType) {
        switch (armorType) {
            case HELMET -> {
                return EquipmentSlot.HELMET;
            }
            case CHESTPLATE -> {
                return EquipmentSlot.CHEST_PLATE;
            }
            case LEGGINGS -> {
                return EquipmentSlot.LEGGINGS;
            }
            case BOOTS -> {
                return EquipmentSlot.BOOTS;
            }
            case BELT -> {
                return EquipmentSlot.BELT;
            }
            case RING -> {
                if (!equipments.containsKey(EquipmentSlot.LEFT_RING))
                    return EquipmentSlot.LEFT_RING;
                return EquipmentSlot.RIGHT_RING;
            }

        }
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

    public StaticPane getEquipmentPane(ChestGui gui) {
        StaticPane pane = new StaticPane(0,0,3,4);
        pane.setOnClick(event -> event.setCancelled(true));
        pane.addItem(new GuiItem(EquipementHolders.getItemStack(this, EquipmentSlot.HELMET), getEquippingClick(gui, pane, BasicArmorItem.ArmorType.HELMET)), 1, 0);
        pane.addItem(new GuiItem(EquipementHolders.getItemStack(this, EquipmentSlot.CHEST_PLATE), getEquippingClick(gui, pane, BasicArmorItem.ArmorType.CHESTPLATE)), 1, 1);
        pane.addItem(new GuiItem(EquipementHolders.getItemStack(this, EquipmentSlot.LEGGINGS), getEquippingClick(gui, pane, BasicArmorItem.ArmorType.LEGGINGS)), 1, 2);
        pane.addItem(new GuiItem(EquipementHolders.getItemStack(this, EquipmentSlot.BOOTS), getEquippingClick(gui, pane, BasicArmorItem.ArmorType.BOOTS)), 1, 3);

        pane.addItem(new GuiItem(EquipementHolders.getItemStack(this, EquipmentSlot.BELT), getEquippingClick(gui, pane, BasicArmorItem.ArmorType.BELT)), 2, 2);
        pane.addItem(new GuiItem(EquipementHolders.getItemStack(this, EquipmentSlot.LEFT_RING), getEquippingClick(gui, pane, BasicArmorItem.ArmorType.RING)), 0, 2);
        pane.addItem(new GuiItem(EquipementHolders.getItemStack(this, EquipmentSlot.RIGHT_RING), getEquippingClick(gui, pane, BasicArmorItem.ArmorType.RING)), 0, 1);

        return pane;
    }

    public Consumer<InventoryClickEvent> getEquippingClick(ChestGui gui, StaticPane pane, BasicArmorItem.ArmorType type) {
        EquipmentSlot slot = getEquipementSlotFromArmorType(type);
        return event -> {
            Player player = (Player) event.getWhoClicked();
            OrionPlayer orionPlayer = OrionPlayer.getOrionPlayer(player);
            Slot invSlot = Slot.fromIndex(event.getSlot()-(6 * (event.getSlot()/9)));
            if(event.getAction().equals(InventoryAction.SWAP_WITH_CURSOR)) {
                if(ItemList.isItem(event.getCursor()) && ItemList.getItemFromItemstack(event.getCursor()) instanceof BasicArmorItem armor && armor.getARMORTYPE().equals(type)) {
                    if(getEquipmentFromSlot(slot) != null) {
                        event.getWhoClicked().setItemOnCursor(getEquipmentFromSlot(slot).generateItem(null, 1));
                        getEquipmentFromSlot(slot).onUnequip(player, orionPlayer);
                        removeEquipment(slot);
                    } else {
                        event.getWhoClicked().setItemOnCursor(null);
                    }
                    armor.onEquip(player, orionPlayer);
                    addEquipment(armor);

                    ItemStack item = EquipementHolders.getItemStack(this, slot);

                    pane.removeItem(invSlot);
                    gui.update();
                    pane.addItem(new GuiItem(item, getEquippingClick(gui, pane, type)), invSlot);
                }
            } else {
                System.out.println("Click: " + event.getClick());
                if(getEquipmentFromSlot(slot) != null) {
                    System.out.println("Unequip");
                    ItemStack item = EquipementHolders.getPlaceholder(slot);
                    pane.removeItem(invSlot);
                    gui.update();
                    pane.addItem(new GuiItem(item, getEquippingClick(gui, pane, type)), invSlot);

                    event.getWhoClicked().setItemOnCursor(getEquipmentFromSlot(slot).generateItem(null, 1));
                    getEquipmentFromSlot(slot).onUnequip(player, orionPlayer);
                    removeEquipment(slot);
                }
            }

            gui.update();
        };
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

    public static class EquipementHolders {

        public static final ItemStack HELMET_PLACEHOLDER = (ItemList.HELMET_PLACEHOLDER.generateItemStack(1)),
                CHESTPLATE_PLACEHOLDER = (ItemList.CHESTPLATE_PLACEHOLDER.generateItemStack(1)),
                LEGGINGS_PLACEHOLDER = (ItemList.LEGGINGS_PLACEHOLDER.generateItemStack(1)),
                BOOTS_PLACEHOLDER = (ItemList.BOOTS_PLACEHOLDER.generateItemStack(1)),
                RING_PLACEHOLDER = (ItemList.RING_PLACEHOLDER.generateItemStack(1)),
                BELT_PLACEHOLDER = (ItemList.BELT_PLACEHOLDER.generateItemStack(1));

        public static ItemStack getItemStack(Equipments equipments, EquipmentSlot slot) {
            if(equipments.getEquipmentFromSlot(slot) != null)
                return equipments.getEquipmentFromSlot(slot).generateItem(null, 1);
            else
                return getPlaceholder(slot);
        }

        private static ItemStack getPlaceholder(EquipmentSlot slot) {
            switch (slot) {
                case HELMET -> {
                    return HELMET_PLACEHOLDER;
                }
                case CHEST_PLATE -> {
                    return CHESTPLATE_PLACEHOLDER;
                }
                case LEGGINGS -> {
                    return LEGGINGS_PLACEHOLDER;
                }
                case BOOTS -> {
                    return BOOTS_PLACEHOLDER;
                }
                case LEFT_RING, RIGHT_RING -> {
                    return RING_PLACEHOLDER;
                }
                case BELT -> {
                    return BELT_PLACEHOLDER;
                }
            }
            return null;
        }
    }
}
