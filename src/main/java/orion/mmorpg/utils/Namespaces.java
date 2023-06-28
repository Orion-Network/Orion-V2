package orion.mmorpg.utils;

import org.bukkit.NamespacedKey;
import orion.mmorpg.Mmorpg;

public final class Namespaces {
    static Mmorpg plugin = Mmorpg.INSTANCE;
    public static final NamespacedKey
            internal_id = NamespacedKey.fromString("internal_id", plugin),
            player_item = NamespacedKey.fromString("player_item", plugin),
            name = NamespacedKey.fromString("name", plugin),
            lore = NamespacedKey.fromString("lore", plugin),
            uuid = NamespacedKey.fromString("uuid", plugin),
            rarity = NamespacedKey.fromString("rarity", plugin),
            base_stats = NamespacedKey.fromString("base_stats", plugin),
            stats = NamespacedKey.fromString("stats", plugin),
            modifiers = NamespacedKey.fromString("modifiers", plugin),
            reforge = NamespacedKey.fromString("reforge", plugin),
            smithingLevelRequired = NamespacedKey.fromString("smithing_level_required", plugin),
            type = NamespacedKey.fromString("type", plugin),
            slot = NamespacedKey.fromString("slot", plugin),
            armor_type = NamespacedKey.fromString("armor_type",  plugin),
            damage = NamespacedKey.fromString("damage", plugin),
            gifter = NamespacedKey.fromString("gifter", plugin),
            support = NamespacedKey.fromString("support", plugin),
            spell = NamespacedKey.fromString("spell", plugin),
            spell_id = NamespacedKey.fromString("spell_id", plugin),
            knowledge = NamespacedKey.fromString("knowledge", plugin),
            mobhealth = NamespacedKey.fromString("mobhealth", plugin),
            health = NamespacedKey.fromString("health", plugin),
            guild_name = NamespacedKey.fromString("guild_name", plugin),

            mark_location = NamespacedKey.fromString("mark_location", plugin),
            tool_type = NamespacedKey.fromString("tool_type", plugin),
            tool_power = NamespacedKey.fromString("tool_power", plugin)
            ;
}
