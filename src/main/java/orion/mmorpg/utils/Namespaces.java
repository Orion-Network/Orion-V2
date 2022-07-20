package orion.mmorpg.utils;

import org.bukkit.NamespacedKey;
import orion.mmorpg.Mmorpg;

public final class Namespaces {
    static Mmorpg plugin = Mmorpg.INSTANCE;
    public static final NamespacedKey
            internal_id = NamespacedKey.fromString("internal_id", plugin),
            uuid = NamespacedKey.fromString("uuid", plugin),
            stats = NamespacedKey.fromString("stats", plugin),
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
            health = NamespacedKey.fromString("health", plugin)
            ;
}
