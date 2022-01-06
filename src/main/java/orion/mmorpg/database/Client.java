package orion.mmorpg.database;

import com.google.common.reflect.TypeToken;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.PortalStone;
import orion.mmorpg.players.abilities.Support;
import orion.mmorpg.players.races.Race;
import orion.mmorpg.utils.BukkitSerialization;
import orion.mmorpg.utils.Infos;
import orion.mmorpg.utils.OrionGson;
import orion.mmorpg.utils.Stats;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class Client {
    MongoClientURI uri = new MongoClientURI("mongodb://admin:azerty123@mongo/?authSource=orion");
    public MongoClient mongoClient = new MongoClient(uri);
    public MongoDatabase orionDatabase = mongoClient.getDatabase("orion");

    public MongoCollection<Document> playerCollection = orionDatabase.getCollection("players");

    static Type portalStoneType = new TypeToken<LinkedHashSet<PortalStone>>(){}.getType();
    static Type spellSetType = new TypeToken<Set<String>>(){}.getType();

    public boolean hasPlayer(UUID uuid) {
        Document doc = playerCollection.find(eq("uuid", uuid.toString())).first();
        return (doc != null);
    }

    public void savePlayer(OrionPlayer player) {
        Document document = new Document();
        document.put("infos", Mmorpg.getOrionGson().getGSON().toJson(player.getInfos()));
        document.put("stats", Mmorpg.getOrionGson().getGSON().toJson(player.getStats()));
        document.put("uuid", player.getUniqueId().toString());

        document.put("health", player.getHealth());
        document.put("statsPoint", player.getStatsPoints());
        document.put("level", player.getLevel());
        document.put("mana", player.getMana());

        document.put("portalStones", Mmorpg.getOrionGson().getGSON().toJson(player.getPortalStones()));
        //document.put("equipements", Mmorpg.getOrionGson().getGSON().toJson(BukkitSerialization.itemStackArrayToBase64(player.getEquipements())));
        document.put("equipements", Mmorpg.getOrionGson().getGSON().toJson(player.getEquipements()));

        document.put("spells", Mmorpg.getOrionGson().getGSON().toJson(player.getSpells().keySet()));

        if(player.getSupport() != null)
            document.put("support", player.getSupport().getClass().getName());
        else
            document.put("support", null);

        if(player.getRace() != null)
            document.put("race", player.getRace().toString());
        else
            document.put("race", null);

        //playerCollection.insertOne(document);

        if(!hasPlayer(player.getUniqueId()))
            playerCollection.insertOne(document);
        else
            playerCollection.replaceOne(eq("uuid", player.getUniqueId().toString()), document);
    }

    public OrionPlayer loadPlayer(UUID uniqueId) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Document document = playerCollection.find(eq("uuid", uniqueId.toString())).first();

        Infos info = Mmorpg.getOrionGson().GSON.fromJson(document.getString("infos"), Infos.class);
        Stats stats = Mmorpg.getOrionGson().GSON.fromJson(document.getString("stats"), Stats.class);

        double health = document.getDouble("health");
        int statsPoint = document.getInteger("statsPoint");
        int level = document.getInteger("level");
        int mana = document.getInteger("mana");

        LinkedHashSet<String> portalStones = Mmorpg.getOrionGson().GSON.fromJson(document.getString("portalStones"), portalStoneType);
        //ItemStack[] equipements = BukkitSerialization.itemStackArrayFromBase64(document.getString("equipements"));
        ItemStack[] equipements = Mmorpg.getOrionGson().GSON.fromJson(document.getString("equipements"), ItemStack[].class);

        Set<String> spells = Mmorpg.getOrionGson().GSON.fromJson(document.getString("spells"), spellSetType);

        Support support;
        if(document.getString("support") == null)
            support = null;
        else
             support = (Support) Class.forName(document.getString("support")).getConstructor().newInstance();

        Race race;
        if(document.getString("race") == null)
            race = null;
        else
            race = Race.valueOf(document.getString("race"));

        return new OrionPlayer(info, stats, uniqueId, health, statsPoint, level, mana, portalStones, equipements, spells, support, race);
    }
}
