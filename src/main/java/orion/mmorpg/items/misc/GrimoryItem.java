package orion.mmorpg.items.misc;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.gui.GrimoryGui;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.items.sample.BasicTool;
import orion.mmorpg.items.sample.BasicUsableItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.SpellList;
import orion.mmorpg.utils.Namespaces;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

public class GrimoryItem extends BasicUsableItem {
    public GrimoryItem() {
        super(Material.BOOK, "Grimory", "GRIMORY", 1, Collections.singletonList("A grimory containing all your spells"),
                Rarity.RARE);
    }

    @Override
    public ItemStack generateItem(ItemStack item, int amount) {
        if (item == null)
            item = new ItemStack(MATERIAL);
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(Namespaces.slot, PersistentDataType.INTEGER, 0);
        item.setItemMeta(meta);
        return super.generateItem(item, amount);
    }

    // TODO cast spell when select spell in selector
    // TODO switch only to the next spell in the selector
    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        int slot = container.get(Namespaces.slot, PersistentDataType.INTEGER);
        if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Spell spell;
            if (data.getSelectedSpells()[slot] == null)
                return;
            try {
                spell = (Spell) Class.forName(data.getSelectedSpells()[slot]).getConstructor().newInstance();
            } catch (ClassNotFoundException | InvocationTargetException | InstantiationException
                    | IllegalAccessException | NoSuchMethodException ex) {
                ex.printStackTrace();
                return;
            }
            Utils.sendDebugMessage(e.getPlayer(),
                    "slot : " + slot + "Spell : " + data.getSelectedSpells()[slot] + " in spell : ");
            boolean canLaunch = spell.beforeLaunch(mmorpg, e.getPlayer(), data, true);
            if (!canLaunch && data.getCooldownSpells().containsKey(spell.getClass())
                    && data.getCooldownSpells().get(spell.getClass()) > System.currentTimeMillis())
                e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                        Math.round((data.getCooldownSpells().get(spell.getClass()) - System.currentTimeMillis()) / 1000)
                                + "s restantes."));
        } else {
            if (e.getPlayer().isSneaking())
                new GrimoryGui(mmorpg, e.getPlayer(), data).show(e.getPlayer());
            else {
                if (slot == 8)
                    container.set(Namespaces.slot, PersistentDataType.INTEGER, 0);
                else
                    container.set(Namespaces.slot, PersistentDataType.INTEGER, slot + 1);
                slot = container.get(Namespaces.slot, PersistentDataType.INTEGER);

                String[] slots = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };
                slots[slot] = ChatColor.GREEN + "" + (slot + 1) + ChatColor.WHITE;
                e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        new TextComponent(StringUtils.join(slots, " ")));
                if (data.getSelectedSpells()[slot] != null) {
                    SpellList spell = null;
                    for (SpellList s : SpellList.values()) {
                        if (s.getSpellClass().getName().equals(data.getSelectedSpells()[slot])) {
                            spell = s;
                        }
                    }
                    if (spell == null)
                        return;
                    meta.setDisplayName(this.RARITY.getChatColor() + this.NAME + " : " + spell.getName());
                } else {
                    meta.setDisplayName(this.RARITY.getChatColor() + this.NAME);
                }
                item.setItemMeta(meta);
                e.getPlayer().updateInventory();
            }
        }
    }
}
