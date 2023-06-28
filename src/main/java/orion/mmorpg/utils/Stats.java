package orion.mmorpg.utils;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * A class to handle different Stats
 */
public class Stats {
    private HashMap<StatsInfo, Float> baseStats = new HashMap<>();
    private HashMap<String, Modifiers> modifiers = new HashMap<>();


    public float getBase(StatsInfo stat) {
        return baseStats.getOrDefault(stat, 0f);
    }

    public float get(StatsInfo stat) {
        float value = getBase(stat);
        for (Modifiers modifiers : this.modifiers.values()) {
            value += modifiers.add.getOrDefault(stat, 0f);
            value *= modifiers.multiply.getOrDefault(stat, 1f);
        }
        return Math.min(value, stat.getCap());
    }

    public void addModifiers(String id, Modifiers modifiers) {
        this.modifiers.put(id, modifiers);
        Bukkit.getLogger().info("Added modifiers " + id + " " + modifiers.getAsLore());
    }

    public void removeModifiers(String id) {
        this.modifiers.remove(id);
        Bukkit.getLogger().info("Removed modifiers " + id);
    }

    public boolean hasModifiers(String id) {
        return this.modifiers.containsKey(id);
    }
    /**
     * Generate an array list of every stat with there value, color and sigill
     *
     * @return Generated list of stats
     */
    public List<String> statsAsLore() {
        List<String> lore = new LinkedList<>();
        for (StatsInfo stat : StatsInfo.values()) {
            float value = get(stat);
            if (value == 0)
                continue;
            lore.add(stat.getColor() + stat.getCharacter() + " " + WordUtils.capitalizeFully(stat.name().replaceAll("_", " ")) + " " + stat.getColor() + value + stat.getSuffix());
        }
        return lore;
    }

    public List<String> modifiersAsLore() {
        List<String> lore = new LinkedList<>();
        for (Modifiers modifiers : this.modifiers.values()) {
            lore.addAll(modifiers.getAsLore());
        }
        return lore;
    }

public static class Modifiers {
        private HashMap<StatsInfo, Float> add = new HashMap<>();
        private HashMap<StatsInfo, Float> multiply = new HashMap<>();

        public void add(StatsInfo stat, float value) {
            add.put(stat, add.getOrDefault(stat, 0f) + value);
        }

        public void multiply(StatsInfo stat, float value) {
            multiply.put(stat, multiply.getOrDefault(stat, 1f) * value);
        }

        public float get(StatsInfo stat) {
            return add.getOrDefault(stat, 0f) + multiply.getOrDefault(stat, 1f);
        }

        public float getAdd(StatsInfo stat) {
            return add.getOrDefault(stat, 0f);
        }

        public float getMultiply(StatsInfo stat) {
            return multiply.getOrDefault(stat, 1f);
        }

        public Modifiers combine(Modifiers modifiers) {
            Modifiers newModifiers = new Modifiers();
            for (StatsInfo stat : StatsInfo.values()) {
                newModifiers.add(stat, getAdd(stat) + modifiers.getAdd(stat));
                newModifiers.multiply(stat, getMultiply(stat) * modifiers.getMultiply(stat));
            }
            return newModifiers;
        }

        public List<String> getAsLore() {
            List<String> lore = new LinkedList<>();
            for (StatsInfo stat : StatsInfo.values()) {
                float value = add.getOrDefault(stat, 0f);
                if (value == 0)
                    continue;
                lore.add(stat.getColor() + stat.getCharacter() + " " + WordUtils.capitalizeFully(stat.name().replaceAll("_", " ")) + ": " + ChatColor.GRAY + (value >= 1 ? "+":"") + value);
            }
            for (StatsInfo stat : StatsInfo.values()) {
                float value = multiply.getOrDefault(stat, 1f);
                if (value == 1)
                    continue;
                lore.add(stat.getColor() + stat.getCharacter() + " " + WordUtils.capitalizeFully(stat.name().replaceAll("_", " ")) + ": " + ChatColor.GRAY + (value >= 1 ? "+"+((value*100)-100):"-"+(100-(value*100)) ) + "%");
            }
            return lore;
        }

        public static class Builder {
            private Modifiers modifiers = new Modifiers();

            public Builder add(StatsInfo stat, float value) {
                modifiers.add.put(stat, value);
                return this;

            }

            public Builder multiply(StatsInfo stat, float value) {
                modifiers.multiply.put(stat, value);
                return this;
            }

            public Modifiers build() {
                return modifiers;
            }

        }
}

    public enum StatsInfo {
        STRENGTH("🗡", ChatColor.of("#F00020"),"", Float.MAX_VALUE),
        CRITICAL_DAMAGE("☠", ChatColor.of("#FF6600"),"", Float.MAX_VALUE),
        CRITICAL_CHANCE("☣", ChatColor.of("#FFD500"),"%", 100),
        ATTACK_SPEED("⚔", ChatColor.of("#3A9D23"),"", 400),
        VITALITY("❤", ChatColor.of("#F9429E"),"", Float.MAX_VALUE),
        INTELLIGENCE("࿇", ChatColor.of("#00ACEE"),"", Float.MAX_VALUE),
        DEFENCE("🛡", ChatColor.of("#884DA7"),"", Float.MAX_VALUE),
        SPEED("࿓", ChatColor.of("#207125"),"", 1000),
        LUCK("☘", ChatColor.of("#FFA500"), "", 500),
        MINING_SPEED("⛏", ChatColor.of("#FFD500"), "", 1000),
        FISHING_SPEED("🎣", ChatColor.of("#FFD500"),"", 1000),
        SEA_CREATURE_CHANCE("♆", ChatColor.of("#FFD500"), "%", 100),
        ;

        String character;
        ChatColor color;
        @Getter
        private String suffix;
        float cap;

        StatsInfo(String character, ChatColor color, String suffix, float cap) {
            this.character = character;
            this.color = color;
            this.suffix = suffix;
            this.cap = cap;
        }

        public String getCharacter() {
            return character;
        }

        public ChatColor getColor() {
            return color;
        }

        public float getCap() {
            return cap;
        }
    }

    @Deprecated
    public static final class Builder {
        private final Stats stats;

        public Builder() {
            stats = new Stats();
        }

        public Builder setBase(StatsInfo stat, float value) {
            stats.baseStats.put(stat, value);
            return this;
        }

        public Stats build() {
            return stats;
        }
    }
}
