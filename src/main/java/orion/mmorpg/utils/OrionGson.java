package orion.mmorpg.utils;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.sample.*;
import orion.mmorpg.players.Equipments;
import orion.mmorpg.players.jobs.Jobs;
import orion.mmorpg.players.jobs.Smithing;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

public class OrionGson {
    public static Gson GSON;

    public OrionGson() {
        RuntimeTypeAdapterFactory<BasicItem> basicItemRuntimeTypeAdapterFactory = RuntimeTypeAdapterFactory.of(BasicItem.class, "typeofclazz")
                .registerSubtype(BasicArmorItem.class, "Armor")
                .registerSubtype(BasicArrow.class, "Arrow")
                .registerSubtype(BasicConsumableItem.class, "Consumable")
                .registerSubtype(BasicMagicWeapon.class, "MagicWeapon")
                .registerSubtype(BasicMeleeWeapon.class, "MeleeWeapon")
                .registerSubtype(BasicRangedWeapon.class, "RangedWeapon");
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping();
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        builder.registerTypeAdapter(ItemStack.class, new ItemStackAdapter());
        builder.registerTypeAdapter(ItemStack[].class, new ItemStackArrayAdapter());
        builder.registerTypeAdapter(Location.class, new LocationAdapter());
        builder.registerTypeAdapter(Knowledge.class, new KnowledgeAdapter());
        builder.registerTypeAdapter(Equipments.class, new EquipmentsAdapter());
        builder.registerTypeAdapterFactory(basicItemRuntimeTypeAdapterFactory);
        builder.registerTypeAdapter(Jobs.class, new JobsAdapter());
        GSON = builder.create();
    }

    public Gson getGSON() {
        return GSON;
    }

    static final class ItemStackAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

        @Override
        public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext jsonSerializationContext) {
            if (itemStack == null)
                return JsonNull.INSTANCE;
            JsonObject jsonObject = new JsonObject();
            ItemMeta meta = Objects.requireNonNull(itemStack.getItemMeta());
            jsonObject.addProperty("internal_id", meta.getPersistentDataContainer().get(Namespaces.internal_id, PersistentDataType.STRING));
            jsonObject.addProperty("amount", itemStack.getAmount());
            return jsonObject;
        }

        @Override
        public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (!jsonElement.isJsonObject())
                if (Objects.equals(jsonElement.getAsString(), "null"))
                    return null;
            JsonObject object = jsonElement.getAsJsonObject();
            return ItemList.valueOf(object.get("internal_id").getAsString()).generateItemStack(object.get("amount").getAsInt());
        }
    }

    static final class ItemStackArrayAdapter implements JsonSerializer<ItemStack[]>, JsonDeserializer<ItemStack[]> {

        ItemStackAdapter itemStackAdapter = new ItemStackAdapter();

        @Override
        public JsonElement serialize(ItemStack[] itemStacks, Type type, JsonSerializationContext jsonSerializationContext) {
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
        public ItemStack[] deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
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
        public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            return new Location(Bukkit.getWorld(jsonObject.get("world").getAsString()), jsonObject.get("x").getAsDouble(), jsonObject.get("y").getAsDouble(), jsonObject.get("z").getAsDouble());
        }
    }

    private final static class KnowledgeAdapter implements JsonSerializer<Knowledge>, JsonDeserializer<Knowledge> {

        @Override
        public JsonElement serialize(Knowledge knowledge, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("knowledge", knowledge.getKnowledge());
            return jsonObject;
        }

        @Override
        public Knowledge deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            return new Knowledge(jsonObject.get("knowledge").getAsInt());
        }
    }

    private final static class EquipmentsAdapter implements JsonSerializer<Equipments>, JsonDeserializer<Equipments> {
        @Override
        public JsonElement serialize(Equipments equipments, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject object = new JsonObject();
            if(equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.HELMET) != null)
                object.addProperty("helmet", GSON.toJson(equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.HELMET), BasicItem.class));


            if(equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.CHEST_PLATE) != null)
                object.addProperty("chestplate", GSON.toJson(equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.CHEST_PLATE), BasicItem.class));

            if(equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.LEGGINGS) != null)
                object.addProperty("leggings",GSON.toJson(equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.LEGGINGS), BasicItem.class));

            if(equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.BOOTS) != null)
                object.addProperty("boots", GSON.toJson(equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.BOOTS), BasicItem.class));

            if(equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.BELT) != null)
                object.addProperty("belt", GSON.toJson(equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.BELT), BasicItem.class));

            if(equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.LEFT_RING) != null)
                object.addProperty("left_ring", GSON.toJson(equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.LEFT_RING), BasicItem.class));

            if(equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.RIGHT_RING) != null)
                object.addProperty("right_ring", GSON.toJson(equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.RIGHT_RING), BasicItem.class));
            return object;
        }

        @Override
        public Equipments deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject object = jsonElement.getAsJsonObject();
            Equipments equipments = new Equipments();
            if(object.has("helmet"))
                equipments.addEquipment(GSON.fromJson(object.get("helmet").getAsString(), (Type) BasicItem.class));
            if(object.has("chestplate"))
                equipments.addEquipment(GSON.fromJson(object.get("chestplate").getAsString(), (Type) BasicItem.class));
            if(object.has("leggings"))
                equipments.addEquipment(GSON.fromJson(object.get("leggings").getAsString(), (Type) BasicItem.class));
            if(object.has("boots"))
                equipments.addEquipment(GSON.fromJson(object.get("boots").getAsString(), (Type) BasicItem.class));
            if(object.has("belt"))
                equipments.addEquipment(GSON.fromJson(object.get("belt").getAsString(), (Type) BasicItem.class));
            if(object.has("left_ring"))
                equipments.addEquipment(GSON.fromJson(object.get("left_ring").getAsString(), (Type) BasicItem.class));
            if(object.has("right_ring"))
                equipments.addEquipment(GSON.fromJson(object.get("right_ring").getAsString(), (Type) BasicItem.class));
            return equipments;
        }
    }

    private static final class JobsAdapter implements JsonSerializer<Jobs>, JsonDeserializer<Jobs> {
        @Override
        public Jobs deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject object = jsonElement.getAsJsonObject();
            JsonObject smitingObject = object.getAsJsonObject("smiting");
            return new Jobs(new Smithing(smitingObject.get("level").getAsInt(), smitingObject.get("experience").getAsInt()));
        }

        @Override
        public JsonElement serialize(Jobs jobs, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject object = new JsonObject();
            JsonObject smitingObject = new JsonObject();
            smitingObject.addProperty("level", jobs.getSmithing().getLevel());
            smitingObject.addProperty("experience", jobs.getSmithing().getExperience());
            object.add("smiting", smitingObject);
            return object;
        }
    }

    /*
    private static final class GuildAdapter implements JsonSerializer<Guild>, JsonDeserializer<Guild> {
        @Override
        public Guild deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject object = jsonElement.getAsJsonObject();
            Guild guild = new Guild(object.get("guildName").getAsString(),
                    LocalDateTime.parse(object.get("creationDate").getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    GSON.fromJson("guildMembers", Ha)

            );
            return null;
        }

        @Override
        public JsonElement serialize(Guild guild, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("guildName", guild.getGuildName());
            jsonObject.addProperty("creationDate", guild.getCreationDate().toString());
            jsonObject.addProperty("guildMembers", GSON.toJson(guild.getGuildMemberMap()));
            jsonObject.addProperty("guildPermissions", GSON.toJson(guild.getGuildMapPermissions()));
            return jsonObject;
        }
    }*/

}
