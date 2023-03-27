package orion.mmorpg.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.entities.Pets;
import orion.mmorpg.entities.samples.AbstractPet;
import orion.mmorpg.players.OrionPlayer;

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
        if(args[0].equalsIgnoreCase("spawn")) {
            summonPet(commandSender, args);
        } else if(args[0].equalsIgnoreCase("list")) {
            commandSender.sendMessage("List of pets:");
            for(Pets pet : Pets.values())
                commandSender.sendMessage(pet.name());
        } else if(args[0].equalsIgnoreCase("tame")) {
            if(!(commandSender instanceof org.bukkit.entity.Player))
                return false;
            if(args.length == 1) {
                commandSender.sendMessage("Usage: /pet tame <pet>");
                return false;
            } else if(args.length == 2) {
                OrionPlayer player = mmorpg.getPlayerManager().getPlayer(((org.bukkit.entity.Player) commandSender).getUniqueId());
                if(player.getTamedPets().contains(Pets.valueOf(args[1])))
                    commandSender.sendMessage("You already have this pet");
                else {
                    player.getTamedPets().add(Pets.valueOf(args[1]));
                    commandSender.sendMessage("You have successfully tamed " + args[1]);
                }
            }
        }
        return false;
    }

    private void summonPet(CommandSender commandSender, String[] args) {
        if(!(commandSender instanceof org.bukkit.entity.Player))
            return;

        Location location = ((org.bukkit.entity.Player) commandSender).getLocation().clone();

        try {
            final AbstractPet mob = (AbstractPet) Pets.valueOf(args[1]).getPetClass().getDeclaredConstructors()[0].newInstance(UUID.randomUUID().toString(), location, commandSender);
            mob.spawn();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 1)
            return List.of("spawn");
        if(strings.length == 2) {
            List<String> list = new ArrayList<>();
            for(Pets pet : Pets.values())
                list.add(pet.name());
            return list;
        }
        return null;
    }
}
