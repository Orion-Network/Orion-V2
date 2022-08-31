package orion.mmorpg.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Utils;

import java.util.Collections;
import java.util.LinkedList;

public class StatsGui extends ChestGui {
    Player p;
    OrionPlayer orionPlayer;

    public StatsGui(Player p, OrionPlayer orionPlayer) {
        super(1, ChatColor.YELLOW + "Stats");
        this.p = p;
        this.orionPlayer = orionPlayer;

        this.addPane(getStatsPane());
        this.setOnGlobalClick(e -> {
            e.setCancelled(true);
            this.update();
        });

    }

    StaticPane pane = new StaticPane(0, 0, 9, 1);
    private StaticPane getStatsPane() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        ItemMeta headMeta = head.getItemMeta();
        headMeta.setDisplayName(ChatColor.YELLOW + orionPlayer.getRace().getName() + " " + p.getName());
        LinkedList<String> infos = new LinkedList<>();
        if (orionPlayer.getSupport() != null)
            infos.add(ChatColor.YELLOW + orionPlayer.getSupport().getName());
        infos.addAll(orionPlayer.getStats().statsAsLore());
        headMeta.setLore(infos);
        head.setItemMeta(headMeta);

        pane.addItem(new GuiItem(head), 0, 0);
        pane.addItem(new GuiItem(BasicItem.generateBasicItem(Material.PAPER, ChatColor.WHITE + "Informations" ,"", 0, Collections.singletonList(ChatColor.WHITE + "Vous avez : " + (orionPlayer.getStatsPoints() - orionPlayer.getSpentStatsPoint()) + " Points de stats"), Rarity.COMMON).generateItem(null, 1)), 1, 0);

        pane.addItem(new GuiItem(BasicItem.generateBasicItem(Material.DIAMOND_SWORD, "Strength", "", 1, Collections.singletonList("Vous avez : " + orionPlayer.getPointStats().getStrength() + " Points de force"), Rarity.COMMON).generateItem(null, 1), e -> {
            if(orionPlayer.getStatsPoints() > orionPlayer.getSpentStatsPoint()) {
                orionPlayer.getPointStats().setStrength(orionPlayer.getPointStats().getStrength() + 1);
                orionPlayer.setSpentStatsPoint(orionPlayer.getSpentStatsPoint() + 1);
                Utils.sendInfoMessage(p, "You have spent a stat point!");
            } else {
                Utils.sendErrorMessage(p, "You don't have any stat points!");
            }
        }), 4, 0);
        pane.addItem(new GuiItem(BasicItem.generateBasicItem(Material.IRON_SWORD, "Attack Speed", "", 1, Collections.singletonList("Vous avez : " + orionPlayer.getPointStats().getAttack_speed() + " Points de vitesse d'attaque"), Rarity.COMMON).generateItem(null, 1), e -> {
            if(orionPlayer.getStatsPoints() > orionPlayer.getSpentStatsPoint()) {
                orionPlayer.getPointStats().setAttack_speed(orionPlayer.getPointStats().getAttack_speed() + 1);
                orionPlayer.setSpentStatsPoint(orionPlayer.getSpentStatsPoint() + 1);
                Utils.sendInfoMessage(p, "You have spent a stat point!");
            } else {
                Utils.sendErrorMessage(p, "You don't have any stat points!");
            }
        }), 5, 0);
        pane.addItem(new GuiItem(BasicItem.generateBasicItem(Material.POTION, "Vitality", "", 1, Collections.singletonList("Vous avez : " + orionPlayer.getPointStats().getVitality() + " Points de vitalité"), Rarity.COMMON).generateItem(null, 1), e -> {
            if(orionPlayer.getStatsPoints() > orionPlayer.getSpentStatsPoint()) {
                orionPlayer.getPointStats().setVitality(orionPlayer.getPointStats().getVitality() + 1);
                orionPlayer.setSpentStatsPoint(orionPlayer.getSpentStatsPoint() + 1);
                Utils.sendInfoMessage(p, "You have spent a stat point!");
            } else {
                Utils.sendErrorMessage(p, "You don't have any stat points!");
            }
        }), 6, 0);
        pane.addItem(new GuiItem(BasicItem.generateBasicItem(Material.SHIELD, "Defence", "", 1, Collections.singletonList("Vous avez : " + orionPlayer.getPointStats().getDefence() + " Points de défence"), Rarity.COMMON).generateItem(null, 1), e -> {
            if(orionPlayer.getStatsPoints() > orionPlayer.getSpentStatsPoint()) {
                orionPlayer.getPointStats().setDefence(orionPlayer.getPointStats().getDefence() + 1);
                orionPlayer.setSpentStatsPoint(orionPlayer.getSpentStatsPoint() + 1);
                Utils.sendInfoMessage(p, "You have spent a stat point!");
            } else {
                Utils.sendErrorMessage(p, "You don't have any stat points!");
            }
        }), 7, 0);
        pane.addItem(new GuiItem(BasicItem.generateBasicItem(Material.IRON_BOOTS, "Speed", "", 1, Collections.singletonList("Vous avez : " + orionPlayer.getPointStats().getSpeed() + " Points de vitesse"), Rarity.COMMON).generateItem(null, 1), e -> {
            if(orionPlayer.getStatsPoints() > orionPlayer.getSpentStatsPoint()) {
                orionPlayer.getPointStats().setSpeed(orionPlayer.getPointStats().getSpeed() + 1);
                orionPlayer.setSpentStatsPoint(orionPlayer.getSpentStatsPoint() + 1);
                Utils.sendInfoMessage(p, "You have spent a stat point!");
            } else {
                Utils.sendErrorMessage(p, "You don't have any stat points!");
            }
        }), 8, 0);
        return pane;
    }


}
