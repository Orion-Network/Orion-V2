package orion.mmorpg.players;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import fr.nexomega.orionmob.gui.PetGui;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.utils.ItemUtils;
import orion.mmorpg.utils.Utils;

import java.util.HashMap;
import java.util.function.Consumer;

//TODO add for pet and mount
@Getter
public class Equipments {
    private final HashMap<BasicArmorItem.ArmorType, BasicArmorItem> equipments = new HashMap<>();
    private Player owner;

    public Utils.Pair<Boolean, String> addEquipment(BasicArmorItem item) {
        if (equipments.containsKey(item.getARMORTYPE()))
            return new Utils.Pair<>(false, "le slot de cet objet n'est pas libre.");
        equipments.put(item.getARMORTYPE(), item);
        return new Utils.Pair<>(true, "l'objet a bien été équipé.");
    }

    public Utils.Pair<Boolean, String> removeEquipment(BasicArmorItem.ArmorType slot) {
        if (!equipments.containsKey(slot))
            return new Utils.Pair<>(false, "le slot est vide.");
        equipments.remove(slot);
        return new Utils.Pair<>(true, "l'objet a bien été retiré.");
    }

    public BasicArmorItem getEquipmentFromSlot(BasicArmorItem.ArmorType slot) {
        if (equipments.containsKey(slot))
            return equipments.get(slot);
        return null;
    }

    public StaticPane getEquipmentPane(ChestGui gui) {
        StaticPane pane = new StaticPane(0, 0, 3, 4);
        pane.setOnClick(event -> event.setCancelled(true));
        pane.addItem(new GuiItem(EquipementHolders.getItemStack(this, BasicArmorItem.ArmorType.HELMET),
                getEquippingClick(gui, pane, BasicArmorItem.ArmorType.HELMET)), 1, 0);
        pane.addItem(new GuiItem(EquipementHolders.getItemStack(this, BasicArmorItem.ArmorType.CHESTPLATE),
                getEquippingClick(gui, pane, BasicArmorItem.ArmorType.CHESTPLATE)), 1, 1);
        pane.addItem(new GuiItem(EquipementHolders.getItemStack(this, BasicArmorItem.ArmorType.LEGGINGS),
                getEquippingClick(gui, pane, BasicArmorItem.ArmorType.LEGGINGS)), 1, 2);
        pane.addItem(new GuiItem(EquipementHolders.getItemStack(this, BasicArmorItem.ArmorType.BOOTS),
                getEquippingClick(gui, pane, BasicArmorItem.ArmorType.BOOTS)), 1, 3);

        pane.addItem(new GuiItem(EquipementHolders.getItemStack(this, BasicArmorItem.ArmorType.BELT),
                getEquippingClick(gui, pane, BasicArmorItem.ArmorType.BELT)), 2, 2);
        pane.addItem(new GuiItem(EquipementHolders.getItemStack(this, BasicArmorItem.ArmorType.RING),
                getEquippingClick(gui, pane, BasicArmorItem.ArmorType.RING)), 0, 2);
        pane.addItem(new GuiItem(EquipementHolders.getItemStack(this, BasicArmorItem.ArmorType.EARRINGS),
                getEquippingClick(gui, pane, BasicArmorItem.ArmorType.RING)), 2, 1);
        pane.addItem(new GuiItem(EquipementHolders.getItemStack(this, BasicArmorItem.ArmorType.NECKLACE),
                getEquippingClick(gui, pane, BasicArmorItem.ArmorType.RING)), 0, 1);

        OrionPlayer orionPlayer = OrionPlayer.getOrionPlayer(getOwner());
        pane.addItem(new GuiItem(ItemUtils.generateItemStack("PET_PLACEHOLDER", 1),
                e -> new PetGui(getOwner().getPlayer(), orionPlayer).show(getOwner())), 0, 3);
        pane.addItem(new GuiItem(ItemUtils.generateItemStack("MOUNT_PLACEHOLDER", 1),
                e -> new PetGui(getOwner().getPlayer(), orionPlayer).show(getOwner())), 2, 3);

        return pane;
    }

    public Consumer<InventoryClickEvent> getEquippingClick(ChestGui gui, StaticPane pane,
            BasicArmorItem.ArmorType type) {
        return event -> {
            Player player = (Player) event.getWhoClicked();
            OrionPlayer orionPlayer = OrionPlayer.getOrionPlayer(player);
            Slot invSlot = Slot.fromIndex(event.getSlot() - (6 * (event.getSlot() / 9)));
            if (event.getAction().equals(InventoryAction.SWAP_WITH_CURSOR)) {
                if (ItemUtils.isItem(event.getCursor())
                        && ItemUtils.getItemFromItemstack(event.getCursor()) instanceof BasicArmorItem armor
                        && armor.getARMORTYPE().equals(type)) {
                    if (getEquipmentFromSlot(type) != null) {
                        event.getWhoClicked().setItemOnCursor(getEquipmentFromSlot(type).generateItem(null, 1));
                        getEquipmentFromSlot(type).onUnequip(player, orionPlayer);
                        removeEquipment(type);
                    } else {
                        event.getWhoClicked().setItemOnCursor(null);
                    }
                    armor.onEquip(player, orionPlayer);
                    addEquipment(armor);

                    ItemStack item = EquipementHolders.getItemStack(this, type);

                    pane.removeItem(invSlot);
                    gui.update();
                    pane.addItem(new GuiItem(item, getEquippingClick(gui, pane, type)), invSlot);
                }
            } else {
                if (getEquipmentFromSlot(type) != null) {
                    ItemStack item = EquipementHolders.getPlaceholder(type);
                    pane.removeItem(invSlot);
                    gui.update();
                    pane.addItem(new GuiItem(item, getEquippingClick(gui, pane, type)), invSlot);

                    event.getWhoClicked().setItemOnCursor(getEquipmentFromSlot(type).generateItem(null, 1));
                    getEquipmentFromSlot(type).onUnequip(player, orionPlayer);
                    removeEquipment(type);
                }
            }

            orionPlayer.updateHealthbar();

            gui.update();
        };
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public static class EquipementHolders {

        public static final ItemStack HELMET_PLACEHOLDER = (ItemUtils.generateItemStack("HELMET_PLACEHOLDER", 1)),
                CHESTPLATE_PLACEHOLDER = (ItemUtils.generateItemStack("CHESTPLATE_PLACEHOLDER", 1)),
                LEGGINGS_PLACEHOLDER = (ItemUtils.generateItemStack("LEGGINGS_PLACEHOLDER", 1)),
                BOOTS_PLACEHOLDER = (ItemUtils.generateItemStack("BOOTS_PLACEHOLDER", 1)),
                RING_PLACEHOLDER = (ItemUtils.generateItemStack("RING_PLACEHOLDER", 1)),
                BELT_PLACEHOLDER = (ItemUtils.generateItemStack("BELT_PLACEHOLDER", 1)),
                EARRINGS_PLACEHOLDER = (ItemUtils.generateItemStack("EARRING_PLACEHOLDER", 1)),
                NECKLACE_PLACEHOLDER = (ItemUtils.generateItemStack("NECKLACE_PLACEHOLDER", 1));

        public static ItemStack getItemStack(Equipments equipments, BasicArmorItem.ArmorType slot) {
            if (equipments.getEquipmentFromSlot(slot) != null)
                return equipments.getEquipmentFromSlot(slot).generateItem(null, 1);
            else
                return getPlaceholder(slot);
        }

        private static ItemStack getPlaceholder(BasicArmorItem.ArmorType slot) {
            switch (slot) {
            case HELMET -> {
                return HELMET_PLACEHOLDER;
            }
            case CHESTPLATE -> {
                return CHESTPLATE_PLACEHOLDER;
            }
            case LEGGINGS -> {
                return LEGGINGS_PLACEHOLDER;
            }
            case BOOTS -> {
                return BOOTS_PLACEHOLDER;
            }
            case RING -> {
                return RING_PLACEHOLDER;
            }
            case BELT -> {
                return BELT_PLACEHOLDER;
            }
            case EARRINGS -> {
                return EARRINGS_PLACEHOLDER;
            }
            case NECKLACE -> {
                return NECKLACE_PLACEHOLDER;
            }
            }
            return null;
        }
    }
}
