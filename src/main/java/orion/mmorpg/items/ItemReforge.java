package orion.mmorpg.items;

import orion.mmorpg.items.sample.BasicEquipmentItem;
import orion.mmorpg.items.sample.BasicMeleeWeapon;
import orion.mmorpg.utils.Stats;

import java.util.function.Function;

public enum ItemReforge {
    NONE("", BasicEquipmentItem.class, start -> start),
    STRONG("Forte", BasicMeleeWeapon.class, (start) -> {
        start.setStrength(start.getStrength()+5);
        return start;
    })
    ;

    String title;
    Class<? extends BasicEquipmentItem> itemClazz;
    Function<Stats, Stats> function;

    ItemReforge(String title, Class<? extends BasicEquipmentItem> itemClazz, Function<Stats, Stats> function) {
        this.title = title;
        this.itemClazz = itemClazz;
        this.function = function;
    }

    public String getTitle() {
        return title;
    }

    public Class<? extends BasicEquipmentItem> getItemClazz() {
        return itemClazz;
    }

    public Function<Stats, Stats> getFunction() {
        return function;
    }
}
