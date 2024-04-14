package orion.mmorpg.listeners;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Utils;
import xyz.upperlevel.spigot.book.BookUtil;

public class PlayerChatListener implements Listener {

    static Player player;

    public static TextComponent formatMessage(String string) {
        TextComponent textComponent = new TextComponent();
        String[] vars = StringUtils.substringsBetween(string, "%", "%");
        if (vars == null)
            return new TextComponent(string);
        for (int i = 0; i <= vars.length; i++) {
            if (string.split("%").length > 2 * i)
                textComponent.addExtra(string.split("%")[(2 * i)]);
            if (i != vars.length) {
                textComponent.addExtra(formatVariable(vars[i], player));
            }
        }
        return textComponent;
    }

    public static BaseComponent formatVariable(String var, Object varSource) {
        BookUtil.TextBuilder textBuilder = new BookUtil.TextBuilder();
        if (varSource instanceof Player) {
            Player pl = (Player) varSource;
            OrionPlayer stats = Mmorpg.INSTANCE.getPlayerManager().getPlayer(pl.getUniqueId());

            if (var.contains("item")) {
                if (var.equalsIgnoreCase("item")) {
                    if (pl.getInventory().getItemInMainHand().getType() == Material.AIR) {
                        Utils.sendErrorMessage(pl, "L'item designé n'est pas valide");
                        return null;
                    }
                    return textBuilder.text(ChatColor.GREEN + "[" + ChatColor.WHITE
                            + Utils.itemDisplayName(player.getInventory().getItemInMainHand()) + ChatColor.GREEN + "]")
                            .onHover(BookUtil.HoverAction.showItem(pl.getInventory().getItemInMainHand())).build();
                } else if (!var.replaceAll("[^\\d.]", "").equals("")) {
                    int slot = Integer.parseInt(var.replaceAll("[^\\d.]", ""));
                    if (pl.getInventory().getItem(slot).getType() == Material.AIR) {
                        Utils.sendErrorMessage(pl, "L'item designé n'est pas valide");
                        return null;
                    }
                    return textBuilder
                            .text(ChatColor.GREEN + "[" + ChatColor.WHITE
                                    + Utils.itemDisplayName(pl.getInventory().getItem(slot)) + ChatColor.GREEN + "]")
                            .onHover(BookUtil.HoverAction.showItem(pl.getInventory().getItem(slot))).build();
                }
            } else if (var.equalsIgnoreCase("me")) {

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(ChatColor.GRAY + "Nom: " + ChatColor.WHITE + pl.getName() + "\n");
                stringBuilder.append(ChatColor.GRAY + "Niveau: " + ChatColor.WHITE + stats.getLevel() + "\n");
                stringBuilder.append(ChatColor.GRAY + "Classe:" + ChatColor.WHITE + stats.getClasse().getName() + "\n");
                stringBuilder.append(
                        ChatColor.GRAY + "Support: " + ChatColor.WHITE + stats.getMainSupport().getName() + "\n");
                stats.getStats().statsAsLore()
                        .forEach(s -> stringBuilder.append(ChatColor.GRAY + "\n" + ChatColor.stripColor(s)));

                return textBuilder
                        .text(ChatColor.GREEN + "[" + ChatColor.WHITE + pl.getDisplayName() + ChatColor.GREEN + "]")
                        .onHover(BookUtil.HoverAction.showText(stringBuilder.toString())).build();
            }
        }
        return null;
    }

    public static String formatPrePlayerMessage(Player player) {
        LuckPerms luckPerms = Mmorpg.getLuckPerms();
        User user = luckPerms.getPlayerAdapter(Player.class).getUser(player);
        return "§7[§6" + user.getCachedData().getMetaData().getPrefix() + "§7] §6" + player.getDisplayName() + "§7: §f";
    }

    @EventHandler
    public void playerChatEvent(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        player = e.getPlayer();
        TextComponent message = new TextComponent(formatPrePlayerMessage(player));
        message.addExtra(formatMessage(e.getMessage()));
        e.getPlayer().spigot().sendMessage(message);
    }
}
