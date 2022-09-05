package orion.mmorpg.players.jobs;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.PatternPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Pattern;
import org.apache.commons.lang.math.IEEE754rUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.sample.BasicEquipmentItem;
import orion.mmorpg.items.sample.BasicReforgeStone;
import orion.mmorpg.utils.Utils;

public class Smithing extends AbstractJob {

    public Smithing(int experience, int level) {
        super(experience, level);
    }

    public ChestGui getGui() {
        ChestGui gui = new ChestGui(3, "Menu de forge");
        return gui;
    }

    boolean validated = false;
    public ChestGui getUpgradeGui() {
        ChestGui gui = new ChestGui(3, "Menu d'amélioration");
        gui.setOnClose(e -> {
            if(gui.getInventory().getItem(10) != null)
                e.getPlayer().getInventory().addItem(gui.getInventory().getItem(10));
            if(gui.getInventory().getItem(16) != null)
                e.getPlayer().getInventory().addItem(gui.getInventory().getItem(16));
            if(gui.getInventory().getItem(13) != null && validated == true)
                e.getPlayer().getInventory().addItem(gui.getInventory().getItem(13));
        });

        Pattern pattern = new Pattern(
                "111111111",
                "101101101",
                "111101111");
        PatternPane maskPane = new PatternPane(0,0,9,3, pattern);
        maskPane.bindItem('1', new GuiItem(new ItemStack(Material.RED_STAINED_GLASS_PANE), e -> e.setCancelled(true)));

        StaticPane guiPane = new StaticPane(9,3);
        guiPane.addItem(new GuiItem(new ItemStack(Material.ANVIL), e -> {
            if(gui.getInventory().getItem(10) != null && gui.getInventory().getItem(16) != null) {
                if(validated) {
                    e.getWhoClicked().sendMessage("Veuillez prendre l'item précédent");
                    return;
                }
                validated = true;
                gui.getInventory().getItem(10).setAmount(gui.getInventory().getItem(10).getAmount() - 1);
                gui.getInventory().getItem(16).setAmount(gui.getInventory().getItem(16).getAmount() - 1);
                //gui.getInventory().setItem(16, new ItemStack(Material.AIR));
            }
        }), 4, 2);
        guiPane.setOnClick(e -> {
            if(e.getSlot() == 10 || e.getSlot() == 16) {
                if(e.getCursor().getType() == Material.AIR) {
                    updateGui(gui);
                    return;
                }
                if(e.getSlot() == 10 && !BasicEquipmentItem.class.isAssignableFrom(ItemList.getItemFromItemstack(e.getCursor()).getClass())) {
                    Utils.sendErrorMessage(e.getWhoClicked(), "Item non-valide. Veuillez mettre un équipement");
                    e.setCancelled(true);
                }
                if(e.getSlot() == 16 && !BasicReforgeStone.class.isAssignableFrom(ItemList.getItemFromItemstack(e.getCursor()).getClass())) {
                    Utils.sendErrorMessage(e.getWhoClicked(), "Item non-valide. Veuillez mettre une reforge stone");
                    e.setCancelled(true);
                }

                updateGui(gui);
            } else {
                if(e.getSlot() == 13) {
                    if(gui.getInventory().getItem(13) != null && validated == true) {
                        validated = false;
                        return;
                    } else {
                        Utils.sendErrorMessage(e.getWhoClicked(), "Vous devez confirmer avant de prendre vore objet");
                        e.setCancelled(true);
                    }
                }
                e.setCancelled(true);
            }
        });

        gui.addPane(maskPane);
        gui.addPane(guiPane);
        return gui;
    }

    private void updateGui(ChestGui gui) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Mmorpg.INSTANCE, () -> {
            if(gui.getInventory().getItem(10) != null && gui.getInventory().getItem(16) != null) {
                BasicEquipmentItem basicEquipmentItem = (BasicEquipmentItem) ItemList.getItemFromItemstack(gui.getInventory().getItem(10).clone());
                basicEquipmentItem.setReforge(((BasicReforgeStone) ItemList.getItemFromItemstack(gui.getInventory().getItem(16).clone())).getReforge());
                gui.getInventory().setItem(13, basicEquipmentItem.generateItem(gui.getInventory().getItem(10).clone(), 1));
            }
            else {
                gui.getInventory().setItem(13, new ItemStack(Material.AIR));
            }
        }, 10L);
    }
}
