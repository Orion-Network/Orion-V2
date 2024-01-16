package orion.mmorpg.players.jobs;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PatternPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.Reforgeable;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.items.sample.BasicReforgeStone;
import orion.mmorpg.utils.ItemUtils;
import orion.mmorpg.utils.Utils;

public class Smithing extends AbstractJob {

    public Smithing() {
        super(JobType.SMITHING);
    }

    public Smithing(int experience, int level) {
        super(JobType.SMITHING, experience, level);
    }

    public ChestGui getGui() {
        ChestGui gui = new ChestGui(3, "Menu de forge");
        return gui;
    }

    boolean validated = false;
    public ChestGui getUpgradeGui() {
        ChestGui gui = new ChestGui(3, "Menu d'amélioration");
        gui.setOnBottomClick(e -> {
            if(e.isShiftClick()) {
                e.setCancelled(true);
            }
        });
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
            }
        }), 4, 2);
        guiPane.setOnClick(e -> {
            if(e.getSlot() == 10 || e.getSlot() == 16) {
                if(e.getCursor().getType() == Material.AIR) {
                    updateGui(gui);
                    return;
                }
                if(e.getSlot() == 10 && !(ItemUtils.getItemFromItemstack(e.getCursor()) instanceof Reforgeable)) {
                    Utils.sendErrorMessage(e.getWhoClicked(), "Item non-valide. Veuillez mettre un objet reforgeable");
                    e.setCancelled(true);
                }
                if(e.getSlot() == 16) {
                    if( !BasicReforgeStone.class.isAssignableFrom(ItemUtils.getItemFromItemstack(e.getCursor()).getClass())) {
                        Utils.sendErrorMessage(e.getWhoClicked(), "Item non-valide. Veuillez mettre une reforge stone");
                        e.setCancelled(true);
                    }
                    BasicReforgeStone reforgeStone = (BasicReforgeStone) ItemUtils.getItemFromItemstack(e.getCursor());
                    if(reforgeStone.getSmithingLevelRequired() > this.getLevel()) {
                        Utils.sendErrorMessage(e.getWhoClicked(), "Vous n'avez pas le niveau requis pour utiliser cette reforge stone");
                        e.setCancelled(true);
                    }
                }

                updateGui(gui);
            } else {
                if(e.getSlot() == 13) {
                    if(gui.getInventory().getItem(13) != null && validated) {
                        validated = false;
                        return;
                    } else {
                        Utils.sendErrorMessage(e.getWhoClicked(), "Vous devez confirmer avant de prendre votre objet");
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
                BasicItem basicEquipmentItem = ItemUtils.getItemFromItemstack(gui.getInventory().getItem(10).clone());
                ((Reforgeable)basicEquipmentItem).setReforge(((BasicReforgeStone) ItemUtils.getItemFromItemstack(gui.getInventory().getItem(16).clone())).getReforge());
                gui.getInventory().setItem(13, basicEquipmentItem.generateItem(gui.getInventory().getItem(10).clone(), 1));
            }
            else {
                gui.getInventory().setItem(13, new ItemStack(Material.AIR));
            }
        }, 10L);
    }
}
