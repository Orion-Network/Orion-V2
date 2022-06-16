package orion.mmorpg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.EnumUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.misc.Gift;
import orion.mmorpg.items.misc.MasterScrollItem;
import orion.mmorpg.items.misc.SpellScroll;
import orion.mmorpg.players.abilities.SpellList;
import orion.mmorpg.utils.Utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ItemCommand implements TabExecutor {
    //TODO Rework command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("give")) {
                if (args.length > 2) {
                    if (args[2].equalsIgnoreCase("master_scroll")) {
                        Bukkit.getPlayer(args[1]).getInventory().addItem(((MasterScrollItem) ItemList.valueOf(args[2].toUpperCase()).getItem()).generateScroll(args[3], Integer.parseInt(args[4])));
                        return true;
                    } else if(args[2].equalsIgnoreCase("spell_scroll")) {
                        Bukkit.getPlayer(args[1]).getInventory().addItem(((SpellScroll) ItemList.valueOf(args[2].toUpperCase()).getItem()).generateScroll(args[3]));
                        return true;
                    }
                    if(!EnumUtils.isValidEnum(ItemList.class, args[2].toUpperCase())) {
                        Utils.sendErrorMessage((Entity) sender, "L'item n'existe pas.");
                        return false;
                    }
                    Bukkit.getPlayer(args[1]).getInventory().addItem(ItemList.valueOf(args[2]).getProcessedItem(1));
                } else {
                    if (sender instanceof Player) {
                        if (args[2].equalsIgnoreCase("master_scroll")) {
                            ((Player) sender).getInventory().addItem(((MasterScrollItem) ItemList.valueOf(args[1].toUpperCase()).getItem()).generateScroll(args[2], Integer.parseInt(args[3])));
                            return true;
                        } else if(args[2].equalsIgnoreCase("spell_scroll")) {
                            Bukkit.getPlayer(args[1]).getInventory().addItem(((MasterScrollItem) ItemList.valueOf(args[2].toUpperCase()).getItem()).generateScroll(args[3], Integer.parseInt(args[4])));
                            return true;
                        }
                        ((Player) sender).getInventory().addItem(ItemList.valueOf(args[1]).getProcessedItem(1));
                    }
                }
            } else if (args[0].equalsIgnoreCase("drop")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (args[1].equalsIgnoreCase("gift")) {
                        ItemStack gift = ((Gift) ItemList.GIFT.getItem()).generateGift(args[2], p.getDisplayName());
                        Item item = p.getWorld().dropItem(p.getTargetBlock(null, 20).getLocation(), gift);
                        item.setGlowing(true);
                        return true;
                    }
                    p.getWorld().dropItem(p.getTargetBlock(null, 20).getLocation(), ItemList.valueOf(args[1]).getProcessedItem(1));
                }
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tab = new LinkedList<>();
        if (args[0].equalsIgnoreCase("give")) {
            if (args.length == 2) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    tab.add(p.getName());
                }
                return tab;
            }
            if (args[2].equalsIgnoreCase("master_scroll") || args[2].equalsIgnoreCase("spell_scroll")) {
                for (SpellList spell : SpellList.values())
                    if (spell.name().startsWith(args[args.length - 1].toUpperCase()))
                        tab.add(spell.name());
                return tab;
            }

            for (ItemList item : ItemList.values())
                if (item.name().startsWith(args[args.length - 1].toUpperCase()))
                    tab.add(item.name());

            return tab;

        } else if (args[0].equalsIgnoreCase("drop")) {
            if (args[1].equalsIgnoreCase("gift")) {
                for (ItemList item : ItemList.values())
                    if (item.name().startsWith(args[args.length - 1].toUpperCase()))
                        tab.add(item.name());

                return tab;
            }
            for (ItemList item : ItemList.values())
                if (item.name().startsWith(args[args.length - 1].toUpperCase()))
                    tab.add(item.name());

            return tab;
        }
        return Arrays.asList("give", "drop");
    }
}
