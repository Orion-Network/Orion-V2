package orion.mmorpg.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.events.OnGetEffect;
import orion.mmorpg.gui.MerchantGui;
import orion.mmorpg.gui.editors.PlayerEditor;
import orion.mmorpg.items.enchants.CustomEnchants;
import orion.mmorpg.items.sample.BasicIngredient;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.effects.Invincibility;
import orion.mmorpg.players.abilities.effects.Propagation;
import orion.mmorpg.utils.Currencies;
import orion.mmorpg.utils.ItemUtils;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Utils;

import java.util.*;

public class AdminCommand implements TabExecutor {

    Mmorpg mmorpg;

    public AdminCommand(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
            @NotNull String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            OrionPlayer orionPlayer = mmorpg.getPlayerManager().getPlayer(((Player) commandSender).getUniqueId());
            if (args[0].equalsIgnoreCase("setlevel")) {
                orionPlayer.setLevel(Integer.parseInt(args[1]));
            } else if (args[0].equalsIgnoreCase("save")) {
                mmorpg.getPlayerManager().savePlayers();
                Utils.sendInfoMessage((Entity) commandSender, "Saving players.");
            } else if (args[0].equalsIgnoreCase("debug")) {
                if (args[1].equalsIgnoreCase("bossbar")) {
                    Bukkit.getBossBars().forEachRemaining(BossBar::removeAll);
                }
            } else if (args[0].equalsIgnoreCase("editor")) {
                if (args[1].equalsIgnoreCase("player")) {
                    Player targetPlayer = Bukkit.getPlayer(args[2]);
                    OrionPlayer orionPlayer1 = mmorpg.getPlayerManager().getPlayer(targetPlayer.getUniqueId());
                    new PlayerEditor((Player) commandSender,
                            mmorpg.getPlayerManager().getPlayer(((Player) commandSender).getUniqueId()), targetPlayer,
                            orionPlayer1);
                }
            } else if (args[0].equalsIgnoreCase("test")) {
                if (args[1].equalsIgnoreCase("enchant")) {
                    // TODO find how to modify the itemstack to add enchants easily with lore
                    ItemStack itemStack = player.getInventory().getItemInMainHand();
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.addEnchant(CustomEnchants.WRATH, 1, true);
                    itemStack.setItemMeta(itemMeta);

                    BasicItem basicItem = ItemUtils.getItemFromItemstack(itemStack);
                    player.getInventory().setItemInMainHand(basicItem.generateItem(itemStack, 1));

                }
                if (args[1].equalsIgnoreCase("currencies")) {

                    orionPlayer.getCurrencies().add("money", Integer.parseInt(args[2]));
                    Utils.sendInfoMessage((Entity) commandSender,
                            "" + orionPlayer.getCurrencies().currencies.containsKey("test"));
                    for (Currencies.Currency currency : orionPlayer.getCurrencies().currencies.values()) {
                        Utils.sendInfoMessage((Entity) commandSender,
                                orionPlayer.getCurrencies().getFormatted(currency.name, Utils.NumberFormat.READABLE)
                                        + " " + currency.symbol);
                    }
                } else if (args[1].equalsIgnoreCase("cmenu")) {
                    ArrayList<MerchantGui.Buyable> buyables = new ArrayList<>();
                    buyables.add(new MerchantGui.Buyable("test", 5, ItemUtils.generateItemStack("KNIGHT_BOOTS", 1)));
                    buyables.add(
                            new MerchantGui.Buyable("test", 50000, ItemUtils.generateItemStack("KNIGHT_BOOTS", 1)));

                    MerchantGui merchantGui = new MerchantGui("Test gui", orionPlayer, player, buyables);
                    merchantGui.show(player);
                } else if (args[1].equalsIgnoreCase("oeffect")) {
                    orionPlayer.addEffect("test", new Invincibility(100L));
                } else if (args[1].equalsIgnoreCase("effect")) {
                    orionPlayer.getOrionEffects().forEach((uuid, map) -> {
                        map.forEach((id, effect) -> {
                            Utils.sendInfoMessage((Entity) commandSender, id + " " + effect.getName() + " "
                                    + effect.getDuration() + " " + effect.getEvent() + " " + effect.getRemainingTime());
                        });
                    });
                } else if (args[1].equalsIgnoreCase("ingredient")) {
                    BasicItem ingredient = BasicIngredient.generateIngredientItem(Material.DIAMOND, "test", 0,
                            new ArrayList<>(), Rarity.COMMON, Invincibility.class);
                    player.getInventory().addItem(ingredient.generateItem(null, 1));
                } else if (args[1].equalsIgnoreCase("display")) {
                    mmorpg.getItemManager().getItems().forEach((s1, aClass) -> {
                        Utils.sendInfoMessage((Entity) commandSender, s1);
                    });
                }

            }
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
            @NotNull String[] args) {
        if (args[0].equalsIgnoreCase("setlevel")) {
            return Arrays.asList("0", "5", "10", "15", "30");
        } else if (args[0].equalsIgnoreCase("editor")) {
            if (args[0].equalsIgnoreCase("player")) {
                return null;
            }
            return Arrays.asList("player", "item");
        }

        return Arrays.asList("setlevel", "editor", "test", "debug", "save");
    }
}
