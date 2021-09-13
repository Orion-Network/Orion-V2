package orion.mmorpg.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.SpellList;

public class GrimoryGui extends ChestGui {
    Mmorpg mmorpg;
    Player player;
    OrionPlayer orionPlayer;

    public GrimoryGui(Mmorpg mmorpg, Player player, OrionPlayer orionPlayer) {
        super(1, ChatColor.YELLOW + "Grimory");
        this.mmorpg = mmorpg;
        this.player = player;
        this.orionPlayer = orionPlayer;
        this.setOnGlobalClick(e -> e.setCancelled(true));
        this.addPane(getSpellPane());
    }

    OutlinePane spellPane = new OutlinePane(0,0,9,1);
    public OutlinePane getSpellPane() {
        for(String spell : orionPlayer.getSelectedSpells()) {
            if(spell == null)
                spellPane.addItem(new GuiItem(ItemList.SPELL_PLACEHOLDER.getProcessedItem(1)));
            else
                spellPane.addItem(new GuiItem(SpellList.valueOf(spell).getItem()));
        }

        spellPane.setOnClick(e -> {
            new SpellSelector(mmorpg, player, orionPlayer, e.getSlot());
        });
        return spellPane;
    }

    class SpellSelector extends ChestGui {
        Mmorpg mmorpg;
        Player player;
        OrionPlayer orionPlayer;
        int clickedSlot;

        public SpellSelector(Mmorpg mmorpg, Player player, OrionPlayer orionPlayer, int clickedSlot) {
            super(3, "Select Spell");
            this.mmorpg = mmorpg;
            this.player = player;
            this.orionPlayer = orionPlayer;
            this.clickedSlot = clickedSlot;

            this.addPane(getSpellList());

            show(player);
        }

        OutlinePane spellList = new OutlinePane(0,0,9,3);
        public OutlinePane getSpellList() {
            for(String spell : orionPlayer.getSpells().keySet()) {
                spellList.addItem(new GuiItem(SpellList.valueOf(spell).getItem(), e -> {
                    orionPlayer.getSelectedSpells()[clickedSlot] = spell;
                    player.closeInventory();
                    e.setCancelled(true);
                }));
            }
            return spellList;
        }
    }
}
