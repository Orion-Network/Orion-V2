package orion.mmorpg.commands;

import org.apache.commons.lang3.EnumUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.misc.Gift;
import orion.mmorpg.items.misc.MasterScrollItem;
import orion.mmorpg.items.misc.MythicalScroll;
import orion.mmorpg.items.misc.SpellScroll;
import orion.mmorpg.players.abilities.SpellList;
import orion.mmorpg.players.abilities.SupportList;
import orion.mmorpg.utils.ItemUtils;
import orion.mmorpg.utils.Utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ItemCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("give")) {
                if (args.length > 2) {
                    if(Bukkit.getPlayer(args[1]) == null) {
                        Utils.sendErrorMessage((Entity) sender, "Le joueur n'existe pas.");
                        return false;
                    }
                    if (args[2].equalsIgnoreCase("master_scroll")) {
                        if (checkMasterSpellScrollValidity((Entity) sender, args)) return false;

                        Bukkit.getPlayer(args[1]).getInventory().addItem(((MasterScrollItem) ItemUtils.generateBasicItem(args[2].toUpperCase())).generateScroll(args[3], Integer.parseInt(args[4])));
                        return true;
                    } else if(args[2].equalsIgnoreCase("spell_scroll")) {
                        if(!EnumUtils.isValidEnum(SpellList.class, args[3].toUpperCase())) {
                            Utils.sendErrorMessage((Entity) sender, "Le sort n'existe pas.");
                            return false;
                        }
                        Bukkit.getPlayer(args[1]).getInventory().addItem(((SpellScroll) ItemUtils.generateBasicItem(args[2].toUpperCase())).generateScroll(args[3]));
                        return true;
                    }else if(args[2].equalsIgnoreCase("mythical_scroll")) {
                        Bukkit.getPlayer(args[1]).getInventory().addItem(((MythicalScroll) ItemUtils.generateBasicItem(args[2].toUpperCase())).generateScroll(args[3]));
                        return true;
                    }
                    if(!ItemUtils.isItem(args[2].toUpperCase())) {
                        Utils.sendErrorMessage((Entity) sender, "L'item n'existe pas.");
                        return false;
                    }
                    Bukkit.getPlayer(args[1]).getInventory().addItem(ItemUtils.generateItemStack(args[2].toUpperCase(), 1));
                } else {
                    if (sender instanceof Player) {
                        if (args[2].equalsIgnoreCase("master_scroll")) {
                            if (checkMasterSpellScrollValidity((Entity) sender, args)) return false;
                            ((Player) sender).getInventory().addItem(((MasterScrollItem) ItemUtils.generateBasicItem(args[1].toUpperCase())).generateScroll(args[2], Integer.parseInt(args[3])));
                            return true;
                        } else if(args[2].equalsIgnoreCase("spell_scroll")) {
                            if(!EnumUtils.isValidEnum(SpellList.class, args[3].toUpperCase())) {
                                Utils.sendErrorMessage((Entity) sender, "Le sort n'existe pas.");
                                return false;
                            }
                            ((Player) sender).getInventory().addItem(((SpellScroll) ItemUtils.generateBasicItem(args[2].toUpperCase())).generateScroll(args[3]));
                            return true;
                        } else if(args[2].equalsIgnoreCase("mythical_scroll")) {
                            if(!EnumUtils.isValidEnum(SupportList.class, args[3].toUpperCase())) {
                                Utils.sendErrorMessage((Entity) sender, "Le support n'existe pas.");
                                return false;
                            }
                            Bukkit.getPlayer(args[1]).getInventory().addItem(((MythicalScroll) ItemUtils.generateBasicItem(args[2].toUpperCase())).generateScroll(args[3]));
                            return true;
                        }
                        ((Player) sender).getInventory().addItem(ItemUtils.generateItemStack(args[1].toUpperCase(), 1));
                    }
                }
            } else if (args[0].equalsIgnoreCase("drop")) {
                if (sender instanceof Player p) {
                    if (args[1].equalsIgnoreCase("gift")) {
                        ItemStack gift = ((Gift) ItemUtils.generateBasicItem("GIFT")).generateGift(args[2], p.getDisplayName());
                        Item item = p.getWorld().dropItem(p.getTargetBlock(null, 20).getLocation(), gift);
                        item.setGlowing(true);
                        return true;
                    }
                    p.getWorld().dropItem(p.getTargetBlock(null, 20).getLocation(), ItemUtils.generateItemStack(args[1].toUpperCase(), 1));
                }
            }
        }

        return false;
    }

    private boolean checkMasterSpellScrollValidity(Entity sender, String[] args) {
        if(!EnumUtils.isValidEnum(SpellList.class, args[3].toUpperCase())) {
            Utils.sendErrorMessage(sender, "Le sort n'existe pas.");
            return true;
        }
        if(args.length < 5) {
            Utils.sendErrorMessage(sender, "Vous devez spécifier la quantité de savoir.");
            return true;
        }
        if(!args[4].matches("[0-9]+")) {
            Utils.sendErrorMessage(sender, "La quantité de savoir doit être un nombre.");
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tab = new LinkedList<>();
        if (args[0].equalsIgnoreCase("give")) {
            if (args.length == 2) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    tab.add(p.getName());
                }
                return tab;
            }
            if (args[2].equalsIgnoreCase("master_scroll") || args[2].equalsIgnoreCase("spell_scroll")) {
                return Utils.enumAutocomplete(args[args.length - 1], SpellList.class);
            }

            if(args[2].equalsIgnoreCase("mythical_scroll")) {
                return Utils.enumAutocomplete(args[args.length - 1], SupportList.class);
            }
            return Utils.listStringAutoComplete(args[args.length - 1], Mmorpg.INSTANCE.getItemManager().getItems().keySet().stream().toList());

        } else if (args[0].equalsIgnoreCase("drop")) {
            if (args[1].equalsIgnoreCase("gift")) {
                return Utils.listStringAutoComplete(args[args.length - 1], Mmorpg.INSTANCE.getItemManager().getItems().keySet().stream().toList());
            }
            return Utils.listStringAutoComplete(args[args.length - 1], Mmorpg.INSTANCE.getItemManager().getItems().keySet().stream().toList());
        }
        return Arrays.asList("give", "drop");
    }
}
