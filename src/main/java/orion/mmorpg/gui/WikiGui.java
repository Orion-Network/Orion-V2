package orion.mmorpg.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.sample.*;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.SpellList;
import orion.mmorpg.utils.Utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class WikiGui extends ChestGui {
    PaginatedPane mainPane = new PaginatedPane(0,0,9,5);

    final List<ItemList> hiddenItems = Arrays.asList(ItemList.TEST_ITEM, ItemList.ADMIN_SWORD, ItemList.STARSET_HELMET, ItemList.STARSET_CHESTPLATE, ItemList.STARSET_LEGGINGS, ItemList.STARSET_BOOTS);
    final List<SpellList> hiddenSpells = Arrays.asList(SpellList.TEST_SPELL);

    List<GuiItem> populateList = new LinkedList<>();
    Mmorpg mmorpg;
    public WikiGui(Mmorpg mmorpg) {
        super(6, "Wiki");
        this.mmorpg = mmorpg;

        this.setOnTopClick(e -> e.setCancelled(true));

        mainPane.addPane(0, getSpellPane());
        mainPane.addPane(1, getSampleItem());
        mainPane.addPane(2, getWeaponPane());
        mainPane.addPane(3, getArmorPane());

        this.addPane(mainPane);

        this.addPane(getNavigationPane());
    }

    public PaginatedPane getSpellPane() {
        populateList.clear();
        PaginatedPane supportPane = new PaginatedPane(0,0,9,5);
        for(SpellList s : SpellList.values()) {
            if (hiddenSpells.contains(s)) continue;
            populateList.add(new GuiItem(s.getItem(), (event) -> {
                OrionPlayer p = mmorpg.getPlayerManager().getPlayer(event.getWhoClicked().getUniqueId());
                SpellList.learnSpell(p, s.name());
                Utils.sendInfoMessage(event.getWhoClicked(), "Apprentissage du sort : " + s.getName());
            }));
        }
        supportPane.populateWithGuiItems(populateList);
        return supportPane;
    }

    public PaginatedPane getSampleItem() {
        populateList.clear();
        PaginatedPane samplePane = new PaginatedPane(0,0,9,5);
        for(ItemList item : ItemList.values()) {
            if(item.getItemClass() != BasicItem.class) continue;
            if(hiddenItems.contains(item)) continue;
            populateList.add(new GuiItem(item.getProcessedItem(1), (event) -> {
                event.getWhoClicked().getInventory().addItem(item.getProcessedItem(1));
                Utils.sendInfoMessage(event.getWhoClicked(), "Give Item : " + item.getItem().getNAME());
            }));
        }

        samplePane.populateWithGuiItems(populateList);
        return samplePane;
    }

    public PaginatedPane getWeaponPane() {
        populateList.clear();
        PaginatedPane weaponPane = new PaginatedPane(0,0,9,5);
        for(ItemList item : ItemList.values()) {
            if(item.getItemClass() != BasicMeleeWeapon.class && item.getItemClass() != BasicRangedWeapon.class && item.getItemClass() != BasicMagicWeapon.class) continue;
            if(hiddenItems.contains(item)) continue;
            populateList.add(new GuiItem(item.getProcessedItem(1), (event) -> {
                event.getWhoClicked().getInventory().addItem(item.getProcessedItem(1));
                Utils.sendInfoMessage(event.getWhoClicked(), "Give Item : " + item.getItem().getNAME());
            }));
        }

        weaponPane.populateWithGuiItems(populateList);
        return weaponPane;
    }

    public PaginatedPane getArmorPane() {
        populateList.clear();
        PaginatedPane armorPane = new PaginatedPane(0,0,9,5);
        for(ItemList item : ItemList.values()) {
            if(item.getItemClass() != BasicArmorItem.class) continue;
            if(hiddenItems.contains(item)) continue;
            populateList.add(new GuiItem(item.getProcessedItem(1), (event) -> {
                event.getWhoClicked().getInventory().addItem(item.getProcessedItem(1));
                Utils.sendInfoMessage(event.getWhoClicked(), "Give Item : " + item.getItem().getNAME());
            }));
        }

        armorPane.populateWithGuiItems(populateList);
        return armorPane;
    }

    public StaticPane getNavigationPane() {
        StaticPane navigationPane = new StaticPane(0,5,9,1);

        navigationPane.addItem(new GuiItem(new ItemStack(Material.GREEN_WOOL), e -> {
            PaginatedPane currentPane = ((PaginatedPane)mainPane.getPanes(mainPane.getPage()));
            if(currentPane.getPage() > 0)
                currentPane.setPage(currentPane.getPage()-1);
            this.update();
        }), 0,0);

        navigationPane.addItem(new GuiItem(new ItemStack(Material.GREEN_WOOL), e -> {
            PaginatedPane currentPane = ((PaginatedPane)mainPane.getPanes(mainPane.getPage()));
            if(currentPane.getPage() < currentPane.getPages() - 1)
                currentPane.setPage(currentPane.getPage() + 1);
            this.update();
        }),8,0);

        navigationPane.addItem(new GuiItem(new ItemStack(Material.REDSTONE), e -> {
            mainPane.setPage(0);
            this.update();
        }),2,0);
        navigationPane.addItem(new GuiItem(new ItemStack(Material.STICK), e -> {
            mainPane.setPage(1);
            this.update();
        }),3,0);
        navigationPane.addItem(new GuiItem(new ItemStack(Material.IRON_SWORD), e -> {
            mainPane.setPage(2);
            this.update();
        }),4,0);
        navigationPane.addItem(new GuiItem(new ItemStack(Material.IRON_CHESTPLATE), e -> {
            mainPane.setPage(3);
            this.update();
        }),5,0);


        return navigationPane;
    }
}
