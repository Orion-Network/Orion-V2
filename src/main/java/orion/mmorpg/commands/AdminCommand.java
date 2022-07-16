package orion.mmorpg.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import orion.mmorpg.config.AssociationConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.gui.editors.PlayerEditor;
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
            //new AdminGui((Player) commandSender, mmorpg.getPlayerManager().getPlayer(((Player) commandSender).getUniqueId())).show((HumanEntity) commandSender);
            OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(((Player) commandSender).getUniqueId());
            if(args[0].equalsIgnoreCase("setlevel")) {
                orionPlayer.setLevel(Integer.parseInt(args[1]));
            } else if(args[0].equalsIgnoreCase("test")) {
                System.out.println(AssociationConfig.getFileConfiguration().getInt("item.gui/accept_button_placeholder"));
            } else if(args[0].equalsIgnoreCase("save")) {
                mmorpg.getPlayerManager().savePlayers();
                Utils.sendInfoMessage((Entity) commandSender, "Saving players.");
            } else if (args[0].equalsIgnoreCase("debug")) {
                if(args[1].equalsIgnoreCase("bossbar")) {
                    Bukkit.getBossBars().forEachRemaining(bossBar -> bossBar.removeAll());
                }
            }
            else if (args[0].equalsIgnoreCase("editor")) {
                if(args[1].equalsIgnoreCase("player")) {
                    Player player = Bukkit.getPlayer(args[2]);
                    OrionPlayer orionPlayer1 = mmorpg.getPlayerManager().getPlayer(player.getUniqueId());
                    new PlayerEditor((Player) commandSender, mmorpg.getPlayerManager().getPlayer(((Player) commandSender).getUniqueId()), player, orionPlayer1);
                }
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
