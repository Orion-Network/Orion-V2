package orion.mmorpg.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.config.AssociationConfig;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.Equipments;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.races.Race;
import orion.mmorpg.utils.Namespaces;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;
import orion.mmorpg.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class MenuGui extends ChestGui {
    Player p;
    OrionPlayer orionPlayer;
    StaticPane menuPane = new StaticPane(3, 0, 6, 4);

    public MenuGui(Player p, OrionPlayer orionPlayer) {
        super(4, ChatColor.GREEN + "Menu");
        this.p = p;
        this.orionPlayer = orionPlayer;

        if (orionPlayer.getRace() != null) {
            this.addPane(this.orionPlayer.getEquipements().getEquipmentPane(this));
            this.addPane(getMenuPane());
            this.show(p);
        } else {
            new RaceGui(p, orionPlayer);
        }

        this.setOnBottomClick(e->{
            if (e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY))
                e.setCancelled(true);
        });
    }

    public StaticPane getMenuPane() {
        menuPane.setOnClick(e->e.setCancelled(true));
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        ItemMeta headMeta = head.getItemMeta();
        headMeta.setDisplayName(ChatColor.YELLOW + orionPlayer.getRace().getName() + " " + p.getName());
        LinkedList<String> infos = new LinkedList<>();
        if (orionPlayer.getMainSupport() != null)
            infos.add(ChatColor.YELLOW + orionPlayer.getMainSupport().getName());
        infos.addAll(orionPlayer.getStats().statsAsLore());
        infos.add(ChatColor.WHITE + "Appuyez pour ouvrir le menu des stats");
        headMeta.setLore(infos);
        head.setItemMeta(headMeta);

        menuPane.addItem(new GuiItem(head, e->new StatsGui(p, orionPlayer).show(p)), 2, 0);
        menuPane.addItem(new GuiItem(ItemList.JOB_PLACEHOLDER.generateItemStack(1), e->new JobGui(p, orionPlayer).show(p)), 3, 0);
        /*if (p.hasPermission("*")) {
            menuPane.addItem(new GuiItem(new ItemStack(Material.BARRIER), e -> {
                if (orionPlayer.getPlayerMode() == Mode.PLAYER)
                    if (p.hasPermission("builder"))
                        orionPlayer.setPlayerMode(Mode.BUILDER);

                if (orionPlayer.getPlayerMode() == Mode.BUILDER)
                    if (p.hasPermission("admin"))
                        orionPlayer.setPlayerMode(Mode.ADMIN);
                    else
                        orionPlayer.setPlayerMode(Mode.PLAYER);

                if (orionPlayer.getPlayerMode() == Mode.ADMIN)
                    orionPlayer.setPlayerMode(Mode.PLAYER);
                Utils.sendInfoMessage(p, "Vous êtes en mode : " + orionPlayer.getPlayerMode());
            }), 3, 0);
        }*/

        if(orionPlayer.getParty() != null) {
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Cliquez pour ouvrir le menu d'équipe");
            lore.add(ChatColor.GRAY + "Clique droit pour quitter l'équipe");
            for(OrionPlayer member : orionPlayer.getParty().getMembers())
                lore.add(ChatColor.YELLOW + Bukkit.getPlayer(member.getUniqueId()).getName() + " : "
                        + Stats.StatsInfo.VITALITY.getColor() + (int) member.getHealth()
                        + ChatColor.YELLOW +  "/"
                        + Stats.StatsInfo.VITALITY.getColor()  + member.getStats().get(Stats.StatsInfo.VITALITY) + " "
                        + Stats.StatsInfo.VITALITY.getCharacter());

            menuPane.addItem(new GuiItem(BasicItem.generateTemplateBasicItem(Material.CARROT_ON_A_STICK, "Menu d'équipe", AssociationConfig.getModel("gui/party_placeholder"), lore, Rarity.COMMON).generateItem(null, 1), e-> {
                if(e.getClick().equals(ClickType.LEFT))
                    new PartyGui( p, orionPlayer).show(p);
                else if(e.getClick().equals(ClickType.RIGHT)) {
                    orionPlayer.getParty().removeMember(orionPlayer);
                    Utils.sendInfoMessage(p, "Vous avez quitté l'équipe");
                }
            }), 1,0);
        } else {
            menuPane.addItem(new GuiItem(BasicItem.generateTemplateBasicItem(Material.CARROT_ON_A_STICK, "Menu d'équipe", AssociationConfig.getModel("gui/party_placeholder"), Collections.singletonList("Cliquez pour ouvrir le menu d'équipe"), Rarity.COMMON).generateItem(null, 1), e-> {
                Mmorpg.INSTANCE.getPartyManager().createParty().addMember(p, orionPlayer);
                new PartyGui( p, orionPlayer).show(p);
            }), 1,0);
        }


        menuPane.addItem(new GuiItem(ItemList.PORTAL_STONE.generateItemStack(1), e -> {
            new PortalStoneGui(p, orionPlayer).show(p);
        }), 2, 1);

        menuPane.addItem(new GuiItem(ItemList.WIKI_BOOK_PLACEHOLDER.generateItemStack(1), e -> {
            new WikiGui(Mmorpg.INSTANCE).show(p);
        }), 1, 1);
        menuPane.addItem(new GuiItem(BasicItem.generateTemplateBasicItem(Material.GOLD_INGOT, "Currencies", 0, orionPlayer.getCurrencies().asLore(), Rarity.COMMON).generateItem(null, 1), e -> {

        }), 3, 1);
        /*
        menuPane.addItem(new GuiItem(new ItemStack(Material.ARROW), e -> {
            Utils.sendErrorMessage(p, "Ce menu n'est pas encore disponible");
            //new QuiverGui(Mmorpg.INSTANCE, p, orionPlayer).show(p);
        }), 3, 1);
        */

        menuPane.addItem(new GuiItem(BasicItem.generateTemplateBasicItem(Material.PAPER, "Menu des sorts", 1, Collections.singletonList("Ouvre le menu des sorts"), Rarity.COMMON).generateItem(null, 1), e -> new SpellGui(p, orionPlayer).show(p)), 2,2);
        menuPane.addItem(new GuiItem(ItemList.SETTINGS_PLACEHOLDER.generateItemStack(1), e -> new SlotGui(Mmorpg.INSTANCE, p, orionPlayer).show(p)), 2,3);

        return menuPane;
    }

    public void show() {
        this.show(p);
    }
}
