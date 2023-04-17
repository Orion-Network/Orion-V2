package orion.mmorpg.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.tools.ToolType;
import orion.mmorpg.managers.RessourceManager;
import orion.mmorpg.players.jobs.JobType;
import orion.mmorpg.utils.Utils;
import orion.mmorpg.world.Ressource;

import java.util.List;

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
            Ressource ressource = new Ressource(player.getLocation().getBlock().getLocation(), Material.IRON_ORE, 10000, JobType.MINING, 0, ToolType.PICKAXE, 0, new ItemStack(Material.IRON_INGOT, 1));
            plugin.getRessourceManager().addRessource(ressource);
            Utils.sendInfoMessage(player, "Ressource spawned");
        }

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
