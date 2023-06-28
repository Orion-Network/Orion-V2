package orion.mmorpg.items;

import orion.mmorpg.utils.Stats;

public interface Modifiers {
    Stats.Modifiers getModifiers();

    default void addModifier(Stats.StatsInfo info, float value) {
        getModifiers().add(info, value);
    }

    default void multiplyModifier(Stats.StatsInfo info, float value) {
        getModifiers().multiply(info, value);
    }
}
