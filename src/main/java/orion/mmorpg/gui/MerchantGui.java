package orion.mmorpg.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Utils;

import java.util.ArrayList;

public class MerchantGui extends ChestGui {

    private final ArrayList<Buyable> buyables = new ArrayList<>();
    private final OrionPlayer orionPlayer;
    private final Player player;
    public MerchantGui(String title, OrionPlayer orionPlayer, Player player) {
        super(6, title);

        this.orionPlayer = orionPlayer;
        this.player = player;
    }

    public void addBuyable(String currencyName, int amount, ItemStack itemStack) {
        buyables.add(new Buyable(currencyName, amount, itemStack));
    }

    @Override
    public void show(@NotNull HumanEntity humanEntity) {
        this.addPane(getBuyPane());
        super.show(humanEntity);
    }

    public OutlinePane getBuyPane() {
        OutlinePane outlinePane = new OutlinePane(0, 0, 9, 9);

        outlinePane.setOnClick(event -> {
            event.setCancelled(true);
        });

        for (Buyable buyable : buyables) {

            ItemStack displayItem = buyable.itemStack.clone();
            ItemMeta itemMeta = displayItem.getItemMeta();
            itemMeta.getLore().add("");
            itemMeta.getLore().add("Cost: " + buyable.amount + " " + buyable.currencyName);
            displayItem.setItemMeta(itemMeta);

            outlinePane.addItem(new GuiItem(displayItem, event -> {
                if (orionPlayer.getCurrencies().hasEnough(buyable.currencyName, buyable.amount)) {
                    orionPlayer.getCurrencies().remove(buyable.currencyName, buyable.amount);
                    player.getInventory().addItem(buyable.itemStack.clone());
                } else {
                    long missing = buyable.amount - orionPlayer.getCurrencies().get(buyable.currencyName);
                    Utils.sendInfoMessage(player, "Il vous manque " + Utils.NumberFormat.READABLE.format(missing) + " " + buyable.currencyName);
                }
            }));
        }

        return outlinePane;
    }

    private static class Buyable {
        private String currencyName;
        private int amount;

        private ItemStack itemStack;

        public Buyable(String currencyName, int amount, ItemStack itemStack) {
            this.currencyName = currencyName;
            this.amount = amount;
            this.itemStack = itemStack;
        }
    }
}
