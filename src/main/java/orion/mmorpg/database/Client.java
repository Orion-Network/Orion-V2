package orion.mmorpg.database;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import orion.mmorpg.players.OrionPlayer;
import static orion.mmorpg.utils.OrionGson.GSON;

public class Client {
    public MongoClient mongoClient = new MongoClient("localhost", 27017);
    public MongoDatabase orionDatabase = mongoClient.getDatabase("orion");

    public MongoCollection<Document> playerCollection = orionDatabase.getCollection("players");

    public void savePlayer(OrionPlayer player) {
        Document document = new Document();
        document.put("infos", GSON.toJson(player.getInfos()));
        document.put("stats", GSON.toJson(player.getStats()));
        document.put("uuid", player.getUniqueId().toString());

        document.put("health", player.getHealth());
        document.put("statsPoint", player.getStatsPoints());
        document.put("level", player.getLevel());
        document.put("mana", player.getMana());

        document.put("portalStones", GSON.toJson(player.getPortalStones()));
        document.put("equipements", GSON.toJson(player.getEquipements()));

        document.put("spells", GSON.toJson(player.getSpells().keySet()));

        document.put("support", player.getSupport().getClass());
        document.put("race", player.getRace().getClass());

        playerCollection.insertOne(document);

    }
}
