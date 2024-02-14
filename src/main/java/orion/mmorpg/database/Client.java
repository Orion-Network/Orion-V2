package orion.mmorpg.database;

import com.google.common.reflect.TypeToken;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import fr.nexomega.orionmob.entities.Mounts;
import fr.nexomega.orionmob.entities.Pets;
import fr.nexomega.orionmob.entities.samples.AbstractMob;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.tools.ToolType;
import orion.mmorpg.players.Equipments;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.Support;
import orion.mmorpg.players.jobs.Jobs;
import orion.mmorpg.players.races.Race;
import orion.mmorpg.utils.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;

public class Client {
    static Type portalStoneType = new TypeToken<LinkedHashSet<String>>() {
    }.getType();
    static Type spellSetType = new TypeToken<LinkedHashMap<Class<? extends Spell>, Knowledge>>() {
    }.getType();
    static Type quiverMapType = new TypeToken<LinkedHashMap<ItemStack, Integer>>() {
    }.getType();
    String uri;
    public MongoClient mongoClient;
    @Getter
    public MongoDatabase orionDatabase;
    public MongoCollection<Document> playerCollection;
    public MongoCollection<Document> jobsCollection;

    public Client(Mmorpg mmorpg) {
        uri = "mongodb://" + mmorpg.getConfig().getString("mongo.username") + ":"
                + mmorpg.getConfig().getString("mongo.password") + "@" + mmorpg.getConfig().getString("mongo.auth")
                + "/?authSource=admin";
        this.mongoClient = MongoClients.create(uri);
        orionDatabase = mongoClient.getDatabase("orion");
        playerCollection = orionDatabase.getCollection("players");
        jobsCollection = orionDatabase.getCollection("jobs");
    }

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
        document.put("level", player.getLevel());
        document.put("exp", player.getExp());
        document.put("mana", player.getMana());

        document.put("portalStones", Mmorpg.getOrionGson().getGSON().toJson(player.getPortalStones()));
        document.put("equipements", Mmorpg.getOrionGson().getGSON().toJson(player.getEquipements()));

        Document spells = new Document();
        player.getKnownSpells().forEach(
                (spell, knowledge) -> spells.put(spell.getName(), Mmorpg.getOrionGson().getGSON().toJson(knowledge)));
        document.put("spells", spells);
        document.put("selectedSpells", Mmorpg.getOrionGson().getGSON().toJson(player.getSelectedSpells()));
        document.put("quiver", Mmorpg.getOrionGson().getGSON().toJson(player.getQuiver()));
        document.put("slotTypes", Mmorpg.getOrionGson().getGSON().toJson(player.getSlotTypes()));

        if (player.getMainSupport() != null)
            document.put("support", player.getMainSupport().getClass().getName());
        else
            document.put("support", null);

        if (player.getRace() != null)
            document.put("race", player.getRace().toString());
        else
            document.put("race", null);

        if (player.getJobs() != null)
            document.put("jobs", Mmorpg.getOrionGson().getGSON().toJson(player.getJobs()));
        else
            document.put("jobs", null);

        // Enum not saved
        if (player.getTamedPets() != null)
            document.put("pets", Mmorpg.getOrionGson().getGSON().toJson(player.getTamedPets()));
        else
            document.put("pets", null);

        if (player.getTamedMounts() != null)
            document.put("mounts", Mmorpg.getOrionGson().getGSON().toJson(player.getTamedMounts()));
        else
            document.put("mounts", null);

        if (player.getCurrencies() != null)
            document.put("currencies", Mmorpg.getOrionGson().getGSON().toJson(player.getCurrencies()));
        else
            document.put("currencies", new Currencies());

        if (!hasPlayer(player.getUniqueId()))
            playerCollection.insertOne(document);
        else
            playerCollection.replaceOne(eq("uuid", player.getUniqueId().toString()), document);
    }

    public OrionPlayer loadPlayer(UUID uniqueId) throws IOException, ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        Document document = playerCollection.find(eq("uuid", uniqueId.toString())).first();

        Infos info = Mmorpg.getOrionGson().GSON.fromJson(document.getString("infos"), Infos.class);
        Stats stats = Mmorpg.getOrionGson().GSON.fromJson(document.getString("stats"), Stats.class);

        double health = document.getDouble("health");
        int level = document.getInteger("level");
        double exp = document.containsKey("exp") ? document.getDouble("exp") : 0;
        int mana = document.getInteger("mana");

        LinkedHashSet<String> portalStones = Mmorpg.getOrionGson().GSON.fromJson(document.getString("portalStones"),
                portalStoneType);
        Equipments equipements = Mmorpg.getOrionGson().GSON.fromJson(document.getString("equipements"),
                Equipments.class);

        LinkedHashMap<Class<? extends Spell>, Knowledge> spells = new LinkedHashMap<>();
        Document object = document.get("spells", Document.class);

        object.forEach((key, value) -> {
            try {
                spells.put((Class<? extends Spell>) Class.forName(key),
                        Mmorpg.getOrionGson().GSON.fromJson(value.toString(), Knowledge.class));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        String[] selectedSpells;
        if (document.containsKey("selectedSpells"))
            selectedSpells = Mmorpg.getOrionGson().GSON.fromJson((String) document.get("selectedSpells"),
                    String[].class);
        else
            selectedSpells = new String[9];
        ItemStack[] quiver;
        if (document.containsKey("quiver"))
            quiver = Mmorpg.getOrionGson().GSON.fromJson((String) document.get("quiver"), ItemStack[].class);
        else
            quiver = new ItemStack[9];
        OrionPlayer.SlotType[] slotTypes;
        if (document.containsKey("slotTypes"))
            slotTypes = Mmorpg.getOrionGson().GSON.fromJson(document.getString("slotTypes"),
                    OrionPlayer.SlotType[].class);
        else
            slotTypes = new OrionPlayer.SlotType[] { OrionPlayer.SlotType.FREE_SLOT, OrionPlayer.SlotType.FREE_SLOT,
                    OrionPlayer.SlotType.FREE_SLOT, OrionPlayer.SlotType.FREE_SLOT, OrionPlayer.SlotType.FREE_SLOT,
                    OrionPlayer.SlotType.FREE_SLOT, OrionPlayer.SlotType.FREE_SLOT, OrionPlayer.SlotType.FREE_SLOT,
                    OrionPlayer.SlotType.USE_SLOT };
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

        Jobs jobs;
        if (document.getString("jobs") == null)
            jobs = null;
        else
            jobs = Mmorpg.getOrionGson().getGSON().fromJson(document.getString("jobs"), Jobs.class);

        Set<Pets> pets;
        if (document.getString("pets") == null)
            pets = new HashSet<>();
        else
            pets = Mmorpg.getOrionGson().getGSON().fromJson(document.getString("pets"), new TypeToken<Set<Pets>>() {
            }.getType());

        Set<Mounts> mounts;
        if (document.getString("mounts") == null)
            mounts = new HashSet<>();
        else
            mounts = Mmorpg.getOrionGson().getGSON().fromJson(document.getString("mounts"),
                    new TypeToken<Set<Mounts>>() {
                    }.getType());

        Currencies currencies;
        if (document.getString("currencies") == null)
            currencies = new Currencies();
        else
            currencies = Mmorpg.getOrionGson().getGSON().fromJson(document.getString("currencies"), Currencies.class);
        return new OrionPlayer(info, stats, uniqueId, health, level, exp, mana, portalStones, equipements, spells,
                selectedSpells, quiver, slotTypes, support, race, jobs, pets, mounts, currencies);
    }

}
