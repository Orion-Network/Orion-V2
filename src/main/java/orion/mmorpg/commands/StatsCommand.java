package orion.mmorpg.commands;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.managers.PlayerManager;
import orion.mmorpg.utils.OrionGson;
import orion.mmorpg.utils.Stats;

import java.util.Arrays;
import java.util.List;

public class StatsCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //TODO Finish the command
        if(args.length == 0) {
        } else {
            if(args[0].equalsIgnoreCase("player")) {

            } else if(args[0].equalsIgnoreCase("item")) {
                if(sender instanceof Player) {
                    ItemMeta meta = ((Player) sender).getInventory().getItemInMainHand().getItemMeta();
                    String internal_id = meta.getPersistentDataContainer().get(NamespacedKey.minecraft("internal_id"), PersistentDataType.STRING);
                    String type = meta.getPersistentDataContainer().get(NamespacedKey.minecraft("type"), PersistentDataType.STRING);
                    Stats stats = Mmorpg.getOrionGson().getGSON().fromJson(meta.getPersistentDataContainer().get(NamespacedKey.minecraft("stats"), PersistentDataType.STRING), Stats.class);
                    float damage = 0;
                    if(meta.getPersistentDataContainer().has(NamespacedKey.minecraft("damage"), PersistentDataType.FLOAT))
                        damage = meta.getPersistentDataContainer().get(NamespacedKey.minecraft("damage"), PersistentDataType.FLOAT);

                    StringBuilder builder = new StringBuilder();
                    builder.append(ChatColor.GRAY + "INTERNAL_ID: " + ChatColor.YELLOW + internal_id + "\n");
                    builder.append(ChatColor.GRAY + "TYPE: " + ChatColor.YELLOW + type + "\n");
                    if(damage != 0)
                        builder.append(ChatColor.GRAY + "DAMAGE: " + ChatColor.RED + damage + "\n");
                    if(stats != null) {
                        builder.append(String.join("\n", stats.statsAsLore()));
                        builder.append("\n");
                    }

                    builder.append(ChatColor.GRAY + "Data: " + ChatColor.LIGHT_PURPLE + meta.getPersistentDataContainer().getKeys());

                    sender.sendMessage(builder.toString());
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args[0].equalsIgnoreCase("item"))
            return Arrays.asList("hand", "0");
        return Arrays.asList("item", "player");
    }
}
