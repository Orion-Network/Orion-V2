package orion.mmorpg.guils;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class MemberState {
    private GuildRank guildRank;
    private ArrayList<GuildPermissions> permissions;
    LocalDateTime joiningTime;

    public MemberState(GuildRank guildRank) {
        this.guildRank = guildRank;
        this.permissions = new ArrayList<>();
        this.joiningTime = LocalDateTime.now();
    }

    public GuildRank getGuildRank() {
        return guildRank;
    }

    public void setGuildRank(GuildRank guildRank) {
        this.guildRank = guildRank;
    }

    public ArrayList<GuildPermissions> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<GuildPermissions> permissions) {
        this.permissions = permissions;
    }
}
