package orion.mmorpg.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import dev.dbassett.skullcreator.SkullCreator;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.config.AssociationConfig;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class PartyGui extends ChestGui {
    Player player;
    OrionPlayer orionPlayer;

    public PartyGui(Player player, OrionPlayer orionPlayer) {
        super(2, "Menu de la party");
        this.player = player;
        this.orionPlayer = orionPlayer;

        this.addPane(getPrimaryPane());
    }

    private OutlinePane getPrimaryPane() {
        OutlinePane primaryPane = new OutlinePane(0, 0, 9, 1);
        BasicItem item = BasicItem.generateTemplateBasicItem(Material.CARROT_ON_A_STICK,
                ChatColor.YELLOW + "Ajouter un membre", AssociationConfig.getModel("gui/party_manager"),
                Collections.singletonList(ChatColor.GRAY + "Cliquez pour ajouter un membre"), Rarity.COMMON);
        primaryPane.addItem(new GuiItem(item.generateItem(null, 1), event -> {
            new AnvilGUI.Builder().onClick((slot, snapshot) -> {
                if (slot != AnvilGUI.Slot.OUTPUT)
                    return List.of(AnvilGUI.ResponseAction.close());
                String text = snapshot.getText();
                if (Bukkit.getPlayer(text) != null) {
                    orionPlayer.getParty().addMember(Bukkit.getPlayer(text),
                            Mmorpg.INSTANCE.getPlayerManager().getPlayer(Bukkit.getPlayer(text).getUniqueId()));
                    player.closeInventory();
                    player.sendMessage(ChatColor.GREEN + "Vous avez ajouté " + Bukkit.getPlayer(text).getDisplayName()
                            + " à votre party");
                } else {
                    player.sendMessage(ChatColor.RED + "Ce joueur n'existe pas");
                }
                return List.of(AnvilGUI.ResponseAction.close());
            }).plugin(Mmorpg.INSTANCE).text("Nom du joueur").title("Ajouter un joueur").open(player);
        }));
        orionPlayer.getParty().getMembers().forEach(member -> {
            primaryPane.addItem(new PartyMemberItem(member, event -> {
                event.setCancelled(true);
                if (event.getClick().isRightClick()
                        && orionPlayer.getParty().getMembers().toArray()[0].equals(orionPlayer)) {
                    orionPlayer.getParty().removeMember(member);
                    player.closeInventory();
                    player.sendMessage(ChatColor.GREEN + "Vous avez kick "
                            + Bukkit.getPlayer(member.getUniqueId()).getDisplayName());
                }
            }));
        });
        return primaryPane;
    }

    private class PartyMemberItem extends GuiItem {
        OrionPlayer member;

        public PartyMemberItem(OrionPlayer member, @NotNull Consumer<InventoryClickEvent> action) {
            super(SkullCreator.itemFromUuid(member.getUniqueId()), action);
            this.member = member;
            ItemMeta meta = getItem().getItemMeta();
            meta.setDisplayName(Bukkit.getPlayer(member.getUniqueId()).getDisplayName());
            ArrayList<String> lore = new ArrayList<>();

            lore.add(ChatColor.YELLOW + "Niveau: " + member.getLevel());
            lore.addAll(member.getStats().statsAsLore());
            lore.add(ChatColor.YELLOW
                    + Utils.locationToReadableString(Bukkit.getPlayer(member.getUniqueId()).getLocation()));
            lore.add(ChatColor.WHITE + "Clique droit pour kick");
            meta.setLore(lore);
            getItem().setItemMeta(meta);
        }
    }
}
