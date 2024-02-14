package orion.mmorpg.players.parties;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Utils;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class Party {
    final UUID uuid;
    final LinkedHashSet<OrionPlayer> players;
    final LinkedHashSet<String> invites;

    public Party(UUID uuid) {
        this.uuid = uuid;
        this.players = new LinkedHashSet<>();
        this.invites = new LinkedHashSet<>();
    }

    public void invite(Player inviter, String player) {
        invites.add(player);
        Utils.sendInfoMessage(Bukkit.getPlayer(player), "Vous avez été à rejoindre la party");
    }

    public void addMember(Player player, OrionPlayer data) {
        players.add(data);
        data.setParty(this);
    }

    public void removeMember(OrionPlayer player) {
        players.remove(player);
        player.setParty(null);
        if (players.isEmpty())
            Mmorpg.INSTANCE.getPartyManager().removeParty(this);
    }

    public Set<String> getInfos() {
        Set<String> infos = new HashSet<>();
        infos.add(uuid.toString());
        StringBuilder sb = new StringBuilder();
        for (OrionPlayer p : players)
            sb.append(Bukkit.getPlayer(p.getUniqueId()).getDisplayName());
        infos.add(sb.toString());
        return infos;
    }

    public UUID getUuid() {
        return uuid;
    }

    public LinkedHashSet<OrionPlayer> getMembers() {
        return players;
    }

    public LinkedHashSet<String> getInvites() {
        return invites;
    }
}
