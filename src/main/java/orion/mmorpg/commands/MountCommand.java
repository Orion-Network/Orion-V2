package orion.mmorpg.commands;

import org.apache.commons.lang3.EnumUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.entities.Mobs;
import orion.mmorpg.entities.Mounts;
import orion.mmorpg.entities.Pets;
import orion.mmorpg.entities.samples.AbstractMount;
import orion.mmorpg.entities.samples.AbstractPet;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MountCommand implements TabExecutor {
    Mmorpg mmorpg;

    public MountCommand(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args[0].equalsIgnoreCase("spawn")) {
            OrionPlayer player = mmorpg.getPlayerManager().getPlayer(((Player) commandSender).getUniqueId());

            if(!EnumUtils.isValidEnum(Mounts.class, args[1]))
                return false;
            if(!player.getTamedMounts().contains(Mounts.valueOf(args[1]))) {
                commandSender.sendMessage("You don't have this mount");
                return false;
            }
            Mounts.valueOf(args[1]).spawn((Player) commandSender);
        } else if(args[0].equalsIgnoreCase("list")) {
            commandSender.sendMessage("List of mounts:");
            for(Mounts mount : Mounts.values())
                commandSender.sendMessage(mount.name());
        } else if(args[0].equalsIgnoreCase("tame")) {
            if(!(commandSender instanceof Player))
                return false;
            if(args.length == 1) {
                commandSender.sendMessage("Usage: /mount tame <mount>");
                return false;
            } else if(args.length == 2) {
                OrionPlayer player = mmorpg.getPlayerManager().getPlayer(((Player) commandSender).getUniqueId());

                if(!EnumUtils.isValidEnum(Mounts.class, args[1]))
                    return false;
                if(player.getTamedMounts().contains(Mounts.valueOf(args[1])))
                    commandSender.sendMessage("You already have this mount");
                else {
                    player.getTamedMounts().add(Mounts.valueOf(args[1]));
                    commandSender.sendMessage("You have successfully tamed " + args[1]);
                }
            }
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length == 1)
            return List.of("spawn", "list", "tame");
        if(args.length == 2) {
            if(args[0].equalsIgnoreCase("spawn")) {
                return Utils.enumAutocomplete(args[1], Mounts.class);
            } else if(args[0].equalsIgnoreCase("tame")) {
                return Utils.enumAutocomplete(args[1], Mounts.class);
            }
        }
        return null;
    }
}
