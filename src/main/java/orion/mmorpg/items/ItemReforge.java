package orion.mmorpg.items;

import org.bukkit.Bukkit;
import orion.mmorpg.items.sample.BasicEquipmentItem;
import orion.mmorpg.items.sample.BasicMeleeWeapon;
import orion.mmorpg.items.sample.BasicTool;
import orion.mmorpg.utils.Stats;

import java.sql.Ref;
import java.util.function.Function;

public enum ItemReforge {
    NONE("", BasicEquipmentItem.class, start -> start), STRONG("Forte", BasicMeleeWeapon.class, (stats) -> {
        Stats.Modifiers modifiers = new Stats.Modifiers();
        modifiers.add(Stats.StatsInfo.STRENGTH, 500);
        return stats.combine(modifiers);
    }), POLARIZER("Polarized", BasicEquipmentItem.class, (stats) -> {
        Stats.Modifiers modifiers = new Stats.Modifiers();
        modifiers.multiply(Stats.StatsInfo.STRENGTH, stats.getAdd(Stats.StatsInfo.INTELLIGENCE) / 10);
        modifiers.multiply(Stats.StatsInfo.INTELLIGENCE, stats.getAdd(Stats.StatsInfo.STRENGTH));
        return modifiers;
    }), TASTY_STONE("Appétissante", BasicTool.class, (stats) -> {
        Stats.Modifiers modifiers = new Stats.Modifiers();
        modifiers.add(Stats.StatsInfo.FISHING_SPEED, 500);
        return stats.combine(modifiers);
    }),;

    String title;
    Class<? extends Reforgeable> itemClazz;
    Function<Stats.Modifiers, Stats.Modifiers> function;

    ItemReforge(String title, Class<? extends Reforgeable> itemClazz,
            Function<Stats.Modifiers, Stats.Modifiers> function) {
        this.title = title;
        this.itemClazz = itemClazz;
        this.function = function;
    }

    public String getTitle() {
        return title;
    }

    public Class<? extends Reforgeable> getItemClazz() {
        return itemClazz;
    }

    public Function<Stats.Modifiers, Stats.Modifiers> getFunction() {
        return function;
    }
}
