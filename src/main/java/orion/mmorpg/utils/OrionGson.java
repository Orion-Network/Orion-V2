package orion.mmorpg.utils;

import com.google.gson.*;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.items.ItemList;

import java.lang.reflect.Type;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;

public class OrionGson {
    public Gson GSON;

    public OrionGson() {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping();
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        builder.registerTypeAdapter(ItemStack.class, new ItemStackAdapter());
        builder.registerTypeAdapter(ItemStack[].class, new ItemStackArrayAdapter());
        GSON = builder.create();
    }

    public Gson getGSON() {
        return GSON;
    }

    static class ItemStackAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

        @Override
        public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext jsonSerializationContext) {
            if(itemStack == null)
                return JsonNull.INSTANCE;
            JsonObject jsonObject = new JsonObject();
            ItemMeta meta = Objects.requireNonNull(Objects.requireNonNull(itemStack.getItemMeta()));
            jsonObject.addProperty("internal_id", meta.getPersistentDataContainer().get(NamespacedKey.minecraft("internal_id"), PersistentDataType.STRING));
            jsonObject.addProperty("amount", itemStack.getAmount());
            return jsonObject;
        }

        @Override
        public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if(!jsonElement.isJsonObject())
                if(Objects.equals(jsonElement.getAsString(), "null"))
                    return null;
            System.out.println(jsonElement);
            JsonObject object = jsonElement.getAsJsonObject();
            return ItemList.valueOf(object.get("internal_id").getAsString()).getProcessedItem(object.get("amount").getAsInt());
        }
    }

    static class ItemStackArrayAdapter implements JsonSerializer<ItemStack[]>, JsonDeserializer<ItemStack[]> {

        ItemStackAdapter itemStackAdapter = new ItemStackAdapter();

        @Override
        public JsonElement serialize(ItemStack[] itemStacks, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject items = new JsonObject();
            for(int i = 0; i < itemStacks.length; i++) {
                if(itemStacks[i] != null)
                    items.add(String.valueOf(i), itemStackAdapter.serialize(itemStacks[i], null, null));
                else
                    items.addProperty(String.valueOf(i),"null");
            }
            System.out.println(items);
            return items;
        }

        //TODO Correct save and load of equipements
        @Override
        public ItemStack[] deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonArray = jsonElement.getAsJsonObject();
            System.out.println("Size : " + jsonArray.size());
            ItemStack[] items = new ItemStack[jsonArray.size()];

            for(int i = 0; i < jsonArray.size(); i++) {
                System.out.println("Debug : " + jsonArray.get(String.valueOf(i)) + " " + jsonArray.get(String.valueOf(i)).getClass());
                if(jsonArray.get(String.valueOf(i)) == null) {
                    items[i] = null;
                    System.out.println("Test");
                } else {
                    ItemStack item = itemStackAdapter.deserialize(jsonArray.get(String.valueOf(i)), null, null);
                    items[i] = item;
                }
            }
            System.out.println(Arrays.toString(items));
            return items;
        }
    }

    class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime> {

        public JsonElement serialize(LocalDateTime localDateTime, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(localDateTime.toInstant(ZoneOffset.UTC).getEpochSecond()); // "yyyy-mm-dd"
        }
    }
}
