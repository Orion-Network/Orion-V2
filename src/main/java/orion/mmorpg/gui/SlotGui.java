package orion.mmorpg.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import org.bukkit.entity.Player;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.gui.UnusablePlaceholder;
import orion.mmorpg.items.gui.UsablePlaceholder;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.ItemUtils;

public class SlotGui extends ChestGui {
    Mmorpg mmorpg;
    Player player;
    OrionPlayer orionPlayer;

    public SlotGui(Mmorpg mmorpg, Player player, OrionPlayer orionPlayer) {
        super(1, "Slot Selection");
        this.mmorpg = mmorpg;
        this.player = player;
        this.orionPlayer = orionPlayer;
        this.addPane(getOutlinePane());
    }

    OutlinePane outlinePane = new OutlinePane(0, 0, 9, 1);

    private OutlinePane getOutlinePane() {
        for (OrionPlayer.SlotType slotType : orionPlayer.getSlotTypes()) {
            if (slotType.equals(OrionPlayer.SlotType.FREE_SLOT))
                outlinePane.addItem(new GuiItem(ItemUtils.generateItemStack("UNUSABLE_PLACEHOLDER", 1)));
            else if (slotType.equals(OrionPlayer.SlotType.USE_SLOT))
                outlinePane.addItem(new GuiItem(ItemUtils.generateItemStack("USABLE_PLACEHOLDER", 1)));
            else if (slotType.equals(OrionPlayer.SlotType.RESTRICTED_SLOT))
                outlinePane.addItem(new GuiItem(ItemUtils.generateItemStack("RESTRICTED_PLACEHOLDER", 1)));
        }
        outlinePane.setOnClick(e -> {
            e.setCancelled(true);
            if (e.getCurrentItem().getItemMeta().getCustomModelData() == new UnusablePlaceholder().getMODEL_ID()) {
                orionPlayer.getSlotTypes()[e.getSlot()] = OrionPlayer.SlotType.USE_SLOT;
                outlinePane.getItems().set(e.getSlot(),
                        new GuiItem(ItemUtils.generateItemStack("USABLE_PLACEHOLDER", 1)));
            } else if (e.getCurrentItem().getItemMeta().getCustomModelData() == new UsablePlaceholder().getMODEL_ID()) {
                orionPlayer.getSlotTypes()[e.getSlot()] = OrionPlayer.SlotType.FREE_SLOT;
                outlinePane.getItems().set(e.getSlot(),
                        new GuiItem(ItemUtils.generateItemStack("UNUSABLE_PLACEHOLDER", 1)));
            }
            this.update();
        });
        return outlinePane;
    }
}
