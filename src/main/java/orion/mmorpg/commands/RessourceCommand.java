package orion.mmorpg.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.tools.ToolType;
import orion.mmorpg.managers.RessourceManager;
import orion.mmorpg.players.jobs.JobType;
import orion.mmorpg.utils.Utils;
import orion.mmorpg.world.Ressource;

import java.util.List;
import java.util.stream.Stream;

public class RessourceCommand implements TabExecutor {
    Mmorpg plugin;

    public RessourceCommand(Mmorpg plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player player))
            return false;
        if(strings[0].equalsIgnoreCase("add")) {
            Block block = player.getTargetBlock(null, 10);
            int respawnTime = Integer.parseInt(strings[1]);
            JobType jobType = JobType.valueOf(strings[2]);
            int jobLevel = Integer.parseInt(strings[3]);
            ToolType toolType = ToolType.valueOf(strings[4]);
            int toolLevel = Integer.parseInt(strings[5]);
            int jobExp = Integer.parseInt(strings[6]);
            int amount = Integer.parseInt(strings[8]);
            ItemStack itemStack = ItemList.valueOf(strings[7]).generateItemStack(amount);
            Ressource ressource = new Ressource(block.getLocation(), block.getType(), respawnTime* 1000L, jobType, jobLevel, toolType, toolLevel, jobExp, itemStack);
            plugin.getRessourceManager().addRessource(ressource);
            Utils.sendInfoMessage(player, "Ressource added");
        } else if (strings[0].equalsIgnoreCase("remove")) {
            Location location = player.getTargetBlock(null, 10).getLocation();
            Ressource ressource = plugin.getRessourceManager().getRessources().get(location);
            plugin.getRessourceManager().removeRessource(ressource);
            Utils.sendInfoMessage(player, "Ressource removed");
        } else if(strings[0].equalsIgnoreCase("list")) {
            Utils.sendInfoMessage(player, "Ressources: ");
            plugin.getRessourceManager().getRessources().forEach((location, ressource) -> {
                Utils.sendInfoMessage(player, "Ressource at " + location.toString());
            });
        }

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> list = List.of("add", "list");
        if(strings.length == 1) {
            return list;
        } else if(strings.length >= 2) {
            if(strings[0].equalsIgnoreCase("add")) {
                if(strings.length == 2)
                    return List.of("respawnTime");
                else if(strings.length == 3)
                    return Stream.of(JobType.values()).map(Enum::name).filter(name -> name.startsWith(strings[2].toUpperCase())).toList();
                else if(strings.length == 4)
                    return List.of("jobLevel");
                else if(strings.length == 5)
                    return Stream.of(ToolType.values()).map(Enum::name).filter(name -> name.startsWith(strings[4].toUpperCase())).toList();
                else if(strings.length == 6)
                    return List.of("toolLevel");
                else if(strings.length == 7)
                    return List.of("jobExp");
                else if(strings.length == 8)
                    return Stream.of(ItemList.values()).map(Enum::name).filter(name -> name.startsWith(strings[7].toUpperCase())).toList();
                else if(strings.length == 9)
                    return List.of("amount");
            }
        }
        return null;
    }
}
