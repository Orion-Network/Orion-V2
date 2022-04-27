package orion.mmorpg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Utils;

import java.util.List;

public class AdminCommand implements TabExecutor {

    Mmorpg mmorpg;

    public AdminCommand(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(args[0].equalsIgnoreCase("data")) {
                if(player.getInventory().getItemInMainHand() != null) {
                    ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
                    meta.setCustomModelData(Integer.valueOf(args[1]));
                    player.getInventory().getItemInMainHand().setItemMeta(meta);
                    Utils.sendInfoMessage(player, "&aSuccessfully set custom model data to " + args[1]);
                }
            } else if(args[0].equalsIgnoreCase("level")) {
                OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(player.getUniqueId());
                orionPlayer.setLevel(Integer.valueOf(args[1]));
            }

        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
