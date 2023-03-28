package orion.mmorpg.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.entities.samples.AbstractEntityBase;
import orion.mmorpg.managers.ChunkManager;
import orion.mmorpg.entities.Mobs;
import orion.mmorpg.entities.samples.AbstractMob;
import orion.mmorpg.entities.samples.AbstractPet;
import orion.mmorpg.utils.Utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
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
            AbstractMob mob = summonMob(commandSender, args);
            mob.setRespawn(false);
        } else if(args[0].equalsIgnoreCase("list")) {
            mmorpg.getMobManager().getMobs().forEach((uuid, mob) -> {
                commandSender.sendMessage(uuid + mob.getClass().getSimpleName());
            });
        } else if(args[0].equalsIgnoreCase("info")) {

        } else if(args[0].equalsIgnoreCase("chunks")) {
            for(ChunkManager.ChunkIdentifier chunk : mmorpg.getChunkManager().getChunkMobs().keySet()) {
                mmorpg.getChunkManager().getChunk(chunk).forEach(mob -> {
                    commandSender.sendMessage(mob.getMobClass().getSimpleName());
                });
            }
        } else if(args[0].equalsIgnoreCase("chunkMob")) {
            if(mmorpg.getChunkManager().containChunk(((Player) commandSender).getLocation().getChunk())) {
                mmorpg.getChunkManager().getChunk(((Player) commandSender).getLocation().getChunk()).forEach(mob -> {
                    Utils.sendInfoMessage((Entity) commandSender, mob.getMobClass().getSimpleName());
                });
            } else {
                Utils.sendInfoMessage((Entity) commandSender, "No mobs in this chunk");
            }
        } else if (args[0].equalsIgnoreCase("removeAll")) {
            mmorpg.getMobManager().despawnMobs();
        } else if(args[0].equalsIgnoreCase("spawnAll")) {
            mmorpg.getMobManager().loadMobs();
        }
        else if (args[0].equalsIgnoreCase("set")) {
            AbstractMob mob = summonMob(commandSender, args);
            if(mob != null) {
                mmorpg.getMongoClient().saveMob(mob);
            }
        }

        return false;
    }

    private AbstractMob summonMob(@NotNull CommandSender commandSender, @NotNull String[] args) {
        Location location = null;
        if(commandSender instanceof org.bukkit.entity.Player)
            location = ((org.bukkit.entity.Player) commandSender).getLocation().clone();
        try {
            final AbstractMob mob = (AbstractMob) Mobs.valueOf(args[1]).getMobClass().getDeclaredConstructors()[0].newInstance(UUID.randomUUID().toString(), location);
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
        return Arrays.asList("spawn", "list", "info", "spawnAll" , "removeAll", "set", "chunkMob", "chunks");
    }
}
