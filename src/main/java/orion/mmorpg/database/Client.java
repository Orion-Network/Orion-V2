package orion.mmorpg.database;

import com.google.common.reflect.TypeToken;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import fr.nexomega.orionmob.entities.Mounts;
import fr.nexomega.orionmob.entities.Pets;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.Equipments;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.OEffect;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.Support;
import orion.mmorpg.players.jobs.Jobs;
import orion.mmorpg.players.classes.Classe;
import orion.mmorpg.utils.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class Client extends SaveStrategy {
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

        if (player.getClasse() != null)
            document.put("classe", player.getClasse().toString());
        else
            document.put("classe", null);

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

        if (player.getOrionEffects() != null) {
            Document effects = new Document();
            player.getOrionEffects().forEach((event, map) -> {
                Document eventDoc = new Document();
                map.forEach((id, effect) -> {
                    eventDoc.put("id", id);
                    eventDoc.put("effectClass", effect.getClass().getName());
                    eventDoc.put("remainingTime", effect.getRemainingTime());
                });
                effects.put(event, eventDoc);
            });
            document.put("effects", effects);
            System.out.println(effects.size() + " effectss");
        } else
            document.put("effects", new LinkedHashMap<>());

        if (!hasPlayer(player.getUniqueId()))
            playerCollection.insertOne(document);
        else
            playerCollection.replaceOne(eq("uuid", player.getUniqueId().toString()), document);
    }

    public OrionPlayer loadPlayer(UUID uniqueId) {
        Document document = playerCollection.find(eq("uuid", uniqueId.toString())).first();
        OrionPlayer.OrionPlayerBuilder builder = new OrionPlayer.OrionPlayerBuilder();
        builder.withUniqueId(uniqueId);

        Infos info = Mmorpg.getOrionGson().GSON.fromJson(document.getString("infos"), Infos.class);
        builder.withInfos(info);

        Stats stats = Mmorpg.getOrionGson().GSON.fromJson(document.getString("stats"), Stats.class);
        builder.withStats(stats);

        double health = document.getDouble("health");
        builder.withHealth(health);

        int level = document.getInteger("level");
        builder.withLevel(level);

        double exp = document.containsKey("exp") ? document.getDouble("exp") : 0;
        builder.withExp(exp);

        int mana = document.getInteger("mana");
        builder.withMana(mana);

        LinkedHashSet<String> portalStones = Mmorpg.getOrionGson().GSON.fromJson(document.getString("portalStones"),
                portalStoneType);
        builder.withPortalStones(portalStones);
        Equipments equipements = Mmorpg.getOrionGson().GSON.fromJson(document.getString("equipements"),
                Equipments.class);
        builder.withEquipements(equipements);

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
        builder.withKnownSpells(spells);

        String[] selectedSpells;
        if (document.containsKey("selectedSpells"))
            selectedSpells = Mmorpg.getOrionGson().GSON.fromJson((String) document.get("selectedSpells"),
                    String[].class);
        else
            selectedSpells = new String[9];
        builder.withSelectedSpells(selectedSpells);

        ItemStack[] quiver;
        if (document.containsKey("quiver"))
            quiver = Mmorpg.getOrionGson().GSON.fromJson((String) document.get("quiver"), ItemStack[].class);
        else
            quiver = new ItemStack[9];
        builder.withQuiver(quiver);

        OrionPlayer.SlotType[] slotTypes;
        if (document.containsKey("slotTypes"))
            slotTypes = Mmorpg.getOrionGson().GSON.fromJson(document.getString("slotTypes"),
                    OrionPlayer.SlotType[].class);
        else
            slotTypes = new OrionPlayer.SlotType[] { OrionPlayer.SlotType.FREE_SLOT, OrionPlayer.SlotType.FREE_SLOT,
                    OrionPlayer.SlotType.FREE_SLOT, OrionPlayer.SlotType.FREE_SLOT, OrionPlayer.SlotType.FREE_SLOT,
                    OrionPlayer.SlotType.FREE_SLOT, OrionPlayer.SlotType.FREE_SLOT, OrionPlayer.SlotType.FREE_SLOT,
                    OrionPlayer.SlotType.USE_SLOT };
        builder.withSlotTypes(slotTypes);

        Support support;
        if (document.getString("support") == null)
            support = null;
        else {
            try {
                support = (Support) Class.forName(document.getString("support")).getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException
                    | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        builder.withMainSupport(support);

        Classe classe;
        if (document.getString("classe") == null)
            classe = null;
        else
            classe = Classe.valueOf(document.getString("classe"));
        builder.withClasse(classe);

        Jobs jobs;
        if (document.getString("jobs") == null)
            jobs = null;
        else
            jobs = Mmorpg.getOrionGson().getGSON().fromJson(document.getString("jobs"), Jobs.class);
        builder.withJobs(jobs);

        Set<Pets> pets;
        if (document.getString("pets") == null)
            pets = new HashSet<>();
        else
            pets = Mmorpg.getOrionGson().getGSON().fromJson(document.getString("pets"), new TypeToken<Set<Pets>>() {
            }.getType());
        builder.withTamedPets(pets);

        Set<Mounts> mounts;
        if (document.getString("mounts") == null)
            mounts = new HashSet<>();
        else
            mounts = Mmorpg.getOrionGson().getGSON().fromJson(document.getString("mounts"),
                    new TypeToken<Set<Mounts>>() {
                    }.getType());
        builder.withTamedMounts(mounts);

        Currencies currencies;
        if (document.getString("currencies") == null)
            currencies = new Currencies();
        else
            currencies = Mmorpg.getOrionGson().getGSON().fromJson(document.getString("currencies"), Currencies.class);
        builder.withCurrencies(currencies);

        LinkedHashMap<String, LinkedHashMap<String, OEffect>> orionEffects = new LinkedHashMap<>();
        if (document.containsKey("effects")) {
            Document effects = document.get("effects", Document.class);
            System.out.println(effects.size() + " effects");
            effects.forEach((event, map) -> {
                Document mapDoc = (Document) map;
                LinkedHashMap<String, OEffect> eventMap = new LinkedHashMap<>();
                try {
                    if (!mapDoc.containsKey("effectClass"))
                        return;
                    OEffect oEffect = (OEffect) Class.forName(mapDoc.getString("effectClass"))
                            .getConstructor(long.class).newInstance(mapDoc.getLong("remainingTime"));
                    eventMap.put(mapDoc.getString("id"), oEffect);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                orionEffects.put(event, eventMap);
            });
        }
        builder.withOrionEffects(orionEffects);

        return builder.build();
    }

}
