package orion.mmorpg.players.jobs;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.items.sample.BasicEquipmentItem;
import orion.mmorpg.items.sample.BasicReforgeStone;

public class Smithing extends AbstractJob {

    public Smithing(int experience, int level) {
        super(experience, level);
    }

    public ChestGui getGui() {
        ChestGui gui = new ChestGui(3, "Menu de forge");
        return gui;
    }

    BasicEquipmentItem equipmentItem; // 11
    BasicReforgeStone reforgeStoneItem; // 13
    ItemStack resultItem; // 15
    public ChestGui getUpgradeGui() {
        ChestGui gui = new ChestGui(3, "Menu d'amélioration");
        StaticPane guiPane = new StaticPane(9,3);
        /*guiPane.setOnClick(e -> {
            if(e.getCursor() == null) {
                Bukkit.broadcastMessage("Cancelled");
                e.setCancelled(true);
                return;
            }
            ItemStack itemStack = e.getCursor();
            if(e.getSlot() != 11)
                e.setCancelled(true);
            ItemStack cloneStack = itemStack.clone();
            if(e.getSlot() == 11 && BasicEquipmentItem.class.isAssignableFrom(ItemList.getItemFromItemstack(itemStack).getClass())) {
                equipmentItem = (BasicEquipmentItem) ItemList.getItemFromItemstack(itemStack);
                itemStack.setAmount(0);
            }
            if(e.getSlot() == 13 && BasicReforgeStone.class.isAssignableFrom(ItemList.getItemFromItemstack(itemStack).getClass())) {
                guiPane.addItem(new GuiItem(cloneStack), 5, 1);
                reforgeStoneItem = (BasicReforgeStone) ItemList.getItemFromItemstack(itemStack);
                itemStack.setAmount(0);
            }

            gui.update();
            Bukkit.broadcastMessage(e.getSlot() + "");
            if(!e.getSlotType().equals(InventoryType.SlotType.RESULT))
                return;
            if(equipmentItem != null && reforgeStoneItem != null) {
                equipmentItem.setReforge(ItemReforge.STRONG);
                guiPane.addItem(new GuiItem(equipmentItem.generateItem(equipmentItem, 1)), 7, 1);
                //e.getWhoClicked().getInventory().setItemInMainHand(item.get().generateItem(item.get(), 1));
            }
        });*/

        gui.addPane(guiPane);
        return gui;
    }
}
