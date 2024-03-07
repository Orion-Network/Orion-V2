package orion.mmorpg.utils;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

/**
 * An enum to handle rarity of items, spells, supports and races
 */
@Getter
public enum Rarity {
    UNOBTAINABLE(ChatColor.DARK_GRAY, "✬"), MYTHIC(ChatColor.LIGHT_PURPLE, "⚝⚝⚝"), LEGENDARY(ChatColor.DARK_RED, "⚝⚝"),
    HERO(ChatColor.RED, "⚝"), SUPERRARE(ChatColor.GOLD, "★★★"), RARE(ChatColor.DARK_GREEN, "★★"),
    UNCOMMON(ChatColor.GREEN, "★"), COMMON(ChatColor.WHITE, ""), TRASH(ChatColor.GRAY, "✧");

    ChatColor chatColor;
    String rarity;

    Rarity(ChatColor chatColor, String rarity) {
        this.chatColor = chatColor;
        this.rarity = rarity;
    }
}
