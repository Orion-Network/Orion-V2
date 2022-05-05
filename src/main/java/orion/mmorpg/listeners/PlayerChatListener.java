package orion.mmorpg.listeners;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
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
                    return textBuilder.text(ChatColor.GREEN + "[" + ChatColor.WHITE + pl.getInventory().getItemInMainHand().getItemMeta().getDisplayName() + ChatColor.GREEN + "]").onHover(BookUtil.HoverAction.showItem(pl.getInventory().getItemInMainHand())).build();
                } else if (!var.replaceAll("[^\\d.]", "").equals("")) {
                    int slot = Integer.parseInt(var.replaceAll("[^\\d.]", ""));
                    if (pl.getInventory().getItem(slot).getType() == Material.AIR) {
                        Utils.sendErrorMessage(pl, "L'item designé n'est pas valide");
                        return null;
                    }
                    return textBuilder.text(ChatColor.GREEN + "[" + ChatColor.WHITE + pl.getInventory().getItem(slot).getItemMeta().getDisplayName() + ChatColor.GREEN + "]")
                            .onHover(BookUtil.HoverAction.showItem(pl.getInventory().getItem(slot))).build();
                }
            } else if (var.equalsIgnoreCase("me")) {//TODO Colors don't work
                TextComponent base = new TextComponent();
                base.setText(ChatColor.GREEN + "[" + ChatColor.WHITE + pl.getDisplayName() + ChatColor.GREEN + "]");

                BaseComponent description = new TextComponent();
                description.addExtra(ChatColor.GRAY + "Level : " + ChatColor.YELLOW + stats.getLevel());
                description.addExtra(ChatColor.GRAY + "\nClasse : " + ChatColor.YELLOW + stats.getRace().name());
                description.addExtra(ChatColor.GRAY + "\nSupport : " + ChatColor.YELLOW + (stats.getSupport() != null ? stats.getSupport().getName() : "Aucun"));
                stats.getStats().statsAsLore().forEach(description::addExtra);

                base.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(description).create()));
                return base;
            }
        }
        return null;
    }

    @EventHandler
    public void playerChatEvent(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        player = e.getPlayer();
        TextComponent message = new TextComponent(ChatColor.GRAY + e.getPlayer().getName() + " : ");
        message.addExtra(formatMessage(e.getMessage()));
        e.getPlayer().spigot().sendMessage(message);
    }
}
