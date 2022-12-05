package orion.mmorpg.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
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
        super((orionPlayer.getKnownSpells().size()/9 <= 0 ? 1 : Math.abs(orionPlayer.getKnownSpells().size()/9)), "Menu des sorts");
        this.player = player;
        this.orionPlayer = orionPlayer;
        this.rows = (orionPlayer.getKnownSpells().size()/9 <= 0 ? 1 : Math.abs(orionPlayer.getKnownSpells().size()/9));
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
            ItemStack item = BasicItem.generateTemplateBasicItem(material, sp.getRarity().getChatColor() + sp.getName(), 1, lore, sp.getRarity()).generateItem(null, 1);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);
            spellPane.addItem(new GuiItem(item));
        });
        return spellPane;
    }
}
