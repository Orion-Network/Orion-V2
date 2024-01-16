package orion.mmorpg.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.SpellList;

public class GrimoryGui extends ChestGui {
    Mmorpg mmorpg;
    Player player;
    OrionPlayer orionPlayer;
    OutlinePane spellPane = new OutlinePane(0, 0, 9, 1);

    public GrimoryGui(Mmorpg mmorpg, Player player, OrionPlayer orionPlayer) {
        super(1, ChatColor.YELLOW + "Grimory");
        this.mmorpg = mmorpg;
        this.player = player;
        this.orionPlayer = orionPlayer;
        this.setOnGlobalClick(e -> e.setCancelled(true));
        this.addPane(getSpellPane());
    }

    public OutlinePane getSpellPane() {
        for (String spell : orionPlayer.getSelectedSpells()) {
            if (spell == null)
                spellPane.addItem(new GuiItem(Mmorpg.INSTANCE.getItemManager().createItemstack("SPELL_PLACEHOLDER", 1)));
            else
            {
                try {
                    spellPane.addItem(new GuiItem(SpellList.getByClass((Class<? extends Spell>) Class.forName(spell)).getItem()));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
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
        OutlinePane spellList = new OutlinePane(0, 0, 9, 3);

        public SpellSelector(Mmorpg mmorpg, Player player, OrionPlayer orionPlayer, int clickedSlot) {
            super(3, "Select Spell");
            this.mmorpg = mmorpg;
            this.player = player;
            this.orionPlayer = orionPlayer;
            this.clickedSlot = clickedSlot;

            this.addPane(getSpellList());

            show(player);
        }

        public OutlinePane getSpellList() {
            for (Class<? extends Spell> spell : orionPlayer.getKnownSpells().keySet()) {
                spellList.addItem(new GuiItem(SpellList.getByClass(spell).getItem(), e -> {
                    orionPlayer.getSelectedSpells()[clickedSlot] = spell.getName();
                    player.closeInventory();
                    e.setCancelled(true);
                }));
            }
            return spellList;
        }
    }
}
