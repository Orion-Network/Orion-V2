package orion.mmorpg.commands;

import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.gui.editors.PlayerEditor;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AdminCommand implements TabExecutor {

    Mmorpg mmorpg;

    public AdminCommand(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(commandSender instanceof Player) {
            Player player = (Player)commandSender;
            OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(((Player) commandSender).getUniqueId());
            if(args[0].equalsIgnoreCase("setlevel")) {
                orionPlayer.setLevel(Integer.parseInt(args[1]));
            } else if(args[0].equalsIgnoreCase("save")) {
                mmorpg.getPlayerManager().savePlayers();
                Utils.sendInfoMessage((Entity) commandSender, "Saving players.");
            } else if (args[0].equalsIgnoreCase("debug")) {
                if(args[1].equalsIgnoreCase("bossbar")) {
                    Bukkit.getBossBars().forEachRemaining(BossBar::removeAll);
                }
            }
            else if (args[0].equalsIgnoreCase("editor")) {
                if(args[1].equalsIgnoreCase("player")) {
                    Player targetPlayer = Bukkit.getPlayer(args[2]);
                    OrionPlayer orionPlayer1 = mmorpg.getPlayerManager().getPlayer(targetPlayer.getUniqueId());
                    new PlayerEditor((Player) commandSender, mmorpg.getPlayerManager().getPlayer(((Player) commandSender).getUniqueId()), targetPlayer, orionPlayer1);
                }
            }
            else if(args[0].equalsIgnoreCase("test")) {
                //test if img exist in resources folder
                try {
                    Image image = ImageIO.read(Objects.requireNonNull(mmorpg.getResource("img/alchemy_2.png")));

                    Utils.sendInfoMessage((Entity) commandSender, "Image found.");
                } catch (IOException e) {
                    Utils.sendErrorMessage((Entity) commandSender, "Image not found.");
                    throw new RuntimeException(e);
                }
            }
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        ArrayList<String> completion = new ArrayList<>();
        if(args[0].equalsIgnoreCase("setlevel")) {
            return Arrays.asList("0", "5", "10", "15", "30");
        } else if(args[0].equalsIgnoreCase("editor")) {
            if(args[0].equalsIgnoreCase("player")) {
                return null;
            }
            return Arrays.asList("player", "item");
        }

        return Arrays.asList("setlevel", "editor", "test", "debug", "save");
    }
}
