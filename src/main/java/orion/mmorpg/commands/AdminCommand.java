package orion.mmorpg.commands;

import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.gui.MerchantGui;
import orion.mmorpg.gui.editors.PlayerEditor;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Currencies;
import orion.mmorpg.utils.Utils;

import java.util.*;

public class AdminCommand implements TabExecutor {

    Mmorpg mmorpg;

    public AdminCommand(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(commandSender instanceof Player) {
            Player player = (Player)commandSender;
            OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(((Player) commandSender).getUniqueId());
            if(args[0].equalsIgnoreCase("setlevel")) {
                orionPlayer.setLevel(Integer.parseInt(args[1]));
            } else if(args[0].equalsIgnoreCase("save")) {
                mmorpg.getPlayerManager().savePlayers();
                Utils.sendInfoMessage((Entity) commandSender, "Saving players.");
            } else if (args[0].equalsIgnoreCase("debug")) {
                if(args[1].equalsIgnoreCase("bossbar")) {
                    Bukkit.getBossBars().forEachRemaining(BossBar::removeAll);
                }
            }
            else if (args[0].equalsIgnoreCase("editor")) {
                if(args[1].equalsIgnoreCase("player")) {
                    Player targetPlayer = Bukkit.getPlayer(args[2]);
                    OrionPlayer orionPlayer1 = mmorpg.getPlayerManager().getPlayer(targetPlayer.getUniqueId());
                    new PlayerEditor((Player) commandSender, mmorpg.getPlayerManager().getPlayer(((Player) commandSender).getUniqueId()), targetPlayer, orionPlayer1);
                }
            }
            else if(args[0].equalsIgnoreCase("test")) {
                if(args[1].equalsIgnoreCase("npc")) {
                    Mmorpg.INSTANCE.getOrionNpcManager().setup(player);

                }
                if(args[1].equalsIgnoreCase("currencies")) {

                    orionPlayer.getCurrencies().add("test", Integer.parseInt(args[2]));
                    Utils.sendInfoMessage((Entity) commandSender, "" + orionPlayer.getCurrencies().currencies.containsKey("test"));
                    for(Currencies.Currency currency : orionPlayer.getCurrencies().currencies.values()) {
                        Utils.sendInfoMessage((Entity) commandSender, orionPlayer.getCurrencies().getFormatted(currency.name, Utils.NumberFormat.READABLE) + " " + currency.symbol);
                    }
                } else if(args[1].equalsIgnoreCase("cmenu")) {
                    ArrayList<MerchantGui.Buyable> buyables = new ArrayList<>();
                    buyables.add(new MerchantGui.Buyable("test", 5, ItemList.KNIGHT_BOOTS.generateItemStack(1)));
                    buyables.add(new MerchantGui.Buyable("test", 50000, ItemList.KNIGHT_CHESTPLATE.generateItemStack(1)));


                    MerchantGui merchantGui = new MerchantGui("Test gui", orionPlayer, player, buyables);
                    merchantGui.show(player);
                }

            }
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        ArrayList<String> completion = new ArrayList<>();
        if(args[0].equalsIgnoreCase("setlevel")) {
            return Arrays.asList("0", "5", "10", "15", "30");
        } else if(args[0].equalsIgnoreCase("editor")) {
            if(args[0].equalsIgnoreCase("player")) {
                return null;
            }
            return Arrays.asList("player", "item");
        }

        return Arrays.asList("setlevel", "editor", "test", "debug", "save");
    }
}
