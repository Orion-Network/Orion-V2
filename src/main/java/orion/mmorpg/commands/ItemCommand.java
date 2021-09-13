package orion.mmorpg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.misc.Gift;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ItemCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length > 1) {
            if (args[0].equalsIgnoreCase("give")) {
                if (args.length > 2) {
                    Bukkit.getPlayer(args[3]).getInventory().addItem(ItemList.valueOf(args[1]).getProcessedItem(1));
                } else {
                    if(sender instanceof Player)
                        ((Player) sender).getInventory().addItem(ItemList.valueOf(args[1]).getProcessedItem(1));
                }
            } else if(args[0].equalsIgnoreCase("drop")) {
                if(sender instanceof Player) {
                    Player p = (Player) sender;
                    if(args[1].equalsIgnoreCase("gift")) {
                        //TODO Add glowing to item
                        ItemStack gift = ((Gift)ItemList.GIFT.getItem()).generateGift(args[2], p.getDisplayName());
                        Item item = p.getWorld().dropItem(p.getTargetBlock(null, 20).getLocation(), gift);
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
        if(args[0].equalsIgnoreCase("give")) {
            for(ItemList item : ItemList.values())
                if(item.name().startsWith(args[args.length-1].toUpperCase()))
                    tab.add(item.name());

                return tab;

        } else if(args[0].equalsIgnoreCase("drop")) {
            if(args[1].equalsIgnoreCase("gift")) {
                for(ItemList item : ItemList.values())
                    if(item.name().startsWith(args[args.length-1].toUpperCase()))
                        tab.add(item.name());

                return tab;
            }
            for(ItemList item : ItemList.values())
                if(item.name().startsWith(args[args.length-1].toUpperCase()))
                    tab.add(item.name());

            return tab;
        }
        return Arrays.asList("give", "drop");
    }
}
