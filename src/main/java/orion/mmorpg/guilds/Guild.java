package orion.mmorpg.guilds;

import orion.mmorpg.players.OrionPlayer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class Guild {
    String guildName;
    LocalDateTime creationDate;
    HashMap<UUID, MemberState> guildMemberMap;
    HashMap<GuildRank, ArrayList<GuildPermissions>> guildMapPermissions;

    /**
     * Create a new guild with name and members
     * @param guildName Define Guild Name
     * @param guildMemberMap Map of Original Members
     */
    public Guild(String guildName, HashMap<UUID, MemberState> guildMemberMap) {
        this.guildName = guildName;
        this.creationDate = LocalDateTime.now();
        this.guildMemberMap = guildMemberMap;
        this.guildMapPermissions = new HashMap<>();
        this.guildMapPermissions.put(GuildRank.CHEF, new ArrayList<>(Arrays.asList(
                GuildPermissions.TELEPORT_TO_GUILD,
                GuildPermissions.ACCEPT_MEMBERS,
                GuildPermissions.INVITE_MEMBERS,
                GuildPermissions.EXCLUDE_MEMBERS,
                GuildPermissions.BAN_MEMBERS
        )));
    }

    /**
     * Constructor used to load guild from Json
     * @param guildName Guild Name From Json
     * @param creationDate Creation Date from Json
     * @param guildMemberMap Guild Map From Json
     * @param guildMapPermissions Guild Permission Form Json
     */

    public Guild(String guildName, LocalDateTime creationDate, HashMap<UUID, MemberState> guildMemberMap, HashMap<GuildRank, ArrayList<GuildPermissions>> guildMapPermissions) {
        this.guildName = guildName;
        this.creationDate = creationDate;
        this.guildMemberMap = guildMemberMap;
        this.guildMapPermissions = guildMapPermissions;
    }

    public boolean addMemberPermission(OrionPlayer player, GuildPermissions permission) {
        MemberState state = guildMemberMap.get(player);
        if(state.getPermissions().contains(permission))
            return false;
        state.getPermissions().add(permission);
        return true;
    }

    public boolean addRankPermission(GuildRank rank, GuildPermissions permission) {
        if(guildMapPermissions.get(rank).contains(permission))
            return false;
        guildMapPermissions.get(rank).add(permission);
        return true;
    }

    public boolean hasPermission(OrionPlayer player, GuildPermissions permission) {
        MemberState state = guildMemberMap.get(player);
        return (state.getPermissions().contains(permission) || guildMapPermissions.get(state.getGuildRank()).contains(permission));
    }

    public String getGuildName() {
        return guildName;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public HashMap<UUID, MemberState> getGuildMemberMap() {
        return guildMemberMap;
    }

    public HashMap<GuildRank, ArrayList<GuildPermissions>> getGuildMapPermissions() {
        return guildMapPermissions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Guild Name: " + guildName + "\n");
        sb.append("Creation Date : " + creationDate + "\n");
        sb.append("Members : \n");
        guildMemberMap.forEach((uuid, state) -> {
            sb.append(uuid.toString() + "\n");
            sb.append("Rank : " + state.getGuildRank() + "\n");
            sb.append("Permissions : " + state.getPermissions() + "\n");
        });
        sb.append("Permissions : \n");
        guildMapPermissions.forEach((rank, perm) -> {
            sb.append("Rank : " + rank + "\n");
            sb.append("Permissions : " + perm);
        });
        return sb.toString();
    }
}
