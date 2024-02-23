package orion.mmorpg.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.SpellList;

import java.util.ArrayList;

public class SpellGui extends ChestGui {
    Player player;
    OrionPlayer orionPlayer;
    final int rows;

    public SpellGui(Player player, OrionPlayer orionPlayer) {
        super((orionPlayer.getKnownSpells().size() / 9 <= 0 ? 1 : Math.abs(orionPlayer.getKnownSpells().size() / 9)),
                "Menu des sorts");
        this.player = player;
        this.orionPlayer = orionPlayer;
        this.rows = (orionPlayer.getKnownSpells().size() / 9 <= 0 ? 1
                : Math.abs(orionPlayer.getKnownSpells().size() / 9));
        this.setOnTopClick(e -> e.setCancelled(true));

        this.addPane(createSpellPane());
    }

    public OutlinePane createSpellPane() {
        OutlinePane spellPane = new OutlinePane(0, 0, 9, rows);
        orionPlayer.getKnownSpells().forEach((spell, knowledge) -> {
            SpellList sp = SpellList.getByClass(spell);
            Material material = sp.getMaterial();
            ArrayList<String> lore = new ArrayList<>();
            lore.addAll(sp.getLore());
            lore.addAll(knowledge.getKnowledgeLore());
            lore.add("§7" + generateProgressBar(knowledge.getKnowledge(), knowledge.getNextMilestone()));
            ItemStack item = BasicItem.generateTemplateBasicItem(material, sp.getRarity().getChatColor() + sp.getName(),
                    1, lore, sp.getRarity()).generateItem(null, 1);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);
            spellPane.addItem(new GuiItem(item));
        });
        return spellPane;
    }

    public String generateProgressBar(int value, int max) {
        if (value > max)
            value = max;
        System.out.println("value: " + value + " max: " + max);
        StringBuilder sb = new StringBuilder();
        int percentage = Math.round(((float) value / max) * 100);
        sb.append(ChatColor.GOLD + "[");
        for (int i = 0; i < 10; i++) {
            if (i < percentage / 10) {
                sb.append(ChatColor.GOLD + "█");
            } else {
                sb.append(ChatColor.GRAY + "█");
            }
        }
        sb.append(ChatColor.GOLD + "] " + ChatColor.GRAY + "(" + percentage + "%)");

        return sb.toString();
    }
}
