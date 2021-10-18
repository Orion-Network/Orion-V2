package orion.mmorpg.items.misc;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.gui.GrimoryGui;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.SpellList;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Utils;

public class GrimoryItem extends BasicItem {
    public GrimoryItem() {
        super(Material.BOOK, "Grimory", "GRIMORY", 1, "A grimory containing all your spells", Rarity.RARE);
    }

    @Override
    public ItemStack generateItem(ItemStack item, int amount) {
        if(item == null)
            item = new ItemStack(MATERIAL);
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(NamespacedKey.minecraft("slot"), PersistentDataType.INTEGER, 0);
        item.setItemMeta(meta);
        return super.generateItem(item, amount);
    }

    //TODO cast spell when select spell in selector
    //TODO change grimory amount based on slot
    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        int slot = container.get(NamespacedKey.minecraft("slot"), PersistentDataType.INTEGER);
        if(e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Spell spell = data.getSpells().get(data.getSelectedSpells()[slot]);
            Utils.sendDebugMessage(e.getPlayer(), "slot : " +slot + "Spell : " + data.getSelectedSpells()[slot] +" in spell : " + data.getSpells().get(data.getSelectedSpells()[slot]));
            if(spell != null)
                spell.beforeLaunch(mmorpg, e.getPlayer(), data, true);
            else
                Utils.sendInfoMessage(e.getPlayer(), "Le sort n'existe pas");
        } else {
            if(e.getPlayer().isSneaking())
                new GrimoryGui(mmorpg, e.getPlayer(), data).show(e.getPlayer());
            else {
                if (slot == 8)
                    container.set(NamespacedKey.minecraft("slot"), PersistentDataType.INTEGER, 0);
                else
                    container.set(NamespacedKey.minecraft("slot"), PersistentDataType.INTEGER, slot + 1);
                slot = container.get(NamespacedKey.minecraft("slot"), PersistentDataType.INTEGER);
                if(data.getSelectedSpells()[slot] != null) {
                    SpellList spell = SpellList.valueOf(data.getSelectedSpells()[slot]);
                    meta.setDisplayName(this.RARITY.getChatColor() + this.NAME + " : " + spell.getName());
                } else {
                    meta.setDisplayName(this.RARITY.getChatColor() + this.NAME);
                }
                item.setItemMeta(meta);
            }
        }
    }
}
