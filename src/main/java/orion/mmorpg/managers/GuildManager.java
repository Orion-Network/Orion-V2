package orion.mmorpg.managers;

import orion.mmorpg.Mmorpg;
import orion.mmorpg.guilds.Guild;
import orion.mmorpg.guilds.GuildRank;
import orion.mmorpg.guilds.MemberState;
import orion.mmorpg.players.OrionPlayer;

import java.util.HashMap;
import java.util.UUID;

public class GuildManager extends AbstractManager {
    HashMap<String, Guild> guildHashMap;
    public GuildManager(Mmorpg mmorpg) {
        super(mmorpg);
        this.guildHashMap = new HashMap<>();
    }

    public void createGuild(String guildName, OrionPlayer master, OrionPlayer... players) {
        if(guildHashMap.containsKey(guildName))
            return;
        HashMap<UUID, MemberState> rankHashMap = new HashMap<>();
        rankHashMap.put(master.getUniqueId(), new MemberState(GuildRank.CHEF));
        if(players.length > 0)
            rankHashMap.put(players[0].getUniqueId(), new MemberState(GuildRank.SUB_CHEF));
        for (int i = 2; i < players.length; i++) {
            rankHashMap.put(players[i].getUniqueId(), new MemberState(GuildRank.MEMBER));
        }
        Guild guild = new Guild(guildName, rankHashMap);
        guildHashMap.put(guild.getGuildName(), guild);
    }

    public void saveGuilds() {

    }

    public HashMap<String, Guild> getGuildHashMap() {
        return guildHashMap;
    }
}
