package orion.mmorpg.players.parties;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Utils;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class Party {
    final Scoreboard scoreboard;
    final UUID uuid;
    final LinkedHashSet<OrionPlayer> players;
    final LinkedHashSet<String> invites;

    public Party(UUID uuid) {
        this.uuid = uuid;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("party_info", "dummy", "Party  Board");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.players = new LinkedHashSet<>();
        this.invites = new LinkedHashSet<>();
    }

    public void invite(Player inviter, String player) {
        invites.add(player);
        Utils.sendInfoMessage(Bukkit.getPlayer(player), "Vous avez été à rejoindre la party");
    }

    public void addPlayer(Player player, OrionPlayer data) {
        players.add(data);
        player.setScoreboard(scoreboard);
        data.setParty(this);

        Score sc = scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(player.getName());
        sc.setScore(players.size()+1);
    }

    public boolean removePlayer(OrionPlayer player) {
        players.remove(player);
        return players.isEmpty();
    }

    public Set<String> getInfos() {
        Set<String> infos = new HashSet<>();
        infos.add(uuid.toString());
        StringBuilder sb = new StringBuilder();
        for(OrionPlayer p : players)
            sb.append(Bukkit.getPlayer(p.getUniqueId()).getDisplayName());
        infos.add(sb.toString());
        return infos;
    }

    public Score getPlayerScore(Player player) {
        for(String str : scoreboard.getEntries())
            if (str.startsWith(player.getDisplayName()))
                return scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(str);
        return null;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public UUID getUuid() {
        return uuid;
    }

    public LinkedHashSet<OrionPlayer> getPlayers() {
        return players;
    }

    public LinkedHashSet<String> getInvites() {
        return invites;
    }
}
