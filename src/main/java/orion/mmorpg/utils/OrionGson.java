package orion.mmorpg.utils;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.sample.*;
import orion.mmorpg.players.Equipments;
import orion.mmorpg.players.jobs.AbstractJob;
import orion.mmorpg.players.jobs.JobType;
import orion.mmorpg.players.jobs.Jobs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

public class OrionGson {
    public static Gson GSON;

    public OrionGson() {
        RuntimeTypeAdapterFactory<BasicItem> basicItemRuntimeTypeAdapterFactory = RuntimeTypeAdapterFactory
                .of(BasicItem.class, "typeofclazz").registerSubtype(BasicArmorItem.class, "Armor")
                .registerSubtype(BasicArrow.class, "Arrow").registerSubtype(BasicConsumableItem.class, "Consumable")
                .registerSubtype(BasicMagicWeapon.class, "MagicWeapon")
                .registerSubtype(BasicMeleeWeapon.class, "MeleeWeapon")
                .registerSubtype(BasicRangedWeapon.class, "RangedWeapon")

        ;
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping();
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        builder.registerTypeAdapter(ItemStack.class, new ItemStackAdapter());
        builder.registerTypeAdapter(ItemStack[].class, new ItemStackArrayAdapter());
        builder.registerTypeAdapter(Location.class, new LocationAdapter());
        builder.registerTypeAdapter(Knowledge.class, new KnowledgeAdapter());
        builder.registerTypeAdapter(Equipments.class, new EquipmentsAdapter());
        builder.registerTypeAdapterFactory(basicItemRuntimeTypeAdapterFactory);
        builder.registerTypeAdapter(Jobs.class, new JobsAdapter());
        builder.registerTypeAdapter(Currencies.class, new CurrenciesAdapter());
        GSON = builder.create();
    }

    public Gson getGSON() {
        return GSON;
    }

    static final class ItemStackAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

        @Override
        public JsonElement serialize(ItemStack itemStack, Type type,
                JsonSerializationContext jsonSerializationContext) {
            if (itemStack == null)
                return JsonNull.INSTANCE;
            JsonObject jsonObject = new JsonObject();
            ItemMeta meta = Objects.requireNonNull(itemStack.getItemMeta());
            jsonObject.addProperty("internal_id",
                    meta.getPersistentDataContainer().get(Namespaces.internal_id, PersistentDataType.STRING));
            jsonObject.addProperty("amount", itemStack.getAmount());
            return jsonObject;
        }

        @Override
        public ItemStack deserialize(JsonElement jsonElement, Type type,
                JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (!jsonElement.isJsonObject())
                if (Objects.equals(jsonElement.getAsString(), "null"))
                    return null;
            JsonObject object = jsonElement.getAsJsonObject();
            return Mmorpg.INSTANCE.getItemManager().createItemstack(object.get("internal_id").getAsString(),
                    object.get("amount").getAsInt());
        }
    }

    static final class ItemStackArrayAdapter implements JsonSerializer<ItemStack[]>, JsonDeserializer<ItemStack[]> {

        ItemStackAdapter itemStackAdapter = new ItemStackAdapter();

        @Override
        public JsonElement serialize(ItemStack[] itemStacks, Type type,
                JsonSerializationContext jsonSerializationContext) {
            JsonObject items = new JsonObject();
            for (int i = 0; i < itemStacks.length; i++) {
                if (itemStacks[i] != null)
                    items.add(String.valueOf(i), itemStackAdapter.serialize(itemStacks[i], null, null));
                else
                    items.addProperty(String.valueOf(i), "null");
            }
            return items;
        }

        @Override
        public ItemStack[] deserialize(JsonElement jsonElement, Type type,
                JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonArray = jsonElement.getAsJsonObject();
            ItemStack[] items = new ItemStack[jsonArray.size()];

            for (int i = 0; i < jsonArray.size(); i++) {
                if (jsonArray.get(String.valueOf(i)) == null) {
                    items[i] = null;
                } else {
                    ItemStack item = itemStackAdapter.deserialize(jsonArray.get(String.valueOf(i)), null, null);
                    items[i] = item;
                }
            }
            return items;
        }
    }

    static final class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime> {

        public JsonElement serialize(LocalDateTime localDateTime, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(localDateTime.toInstant(ZoneOffset.UTC).getEpochSecond()); // "yyyy-mm-dd"
        }
    }

    private final static class LocationAdapter implements JsonSerializer<Location>, JsonDeserializer<Location> {

        @Override
        public JsonElement serialize(Location location, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("world", location.getWorld().getName());
            jsonObject.addProperty("x", location.getX());
            jsonObject.addProperty("y", location.getY());
            jsonObject.addProperty("z", location.getZ());
            return jsonObject;
        }

        @Override
        public Location deserialize(JsonElement jsonElement, Type type,
                JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            return new Location(Bukkit.getWorld(jsonObject.get("world").getAsString()),
                    jsonObject.get("x").getAsDouble(), jsonObject.get("y").getAsDouble(),
                    jsonObject.get("z").getAsDouble());
        }
    }

    private final static class KnowledgeAdapter implements JsonSerializer<Knowledge>, JsonDeserializer<Knowledge> {

        @Override
        public JsonElement serialize(Knowledge knowledge, Type type,
                JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("knowledge", knowledge.getKnowledge());
            return jsonObject;
        }

        @Override
        public Knowledge deserialize(JsonElement jsonElement, Type type,
                JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            return new Knowledge(jsonObject.get("knowledge").getAsInt());
        }
    }

    private final static class EquipmentsAdapter implements JsonSerializer<Equipments>, JsonDeserializer<Equipments> {
        @Override
        public JsonElement serialize(Equipments equipments, Type type,
                JsonSerializationContext jsonSerializationContext) {
            JsonObject object = new JsonObject();
            if (equipments.getEquipmentFromSlot(BasicArmorItem.ArmorType.HELMET) != null)
                object.addProperty("helmet",
                        GSON.toJson(equipments.getEquipmentFromSlot(BasicArmorItem.ArmorType.HELMET), BasicItem.class));

            if (equipments.getEquipmentFromSlot(BasicArmorItem.ArmorType.CHESTPLATE) != null)
                object.addProperty("chestplate", GSON
                        .toJson(equipments.getEquipmentFromSlot(BasicArmorItem.ArmorType.CHESTPLATE), BasicItem.class));

            if (equipments.getEquipmentFromSlot(BasicArmorItem.ArmorType.LEGGINGS) != null)
                object.addProperty("leggings", GSON
                        .toJson(equipments.getEquipmentFromSlot(BasicArmorItem.ArmorType.LEGGINGS), BasicItem.class));

            if (equipments.getEquipmentFromSlot(BasicArmorItem.ArmorType.BOOTS) != null)
                object.addProperty("boots",
                        GSON.toJson(equipments.getEquipmentFromSlot(BasicArmorItem.ArmorType.BOOTS), BasicItem.class));

            if (equipments.getEquipmentFromSlot(BasicArmorItem.ArmorType.BELT) != null)
                object.addProperty("belt",
                        GSON.toJson(equipments.getEquipmentFromSlot(BasicArmorItem.ArmorType.BELT), BasicItem.class));

            if (equipments.getEquipmentFromSlot(BasicArmorItem.ArmorType.RING) != null)
                object.addProperty("left_ring",
                        GSON.toJson(equipments.getEquipmentFromSlot(BasicArmorItem.ArmorType.RING), BasicItem.class));

            if (equipments.getEquipmentFromSlot(BasicArmorItem.ArmorType.EARRINGS) != null)
                object.addProperty("right_ring", GSON
                        .toJson(equipments.getEquipmentFromSlot(BasicArmorItem.ArmorType.EARRINGS), BasicItem.class));

            if (equipments.getEquipmentFromSlot(BasicArmorItem.ArmorType.NECKLACE) != null)
                object.addProperty("necklace", GSON
                        .toJson(equipments.getEquipmentFromSlot(BasicArmorItem.ArmorType.NECKLACE), BasicItem.class));
            return object;
        }

        @Override
        public Equipments deserialize(JsonElement jsonElement, Type type,
                JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject object = jsonElement.getAsJsonObject();
            Equipments equipments = new Equipments();
            if (object.has("helmet"))
                equipments.addEquipment(GSON.fromJson(object.get("helmet").getAsString(), (Type) BasicItem.class));
            if (object.has("chestplate"))
                equipments.addEquipment(GSON.fromJson(object.get("chestplate").getAsString(), (Type) BasicItem.class));
            if (object.has("leggings"))
                equipments.addEquipment(GSON.fromJson(object.get("leggings").getAsString(), (Type) BasicItem.class));
            if (object.has("boots"))
                equipments.addEquipment(GSON.fromJson(object.get("boots").getAsString(), (Type) BasicItem.class));
            if (object.has("belt"))
                equipments.addEquipment(GSON.fromJson(object.get("belt").getAsString(), (Type) BasicItem.class));
            if (object.has("left_ring"))
                equipments.addEquipment(GSON.fromJson(object.get("left_ring").getAsString(), (Type) BasicItem.class));
            if (object.has("right_ring"))
                equipments.addEquipment(GSON.fromJson(object.get("right_ring").getAsString(), (Type) BasicItem.class));
            return equipments;
        }
    }

    private static final class JobsAdapter implements JsonSerializer<Jobs>, JsonDeserializer<Jobs> {
        @Override
        public Jobs deserialize(JsonElement jsonElement, Type type,
                JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject object = jsonElement.getAsJsonObject();
            Jobs jobs = new Jobs();
            object.entrySet().forEach(entry -> {
                JsonObject jobObject = entry.getValue().getAsJsonObject();
                JobType jobType = JobType.valueOf(entry.getKey().toUpperCase());
                try {
                    AbstractJob job = jobType.getJobClass().getDeclaredConstructor(int.class, int.class)
                            .newInstance(jobObject.get("level").getAsInt(), jobObject.get("experience").getAsInt());
                    jobs.addJob(job);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                        | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            });
            return jobs;
        }

        @Override
        public JsonElement serialize(Jobs jobs, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject object = new JsonObject();
            jobs.getJobs().forEach((job, jobData) -> {
                JsonObject jobObject = new JsonObject();
                jobObject.addProperty("level", jobData.getLevel());
                jobObject.addProperty("experience", jobData.getExperience());
                object.add(job.name().toLowerCase(), jobObject);
            });
            return object;
        }
    }

    public static class CurrenciesAdapter implements JsonSerializer<Currencies>, JsonDeserializer<Currencies> {
        @Override
        public Currencies deserialize(JsonElement jsonElement, Type type,
                JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject object = jsonElement.getAsJsonObject();
            Currencies currencies = new Currencies();
            object.entrySet().forEach(entry -> {
                JsonObject currencyObject = entry.getValue().getAsJsonObject();
                Currencies.Currency currency = new Currencies.Currency(currencyObject.get("name").getAsString(),
                        currencyObject.get("amount").getAsLong());
                currency.setColor(currencyObject.get("color").getAsString());
                currency.setSymbol(currencyObject.get("symbol").getAsString());
                currencies.add(currency);
            });
            return currencies;
        }

        @Override
        public JsonElement serialize(Currencies currencies, Type type,
                JsonSerializationContext jsonSerializationContext) {
            JsonObject object = new JsonObject();
            currencies.getCurrencies().forEach((name, currency) -> {
                JsonObject currencyObject = new JsonObject();
                currencyObject.addProperty("name", name);
                currencyObject.addProperty("symbol", currency.getSymbol());
                currencyObject.addProperty("color", currency.getColor());
                currencyObject.addProperty("amount", currency.getAmount());
                object.add(name, currencyObject);
            });
            return object;
        }

    }

    /*
     * private static final class GuildAdapter implements JsonSerializer<Guild>, JsonDeserializer<Guild> {
     *
     * @Override public Guild deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext
     * jsonDeserializationContext) throws JsonParseException { JsonObject object = jsonElement.getAsJsonObject(); Guild
     * guild = new Guild(object.get("guildName").getAsString(),
     * LocalDateTime.parse(object.get("creationDate").getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
     * GSON.fromJson("guildMembers", Ha)
     *
     * ); return null; }
     *
     * @Override public JsonElement serialize(Guild guild, Type type, JsonSerializationContext jsonSerializationContext)
     * { JsonObject jsonObject = new JsonObject(); jsonObject.addProperty("guildName", guild.getGuildName());
     * jsonObject.addProperty("creationDate", guild.getCreationDate().toString());
     * jsonObject.addProperty("guildMembers", GSON.toJson(guild.getGuildMemberMap()));
     * jsonObject.addProperty("guildPermissions", GSON.toJson(guild.getGuildMapPermissions())); return jsonObject; } }
     */

}
