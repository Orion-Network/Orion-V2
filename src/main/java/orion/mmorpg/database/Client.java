package orion.mmorpg.database;

import com.google.common.reflect.TypeToken;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.entities.Mobs;
import orion.mmorpg.entities.Mounts;
import orion.mmorpg.entities.Pets;
import orion.mmorpg.entities.samples.AbstractMob;
import orion.mmorpg.items.tools.ToolType;
import orion.mmorpg.players.Equipments;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.Support;
import orion.mmorpg.players.jobs.JobType;
import orion.mmorpg.players.jobs.Jobs;
import orion.mmorpg.players.races.Race;
import orion.mmorpg.utils.Infos;
import orion.mmorpg.utils.Knowledge;
import orion.mmorpg.utils.Stats;
import orion.mmorpg.world.Ressource;

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
    public MongoDatabase orionDatabase;
    public MongoCollection<Document> playerCollection;
    public MongoCollection<Document> mobCollection;
    public MongoCollection<Document> jobsCollection;
    public MongoCollection<Document> ressourceCollection;

    public Client(Mmorpg mmorpg) {
        uri = "mongodb://"+mmorpg.getConfig().getString("mongo.username")+":"+ mmorpg.getConfig().getString("mongo.password")+"@"+mmorpg.getConfig().getString("mongo.auth")+"/?authSource=admin";
        this.mongoClient = MongoClients.create(uri);
        orionDatabase = mongoClient.getDatabase("orion");
        playerCollection = orionDatabase.getCollection("players");
        mobCollection = orionDatabase.getCollection("mobs");
        jobsCollection = orionDatabase.getCollection("jobs");
        ressourceCollection = orionDatabase.getCollection("ressources");
    }

    public boolean hasPlayer(UUID uuid) {
        Document doc = playerCollection.find(eq("uuid", uuid.toString())).first();
        return (doc != null);
    }

    public void savePlayer(OrionPlayer player) {
        Document document = new Document();
        document.put("infos", Mmorpg.getOrionGson().getGSON().toJson(player.getInfos()));
        document.put("stats", Mmorpg.getOrionGson().getGSON().toJson(player.getBaseStats()));
        document.put("pointStats", Mmorpg.getOrionGson().getGSON().toJson(player.getPointStats()));
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

        Document spells = new Document();
        player.getKnownSpells().forEach((spell, knowledge) -> spells.put(spell.getName(), Mmorpg.getOrionGson().getGSON().toJson(knowledge)));
        document.put("spells", spells);
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

        if(player.getJobs() != null)
            document.put("jobs", Mmorpg.getOrionGson().getGSON().toJson(player.getJobs()));
        else
            document.put("jobs", null);

        //Enum not saved
        if(player.getTamedPets() != null)
            document.put("pets", Mmorpg.getOrionGson().getGSON().toJson(player.getTamedPets()));
        else
            document.put("pets", null);

        if(player.getTamedMounts() != null)
            document.put("mounts", Mmorpg.getOrionGson().getGSON().toJson(player.getTamedMounts()));
        else
            document.put("mounts", null);

        if (!hasPlayer(player.getUniqueId()))
            playerCollection.insertOne(document);
        else
            playerCollection.replaceOne(eq("uuid", player.getUniqueId().toString()), document);
    }

    public OrionPlayer loadPlayer(UUID uniqueId) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Document document = playerCollection.find(eq("uuid", uniqueId.toString())).first();

        Infos info = Mmorpg.getOrionGson().GSON.fromJson(document.getString("infos"), Infos.class);
        Stats stats = Mmorpg.getOrionGson().GSON.fromJson(document.getString("stats"), Stats.class);
        Stats pointStats = Mmorpg.getOrionGson().GSON.fromJson(document.getString("pointStats"), Stats.class);

        double health = document.getDouble("health");
        int statsPoint = document.getInteger("statsPoint");
        int spentStatsPoint = document.containsKey("spentStatsPoint") ? document.getInteger("spentStatsPoint") : 0;
        int level = document.getInteger("level");
        double exp = document.containsKey("exp") ? document.getDouble("exp") : 0;
        int mana = document.getInteger("mana");

        LinkedHashSet<String> portalStones = Mmorpg.getOrionGson().GSON.fromJson(document.getString("portalStones"), portalStoneType);
        Equipments equipements = Mmorpg.getOrionGson().GSON.fromJson(document.getString("equipements"), Equipments.class);

        LinkedHashMap<Class<? extends Spell>, Knowledge> spells = new LinkedHashMap<>();
        Document object = document.get("spells", Document.class);

        object.forEach((key, value) -> {
            try {
                spells.put((Class<? extends Spell>) Class.forName(key), Mmorpg.getOrionGson().GSON.fromJson(value.toString(), Knowledge.class));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

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

        Jobs jobs;
        if(document.getString("jobs") == null)
            jobs = null;
        else
            jobs = Mmorpg.getOrionGson().getGSON().fromJson(document.getString("jobs"), Jobs.class);

        Set<Pets> pets;
        if(document.getString("pets") == null)
            pets = new HashSet<>();
        else
            pets = Mmorpg.getOrionGson().getGSON().fromJson(document.getString("pets"), new TypeToken<Set<Pets>>(){}.getType());

        Set<Mounts> mounts;
        if(document.getString("mounts") == null)
            mounts = new HashSet<>();
        else
            mounts = Mmorpg.getOrionGson().getGSON().fromJson(document.getString("mounts"), new TypeToken<Set<Mounts>>(){}.getType());
        return new OrionPlayer(info, stats, pointStats, uniqueId, health, statsPoint, spentStatsPoint, level, exp, mana, portalStones, equipements, spells, selectedSpells, quiver, slotTypes, support, race, jobs, pets, mounts);
    }

    public void saveMob(AbstractMob mob) {
        Document document = new Document();

        document.put("className", mob.getMobData().getMobClass().getName());
        document.put("uuid", mob.getMobData().getUuid());
        document.put("location", Mmorpg.getOrionGson().GSON.toJson(mob.getMobData().getLocation()));

        mobCollection.insertOne(document);
    }

    public AbstractMob loadMob(String uuid) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Document doc = mobCollection.find(eq("uuid", uuid)).first();

        Location location = Mmorpg.getOrionGson().GSON.fromJson(doc.getString("location"), Location.class);
        Class<? extends AbstractMob> mobClass = (Class<? extends AbstractMob>) Class.forName(doc.getString("className"));
        String uuidMob = doc.getString("uuid");

        return mobClass.getConstructor(String.class, Location.class).newInstance(uuidMob, location);
    }

    public void saveRessource(Ressource ressource) {
        Document document = new Document();

        document.put("location", Mmorpg.getOrionGson().GSON.toJson(ressource.getLocation()));
        document.put("material", ressource.getMaterial().name());
        document.put("respawnTime", ressource.getRespawnTime());
        document.put("jobType", ressource.getJobType().name());
        document.put("jobLevel", ressource.getJobLevel());
        document.put("toolType", ressource.getToolType().name());
        document.put("toolPower", ressource.getToolPower());
        document.put("drop", Mmorpg.getOrionGson().GSON.toJson(ressource.getDrop()));

        ressourceCollection.insertOne(document);
    }

    public void saveRessources() {
        ressourceCollection.deleteMany(new Document());
        Mmorpg.INSTANCE.getRessourceManager().getRessources().forEach((location, ressource) -> saveRessource(ressource));
    }

    public HashMap<Location, Ressource> loadRessources() {
        HashMap<Location, Ressource> ressources = new HashMap<>();

        ressourceCollection.find().forEach((Consumer<? super Document>) document -> {
            Location location = Mmorpg.getOrionGson().GSON.fromJson(document.getString("location"), Location.class);
            Material material = Material.valueOf(document.getString("material"));
            long respawnTime = document.getLong("respawnTime");
            JobType jobType = JobType.valueOf(document.getString("jobType"));
            int jobLevel = document.getInteger("jobLevel");
            ToolType toolType = ToolType.valueOf(document.getString("toolType"));
            int toolPower = document.getInteger("toolPower");
            ItemStack drop = Mmorpg.getOrionGson().GSON.fromJson(document.getString("drop"), ItemStack.class);

            ressources.put(location, new Ressource(location, material, respawnTime, jobType, jobLevel, toolType, toolPower, drop));
        });

        return ressources;
    }

    public MongoCollection<Document> getMobCollection() {
        return mobCollection;
    }
}
