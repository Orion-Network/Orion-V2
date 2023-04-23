package orion.mmorpg.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.EnumUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.entities.Pets;
import orion.mmorpg.entities.samples.AbstractPet;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PetCommand implements TabExecutor {
    Mmorpg mmorpg;

    public PetCommand(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length == 0) {
            commandSender.sendMessage("Usage: /pet <spawn/list/tame>");
            return false;
        }

        if(args[0].equalsIgnoreCase("spawn")) {
            if(args.length == 2) {

                OrionPlayer player = mmorpg.getPlayerManager().getPlayer(((org.bukkit.entity.Player) commandSender).getUniqueId());
                if(!EnumUtils.isValidEnum(Pets.class, args[1]))
                    return false;

                if (!player.getTamedPets().contains(Pets.valueOf(args[1]))) {
                    commandSender.sendMessage("You don't have this pet");
                    return false;
                }
                Pets.valueOf(args[1]).spawn((Player) commandSender);
            }   else {
                commandSender.sendMessage("Usage: /pet spawn <pet>");
                return false;
            }
        } else if(args[0].equalsIgnoreCase("list")) {
            commandSender.sendMessage("List of pets:");
            for(Pets pet : Pets.values())
                commandSender.sendMessage(pet.name());
        } else if(args[0].equalsIgnoreCase("tame")) {
            if(!(commandSender instanceof org.bukkit.entity.Player))
                return false;
            if(args.length == 2) {
                OrionPlayer player = mmorpg.getPlayerManager().getPlayer(((org.bukkit.entity.Player) commandSender).getUniqueId());
                if(!EnumUtils.isValidEnum(Pets.class, args[1]))
                    return false;
                if(player.getTamedPets().contains(Pets.valueOf(args[1])))
                    commandSender.sendMessage("You already have this pet");
                else {
                    player.getTamedPets().add(Pets.valueOf(args[1]));
                    commandSender.sendMessage("You have successfully tamed " + args[1]);
                }
            } else {
                commandSender.sendMessage("Usage: /pet tame <pet>");
                return false;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length > 1) {
            if(args[0].equalsIgnoreCase("spawn"))
                return Utils.enumAutocomplete(args[1], Pets.class);
            else if(args[0].equalsIgnoreCase("tame"))
                return Utils.enumAutocomplete(args[1], Pets.class);
        }
        return List.of("spawn", "list", "tame");
    }
}
