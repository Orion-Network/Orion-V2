package orion.mmorpg.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.mobs.Mobs;
import orion.mmorpg.mobs.samples.SampleMob;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MobCommand implements TabExecutor {
    Mmorpg mmorpg;

    public MobCommand(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args[0].equalsIgnoreCase("spawn")) {
            SampleMob mob = summonMob(commandSender, args);
            mob.setRespawn(false);
        } else if(args[0].equalsIgnoreCase("list")) {

        } else if(args[0].equalsIgnoreCase("info")) {

        } else if (args[0].equalsIgnoreCase("remove")) {

        } else if (args[0].equalsIgnoreCase("set")) {
            SampleMob mob = summonMob(commandSender, args);
            if(mob != null) {
                mmorpg.getMongoClient().saveMob(mob);
            }
        }

        return false;
    }

    private SampleMob summonMob(@NotNull CommandSender commandSender, @NotNull String[] args) {
        Location location = null;
        if(commandSender instanceof org.bukkit.entity.Player)
            location = ((org.bukkit.entity.Player) commandSender).getLocation().clone();

        try {
            final SampleMob mob = (SampleMob) Mobs.valueOf(args[1]).getMobClass().getDeclaredConstructors()[0].newInstance(UUID.randomUUID().toString(), location);
            mob.spawn();
            return mob;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> tab = new ArrayList<>();
        if(strings.length == 2) {
            for (Mobs mob : Mobs.values()) {
                tab.add(mob.name());
            }
            return tab;
        } else if(strings.length == 3) {

        }
        return tab;
    }
}
