package orion.mmorpg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Support;
import orion.mmorpg.players.abilities.SupportList;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class SupportCommand implements TabExecutor {
    Mmorpg mmorpg;

    public SupportCommand(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            OrionPlayer player1 = mmorpg.getPlayerManager().getPlayer(player.getUniqueId());
            if(SupportList.valueOf(args[0].toUpperCase()) != null) {
                try {
                    player1.setSupport((Support) SupportList.valueOf(args[0].toUpperCase()).getSupport().getDeclaredConstructors()[0].newInstance());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> tab = new ArrayList<>();
        for(SupportList supportList : SupportList.values()) {
            if(supportList.getSupport().getSimpleName().startsWith(strings[0].toUpperCase())) {
                tab.add(supportList.getSupport().getSimpleName());
            }
        }
        return tab;
    }
}
