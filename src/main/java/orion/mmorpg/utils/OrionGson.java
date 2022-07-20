package orion.mmorpg.utils;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.players.Equipments;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

public class OrionGson {
    public Gson GSON;

    public OrionGson() {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping();
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        builder.registerTypeAdapter(ItemStack.class, new ItemStackAdapter());
        builder.registerTypeAdapter(ItemStack[].class, new ItemStackArrayAdapter());
        builder.registerTypeAdapter(Location.class, new LocationAdapter());
        builder.registerTypeAdapter(Knowledge.class, new KnowledgeAdapter());
        builder.registerTypeAdapter(Equipments.class, new EquipmentsAdapter());
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
            ItemMeta meta = Objects.requireNonNull(Objects.requireNonNull(itemStack.getItemMeta()));
            jsonObject.addProperty("internal_id", meta.getPersistentDataContainer().get(Namespaces.internal_id, PersistentDataType.STRING));
            jsonObject.addProperty("amount", itemStack.getAmount());
            return jsonObject;
        }

        @Override
        public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (!jsonElement.isJsonObject())
                if (Objects.equals(jsonElement.getAsString(), "null"))
                    return null;
            System.out.println(jsonElement);
            JsonObject object = jsonElement.getAsJsonObject();
            return ItemList.valueOf(object.get("internal_id").getAsString()).getProcessedItem(object.get("amount").getAsInt());
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
                object.addProperty("helmet", equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.HELMET).getINTERNAL_ID());


            if(equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.CHESTPLATE) != null)
                object.addProperty("chestplate", equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.CHESTPLATE).getINTERNAL_ID());

            if(equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.LEGGINGS) != null)
                object.addProperty("leggings", equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.LEGGINGS).getINTERNAL_ID());

            if(equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.BOOTS) != null)
                object.addProperty("boots", equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.BOOTS).getINTERNAL_ID());

            if(equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.BELT) != null)
                object.addProperty("belt", equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.BELT).getINTERNAL_ID());

            if(equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.LEFT_RING) != null)
                object.addProperty("left_ring", equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.LEFT_RING).getINTERNAL_ID());

            if(equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.RIGHT_RING) != null)
                object.addProperty("right_ring", equipments.getEquipmentFromSlot(Equipments.EquipmentSlot.RIGHT_RING).getINTERNAL_ID());
            return object;
        }

        @Override
        public Equipments deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject object = jsonElement.getAsJsonObject();
            Equipments equipments = new Equipments();
            if(object.has("helmet"))
                equipments.addEquipment((BasicArmorItem) ItemList.valueOf(object.get("helmet").getAsString()).getItem());
            if(object.has("chestplate"))
                equipments.addEquipment((BasicArmorItem) ItemList.valueOf(object.get("chestplate").getAsString()).getItem());
            if(object.has("leggings"))
                equipments.addEquipment((BasicArmorItem) ItemList.valueOf(object.get("leggings").getAsString()).getItem());
            if(object.has("boots"))
                equipments.addEquipment((BasicArmorItem) ItemList.valueOf(object.get("boots").getAsString()).getItem());
            if(object.has("belt"))
                equipments.addEquipment((BasicArmorItem) ItemList.valueOf(object.get("belt").getAsString()).getItem());
            if(object.has("left_ring"))
                equipments.addEquipment((BasicArmorItem) ItemList.valueOf(object.get("left_ring").getAsString()).getItem());
            if(object.has("right_ring"))
                equipments.addEquipment((BasicArmorItem) ItemList.valueOf(object.get("right_ring").getAsString()).getItem());
            return equipments;
        }
    }

}
