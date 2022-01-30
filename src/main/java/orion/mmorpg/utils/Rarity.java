package orion.mmorpg.utils;


import net.md_5.bungee.api.ChatColor;

public enum Rarity {
    UNOBTAINABLE(ChatColor.DARK_GRAY, "\u272c"),
    MYTHIC(ChatColor.DARK_PURPLE, "\u269d\u269d\u269d"),
    LEGENDARY(ChatColor.DARK_RED, "\u269d\u269d"),
    HERO(ChatColor.RED, "\u269d"),
    SUPERRARE(ChatColor.GOLD, "\u2605\u2605\u2605"),
    RARE(ChatColor.DARK_GREEN, "\u2605\u2605"),
    UNCOMMON(ChatColor.GREEN, "\u2605"),
    COMMON(ChatColor.WHITE, ""),
    TRASH(ChatColor.GRAY, "\u2727");

    ChatColor chatColor;
    String rarity;

    Rarity(ChatColor chatColor, String rarity) {
        this.chatColor = chatColor;
        this.rarity = rarity;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public String getRarity() {
        return rarity;
    }
}
