package orion.mmorpg.gui.editors;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.wesjd.anvilgui.AnvilGUI;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.managers.WorldManager;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.SpellList;
import orion.mmorpg.players.abilities.Support;
import orion.mmorpg.players.abilities.SupportList;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerEditor extends ChestGui {

    private final Player ownerPlayer;
    private final OrionPlayer ownerOrionPlayer;
    private final Player targetPlayer;
    private final OrionPlayer targetOrionPlayer;
    public PlayerEditor(Player ownerPlayer, OrionPlayer ownerOrionPlayer, Player targetPlayer, OrionPlayer targetOrionPlayer) {
        super(3, "Editing " + targetPlayer.getName());
        this.ownerPlayer = ownerPlayer;
        this.ownerOrionPlayer = ownerOrionPlayer;
        this.targetPlayer = targetPlayer;
        this.targetOrionPlayer = targetOrionPlayer;

        this.addPane(getPlayerPane());
        this.setOnGlobalClick(event -> event.setCancelled(true));
        this.show(ownerPlayer);
    }

    private StaticPane getPlayerPane() {
        StaticPane pane = new StaticPane(0,0, 9,3);
        pane.addItem(new GuiItem(new ItemStack(Material.AMETHYST_SHARD), event -> {
            BasicItem item = BasicItem.generateTemplateBasicItem(Material.AMETHYST_SHARD, "Nom de la pierre", 0, null, Rarity.COMMON);
            ItemStack buildItem = item.generateItem(null, 1);
            ItemMeta meta = buildItem.getItemMeta();
            //TODO Rework for new Portal Stones
            List<String> removableStones = targetOrionPlayer.getPortalStones().stream().map(string -> ChatColor.YELLOW + string).collect(Collectors.toCollection(ArrayList::new));
            ArrayList<WorldManager.PortalStone> portalStones = new ArrayList<>(Mmorpg.INSTANCE.getWorldManager().getPortalStones().values());
            removableStones.addAll(portalStones.stream().map(t -> ChatColor.RED + t.getTitle()).filter(id ->
                    (!targetOrionPlayer.getPortalStones().contains(ChatColor.stripColor(id)))).toList());

            meta.setLore(removableStones);
            buildItem.setItemMeta(meta);

            new AnvilGUI.Builder()
                    .plugin(Mmorpg.INSTANCE)
                    .title("Editer les pierres de " + targetPlayer.getName())
                    .itemLeft(buildItem)
                    .onComplete((player, text) -> {
                        text = text.toLowerCase();
                        if(Mmorpg.INSTANCE.getWorldManager().getPortalStones().containsKey(text)) {
                            if(!targetOrionPlayer.getPortalStones().contains(text)) {
                                targetOrionPlayer.addPortalStone(text);
                                Utils.sendInfoMessage(ownerPlayer, "Pierre ajoutée");
                                return AnvilGUI.Response.close();
                            } else {
                                targetOrionPlayer.removePortalStone(text);
                                Utils.sendInfoMessage(ownerPlayer, "Pierre enlevé");
                                return AnvilGUI.Response.close();
                            }
                        }
                        return AnvilGUI.Response.text("Pierre non valide");
                    })
                    .open(ownerPlayer);
        }), 0,0);

        pane.addItem(new GuiItem(new ItemStack(Material.AMETHYST_SHARD), event -> {
            new SpellEditor(ownerPlayer, ownerOrionPlayer, targetPlayer, targetOrionPlayer).show(targetPlayer);
        }), 1,0);

        pane.addItem(new GuiItem(new ItemStack(Material.PAPER), event -> {
            BasicItem item = BasicItem.generateTemplateBasicItem(Material.PAPER, (targetOrionPlayer.getMainSupport() != null ? "Support Actuel : " + targetOrionPlayer.getMainSupport().getName() : "Aucun support"), 3, null, (targetOrionPlayer.getMainSupport() != null ? targetOrionPlayer.getMainSupport().getRank() : Rarity.TRASH));
            ItemStack buildItem = item.generateItem(null, 1);
            ItemMeta meta = buildItem.getItemMeta();
            ArrayList<String> lore = Arrays.stream(SupportList.values()).map(t -> ChatColor.GRAY + t.name()).collect(Collectors.toCollection(ArrayList::new));
            lore.add("Null");
            meta.setLore(lore);
            buildItem.setItemMeta(meta);

            new AnvilGUI.Builder()
                    .plugin(Mmorpg.INSTANCE)
                    .itemLeft(buildItem)
                    .onComplete(((player, text) -> {
                        text = text.toUpperCase();
                        if(EnumUtils.isValidEnum(SupportList.class, text)) {
                            targetOrionPlayer.setMainSupport(SupportList.valueOf(text).getSupport());
                            Utils.sendInfoMessage(ownerPlayer, "Support modifié");
                            return AnvilGUI.Response.close();
                        } else if(text.equalsIgnoreCase("null")) {
                            targetOrionPlayer.setMainSupport((Support) null);
                            Utils.sendInfoMessage(ownerPlayer, "Support supprimé");
                            return AnvilGUI.Response.close();
                        }
                        return AnvilGUI.Response.text("Support invalide");
                    }))
                    .open(ownerPlayer);
        }), 2, 0);

        return pane;
    }

    private class SpellEditor extends ChestGui {
        private Player ownerPlayer;
        private OrionPlayer ownerOrionPlayer;
        private Player targetPlayer;
        private OrionPlayer targetOwnerPlayer;

        List<GuiItem> populateList = new LinkedList<>();

        public SpellEditor(Player ownerPlayer, OrionPlayer ownerOrionPlayer, Player targetPlayer, OrionPlayer targetOwnerPlayer) {
            super(3, "Editer les sorts du joueur");
            this.ownerPlayer = ownerPlayer;
            this.ownerOrionPlayer = ownerOrionPlayer;
            this.targetPlayer = targetPlayer;
            this.targetOwnerPlayer = targetOwnerPlayer;

            this.addPane(getSpellPane());
            this.setOnGlobalClick(e -> e.setCancelled(true));
        }

        public PaginatedPane getSpellPane() {
            PaginatedPane supportPane = new PaginatedPane(0, 0, 9, 5);
            for (SpellList s : SpellList.values()) {
                populateList.add(new GuiItem(s.getItem(), (event) -> {
                    if(event.getClick().equals(ClickType.LEFT)) {
                        new AnvilGUI.Builder().plugin(Mmorpg.INSTANCE)
                                .itemLeft(s.getItem())
                                .title("Ajouter des points de savoir")
                                .onComplete((player, response) -> {
                                    if(!NumberUtils.isNumber(response))
                                        return AnvilGUI.Response.text("Nombre Invalide");
                                    if(!targetOrionPlayer.getKnownSpells().containsKey(s.getSpellClass()))
                                        SpellList.learnSpell(targetOrionPlayer, s.getSpellClass(), 1);
                                    targetOrionPlayer.getKnownSpells().get(s.getSpellClass()).setKnowledge(Integer.parseInt(response));
                                    return AnvilGUI.Response.close();
                                })
                                .open(ownerPlayer);
                    } else if(event.getClick().isRightClick()) {
                        targetOrionPlayer.getKnownSpells().forEach((spellClass, knowledge) -> {
                            if(targetOrionPlayer.getKnownSpells().containsKey(s.getSpellClass())) {
                                targetOrionPlayer.getKnownSpells().remove(s.getSpellClass());
                                return;
                            }
                        });
                        Utils.sendInfoMessage(ownerPlayer, "Le joueur ne connais pas ce sort.");
                        return;
                    }
                }));
            }
            supportPane.populateWithGuiItems(populateList);
            return supportPane;
        }
    }
}
