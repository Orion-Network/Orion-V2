package orion.mmorpg.commands;

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
import orion.mmorpg.entities.samples.AbstractMount;
import orion.mmorpg.entities.samples.AbstractPet;

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
            AbstractMount mob = summonMount(commandSender, args);
            if(mob == null)
                return false;
            mob.spawn();
        }
        return false;
    }
    private AbstractMount summonMount(CommandSender commandSender, String[] args) {
        if(!(commandSender instanceof org.bukkit.entity.Player))
            return null;

        Location location = ((org.bukkit.entity.Player) commandSender).getLocation().clone();

        try {
            final AbstractMount mob = (AbstractMount) Mounts.valueOf(args[1]).getMountClass().getDeclaredConstructors()[0].newInstance(UUID.randomUUID().toString(), location, commandSender);
            return mob;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 1)
            return List.of("spawn");
        if(strings.length == 2) {
            List<String> list = new ArrayList<>();
            for(Mounts mount : Mounts.values())
                list.add(mount.name());
            return list;
        }
        return null;
    }
}
