package orion.mmorpg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Utils;

import java.util.Arrays;
import java.util.List;

public class PartyCommand implements TabExecutor {

    Mmorpg mmorpg;

    public PartyCommand(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(sender instanceof Player)) return false;

        OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(((Player) sender).getUniqueId());
        if(args[0].equalsIgnoreCase("create")) {
            if(orionPlayer.getParty() == null)
                mmorpg.getPartyManager().createParty().addPlayer((Player) sender, orionPlayer);
            else
                Utils.sendInfoMessage((Player) sender, "Vous devez quitter votre party avant d'en créer une.");
        } else if(args[0].equalsIgnoreCase("invite")) {
            if(orionPlayer.getParty() != null)
                if(Bukkit.getPlayer(args[1]) != null)
                    orionPlayer.getParty().invite((Player) sender, args[1]);
                else
                    Utils.sendInfoMessage((Player) sender, "Ce joueur n'existe pas.");
            else
                Utils.sendInfoMessage((Player) sender, "Vous êtes dans aucune party.");
        } else if (args[0].equalsIgnoreCase("leave")){
            if(orionPlayer.getParty() != null) {
                if (orionPlayer.getParty().removePlayer(orionPlayer))
                    mmorpg.getPartyManager().removeParty(orionPlayer.getParty());
                orionPlayer.setParty(null);
                ((Player) sender).setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
                Utils.sendInfoMessage((Player) sender, "Vous avez quitté la party");
            }
        } else if(args[0].equalsIgnoreCase("join")) {
            if(Bukkit.getPlayer(args[0]) != null) {
                OrionPlayer targetPlayer = mmorpg.getPlayerManager().getPlayer(Bukkit.getPlayer(args[0]).getUniqueId());
                if(targetPlayer.getParty() != null)
                    if(targetPlayer.getParty().getInvites().contains((Player) sender))
                        targetPlayer.getParty().addPlayer((Player) sender, mmorpg.getPlayerManager().getPlayer(((Player) sender).getUniqueId()));
                    else
                        Utils.sendInfoMessage((Player) sender, "Vous n'avez mas été invité dans cette party.");
                else
                    Utils.sendInfoMessage((Player) sender, "Ce joueur n'a pas de party");
            }
        } else if(args[0].equalsIgnoreCase("infos")) {
            if(orionPlayer.getParty() != null)
                for (String info : orionPlayer.getParty().getInfos())
                    Utils.sendInfoMessage((Entity) sender, info);
            else
                Utils.sendInfoMessage((Entity) sender, "Vous n'êtes dans aucune party");
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return Arrays.asList("create", "invite", "leave", "join", "infos");
    }
}
