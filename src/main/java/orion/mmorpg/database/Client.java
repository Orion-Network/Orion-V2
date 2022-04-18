package orion.mmorpg.database;

import com.google.common.reflect.TypeToken;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.mobs.samples.SampleMob;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Support;
import orion.mmorpg.players.races.Race;
import orion.mmorpg.utils.Infos;
import orion.mmorpg.utils.Stats;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class Client {
    static Type portalStoneType = new TypeToken<LinkedHashSet<String>>() {
    }.getType();
    static Type spellSetType = new TypeToken<Set<String>>() {
    }.getType();
    static Type quiverMapType = new TypeToken<LinkedHashMap<ItemStack, Integer>>() {
    }.getType();
    String uri = "mongodb://admin:azerty123@mongo/?authSource=orion";
    public MongoClient mongoClient = MongoClients.create(uri);
    public MongoDatabase orionDatabase = mongoClient.getDatabase("orion");
    public MongoCollection<Document> playerCollection = orionDatabase.getCollection("players");
    public MongoCollection<Document> mobCollection = orionDatabase.getCollection("mobs");

    public boolean hasPlayer(UUID uuid) {
        Document doc = playerCollection.find(eq("uuid", uuid.toString())).first();
        return (doc != null);
    }

    public void savePlayer(OrionPlayer player) {
        Document document = new Document();
        document.put("infos", Mmorpg.getOrionGson().getGSON().toJson(player.getInfos()));
        document.put("stats", Mmorpg.getOrionGson().getGSON().toJson(player.getBaseStats()));
        document.put("uuid", player.getUniqueId().toString());

        document.put("health", player.getHealth());
        document.put("statsPoint", player.getStatsPoints());
        document.put("spentStatsPoint", player.getSpentStatsPoint());
        document.put("level", player.getLevel());
        document.put("exp", player.getExp());
        document.put("mana", player.getMana());

        document.put("portalStones", Mmorpg.getOrionGson().getGSON().toJson(player.getPortalStones()));
        //document.put("equipements", Mmorpg.getOrionGson().getGSON().toJson(BukkitSerialization.itemStackArrayToBase64(player.getEquipements())));
        document.put("equipements", Mmorpg.getOrionGson().getGSON().toJson(player.getEquipements()));

        document.put("spells", Mmorpg.getOrionGson().getGSON().toJson(player.getSpells().keySet()));
        document.put("selectedSpells", Mmorpg.getOrionGson().getGSON().toJson(player.getSelectedSpells()));
        document.put("quiver", Mmorpg.getOrionGson().getGSON().toJson(player.getQuiver()));
        document.put("slotTypes", Mmorpg.getOrionGson().getGSON().toJson(player.getSlotTypes()));

        if (player.getSupport() != null)
            document.put("support", player.getSupport().getClass().getName());
        else
            document.put("support", null);

        if (player.getRace() != null)
            document.put("race", player.getRace().toString());
        else
            document.put("race", null);

        if (!hasPlayer(player.getUniqueId()))
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
        int spentStatsPoint = document.containsKey("spentStatsPoint") ? document.getInteger("spentStatsPoint") : 0;
        int level = document.getInteger("level");
        double exp = document.containsKey("exp") ? document.getDouble("exp") : 0;
        int mana = document.getInteger("mana");

        LinkedHashSet<String> portalStones = Mmorpg.getOrionGson().GSON.fromJson(document.getString("portalStones"), portalStoneType);
        ItemStack[] equipements = Mmorpg.getOrionGson().GSON.fromJson(document.getString("equipements"), ItemStack[].class);

        Set<String> spells = Mmorpg.getOrionGson().GSON.fromJson(document.getString("spells"), spellSetType);
        String[] selectedSpells;
        if (document.containsKey("selectedSpells"))
            selectedSpells = Mmorpg.getOrionGson().GSON.fromJson((String) document.get("selectedSpells"), String[].class);
        else
            selectedSpells = new String[9];
        ItemStack[] quiver;
        if (document.containsKey("quiver"))
            quiver = Mmorpg.getOrionGson().GSON.fromJson((String) document.get("quiver"), ItemStack[].class);
        else
            quiver = new ItemStack[9];
        OrionPlayer.SlotType[] slotTypes;
        if(document.containsKey("slotTypes"))
            slotTypes = Mmorpg.getOrionGson().GSON.fromJson(document.getString("slotTypes"), OrionPlayer.SlotType[].class);
        else
            slotTypes = new OrionPlayer.SlotType[]{OrionPlayer.SlotType.FREE_SLOT,
                OrionPlayer.SlotType.FREE_SLOT,
                OrionPlayer.SlotType.FREE_SLOT,
                OrionPlayer.SlotType.FREE_SLOT,
                OrionPlayer.SlotType.FREE_SLOT,
                OrionPlayer.SlotType.FREE_SLOT,
                OrionPlayer.SlotType.FREE_SLOT,
                OrionPlayer.SlotType.FREE_SLOT,
                OrionPlayer.SlotType.USE_SLOT};
        Support support;
        if (document.getString("support") == null)
            support = null;
        else
            support = (Support) Class.forName(document.getString("support")).getConstructor().newInstance();

        Race race;
        if (document.getString("race") == null)
            race = null;
        else
            race = Race.valueOf(document.getString("race"));

        return new OrionPlayer(info, stats, uniqueId, health, statsPoint, spentStatsPoint, level, exp, mana, portalStones, equipements, spells, selectedSpells, quiver, slotTypes, support, race);
    }

    public void saveMob(SampleMob mob) {
        Document document = new Document();

        document.put("className", mob.getMobData().getMobClass().getName());
        document.put("uuid", mob.getMobData().getUuid());
        document.put("location", Mmorpg.getOrionGson().GSON.toJson(mob.getMobData().getLocation()));

        mobCollection.insertOne(document);
    }

    public SampleMob loadMob(String uuid) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Document doc = mobCollection.find(eq("uuid", uuid)).first();

        Location location = Mmorpg.getOrionGson().GSON.fromJson(doc.getString("location"), Location.class);
        Class<? extends SampleMob> mobClass = (Class<? extends SampleMob>) Class.forName(doc.getString("className"));
        String uuidMob = doc.getString("uuid");

        return mobClass.getConstructor(String.class, Location.class).newInstance(uuidMob, location);
    }

    public MongoCollection<Document> getMobCollection() {
        return mobCollection;
    }
}
