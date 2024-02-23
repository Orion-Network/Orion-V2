package orion.mmorpg.items.armors;

import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.Arrays;

public class CosmeticHeads {
    public static class ShangriLa extends BasicArmorItem {
        public ShangriLa() {
            super(Material.PLAYER_HEAD, "Shangri-La", "SHANGRI_LA", 0,
                    Arrays.asList("Un chapeau ridicule et de mauvais gouts", "Veillez a avoir d'autre vêtements",
                            "sinon vous seriez vite vu comme un pervers"),
                    Rarity.RARE, ArmorType.HELMET, new Stats.Modifiers.Builder().build());
        }

        @Override
        public ItemStack generateItem(ItemStack item, int amount) {
            System.out.println("ShangriLa.generateItem");
            return SkullCreator.itemWithBase64(super.generateItem(item, amount),
                    "ewogICJ0aW1lc3RhbXAiIDogMTY5NjQ4MTc0NDE5OSwKICAicHJvZmlsZUlkIiA6ICJkNzU2OTc4MWUyYjY0OWIyYjVlMjVlYTJhNDZkOGQxOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJEckthcGRvciIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lNTk1ZTBmOTE2YzIzY2JmNzA5OWQwNzMyMTI1MmEzMjg1NzBiMzVjMTRiZmFlMzMzMzBhNTFjOTQ4NWM0NjhmIgogICAgfQogIH0KfQ==");
        }
    }
}
