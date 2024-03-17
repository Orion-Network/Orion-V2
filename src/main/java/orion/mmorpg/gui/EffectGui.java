package orion.mmorpg.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.OEffect;

import java.util.LinkedList;

public class EffectGui extends ChestGui {
    Player p;
    OrionPlayer orionPlayer;

    public EffectGui(Player p, OrionPlayer orionPlayer) {
        super(5, ChatColor.YELLOW + "Effets");
        this.p = p;
        this.orionPlayer = orionPlayer;

        this.addPane(getStatsPane());
        this.setOnGlobalClick(e -> {
            e.setCancelled(true);
            this.update();
        });

    }

    OutlinePane pane = new OutlinePane(0, 0, 9, 5);

    private OutlinePane getStatsPane() {
        orionPlayer.getOrionEffects().forEach((event, effectList) -> {
            effectList.forEach((id, effect) -> {
                if (effect.getRemainingTime() <= 0) {
                    return;
                }
                ItemStack item = getItemStack(effect);
                pane.addItem(new GuiItem(item, e -> {
                    e.setCancelled(true);
                    orionPlayer.getOrionEffects().get(event).remove(id);
                    this.update();
                }));
            });
        });
        return pane;
    }

    /**
     * provides a String representation of the given time
     *
     * @return {@code millis} in hh:mm:ss format
     */
    public final String formatTime(long millis) {
        long secs = millis / 1000;
        return String.format("%02d:%02d:%02d", secs / 3600, (secs % 3600) / 60, secs % 60);
    }

    @NotNull
    private ItemStack getItemStack(OEffect effect) {
        ItemStack item = new ItemStack(Material.POTION);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + effect.getName());
        LinkedList<String> lore = new LinkedList<>();
        lore.add(ChatColor.GRAY + "Durée: " + formatTime(effect.getRemainingTime()));
        meta.setLore(lore);
        meta.setDisplayName(ChatColor.YELLOW + effect.getName());
        item.setItemMeta(meta);
        return item;
    }

}
