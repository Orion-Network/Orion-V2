package orion.mmorpg.managers;

import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.parties.Party;

import java.util.LinkedHashMap;
import java.util.UUID;

public class PartyManager {
    Mmorpg mmorpg;
    LinkedHashMap<UUID, Party> partyMap;

    public PartyManager(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
        this.partyMap = new LinkedHashMap<>();
    }

    public Party createParty() {
        Party p = new Party(UUID.randomUUID());
        partyMap.put(p.getUuid(), p);
        return p;
    }

    public Party getParty(UUID uuid) {
        if (partyMap.containsKey(uuid))
            return partyMap.get(uuid);
        return null;
    }

    public void removeParty(Party party) {
        partyMap.remove(party);
    }
}
