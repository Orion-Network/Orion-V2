package orion.mmorpg.items;

import orion.mmorpg.items.sample.BasicEquipmentItem;
import orion.mmorpg.items.sample.BasicMeleeWeapon;
import orion.mmorpg.utils.Stats;

import java.util.function.Function;

public enum ItemReforge {
    STRONG(BasicMeleeWeapon.class, (start) -> {
        start.setStrength(start.getStrength()*100);
        return start;
    })
    ;

    Class<? extends BasicEquipmentItem> itemClazz;
    Function<Stats, Stats> function;

    ItemReforge(Class<? extends BasicEquipmentItem> itemClazz, Function<Stats, Stats> function) {
        this.itemClazz = itemClazz;
        this.function = function;
    }
}
