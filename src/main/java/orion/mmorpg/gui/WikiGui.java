package orion.mmorpg.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.sample.*;
import orion.mmorpg.managers.ItemManager;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.SpellList;
import orion.mmorpg.utils.Utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class WikiGui extends ChestGui {
    final List<String> hiddenItems = Arrays.asList("TEST_ITEM", "ADMIN_SWORD", "STARSET_HELMET", "STARSET_CHESTPLATE",
            "STARSET_LEGGINGS", "STARSET_BOOTS", "GIFT", "INFINITE_FIREWORK", "CELESTIAL_SERUM");
    final List<SpellList> hiddenSpells = Arrays.asList(SpellList.TEST_SPELL, SpellList.GOAT_HUNGER);
    PaginatedPane mainPane = new PaginatedPane(0, 0, 9, 5);
    List<GuiItem> populateList = new LinkedList<>();
    Mmorpg mmorpg;
    ItemManager itemManager;

    public WikiGui(Mmorpg mmorpg) {
        super(6, "Wiki");
        this.mmorpg = mmorpg;
        this.itemManager = mmorpg.getItemManager();

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
        PaginatedPane supportPane = new PaginatedPane(0, 0, 9, 5);
        for (SpellList s : SpellList.values()) {
            if (hiddenSpells.contains(s))
                continue;
            populateList.add(new GuiItem(s.getItem(), (event) -> {
                if (!event.getWhoClicked().hasPermission("orion.wiki.give.spell"))
                    return;
                OrionPlayer p = mmorpg.getPlayerManager().getPlayer(event.getWhoClicked().getUniqueId());
                SpellList.learnSpell(p, s.getSpellClass(), 200);
                Utils.sendInfoMessage(event.getWhoClicked(), "Apprentissage du sort : " + s.getName());
            }));
        }
        supportPane.populateWithGuiItems(populateList);
        return supportPane;
    }

    public PaginatedPane getSampleItem() {
        populateList.clear();
        PaginatedPane samplePane = new PaginatedPane(0, 0, 9, 5);
        for (String item : Mmorpg.INSTANCE.getItemManager().getItems().keySet()) {
            if (!Mmorpg.INSTANCE.getItemManager().getItem(item).equals(BasicItem.class))
                continue;
            if (hiddenItems.contains(item))
                continue;
            BasicItem basicItem = Mmorpg.INSTANCE.getItemManager().createItem(item);
            populateList.add(new GuiItem(basicItem.generateItem(null, 1), (event) -> {
                if (!event.getWhoClicked().hasPermission("orion.wiki.give.item"))
                    return;
                event.getWhoClicked().getInventory().addItem(basicItem.generateItem(null, 1));
                Utils.sendInfoMessage(event.getWhoClicked(), "Give Item : " + basicItem.getNAME());
            }));
        }

        samplePane.populateWithGuiItems(populateList);
        return samplePane;
    }

    public PaginatedPane getWeaponPane() {
        populateList.clear();
        PaginatedPane weaponPane = new PaginatedPane(0, 0, 9, 5);
        for (String item : itemManager.getItems().keySet()) {
            if (!BasicMeleeWeapon.class.isAssignableFrom(itemManager.getItem(item))
                    && !BasicMagicWeapon.class.isAssignableFrom(itemManager.getItem(item))
                    && !BasicRangedWeapon.class.isAssignableFrom(itemManager.getItem(item)))
                continue;

            if (hiddenItems.contains(item))
                continue;
            BasicItem basicItem = itemManager.createItem(item);
            populateList.add(new GuiItem(basicItem.generateItem(null, 1), (event) -> {
                if (!event.getWhoClicked().hasPermission("orion.wiki.give.weapons"))
                    return;
                event.getWhoClicked().getInventory().addItem(basicItem.generateItem(null, 1));
                Utils.sendInfoMessage(event.getWhoClicked(), "Give Item : " + basicItem.getNAME());
            }));
        }

        weaponPane.populateWithGuiItems(populateList);
        return weaponPane;
    }

    public PaginatedPane getArmorPane() {
        populateList.clear();
        PaginatedPane armorPane = new PaginatedPane(0, 0, 9, 5);
        for (String item : itemManager.getItems().keySet()) {
            if (!BasicArmorItem.class.isAssignableFrom(itemManager.getItem(item)))
                continue;
            if (hiddenItems.contains(item))
                continue;
            BasicItem basicItem = itemManager.createItem(item);
            populateList.add(new GuiItem(basicItem.generateItem(null, 1), (event) -> {
                if (!event.getWhoClicked().hasPermission("orion.wiki.give.armor"))
                    return;
                event.getWhoClicked().getInventory().addItem(basicItem.generateItem(null, 1));
                Utils.sendInfoMessage(event.getWhoClicked(), "Give Item : " + basicItem.getNAME());
            }));
        }

        armorPane.populateWithGuiItems(populateList);
        return armorPane;
    }

    public StaticPane getNavigationPane() {
        StaticPane navigationPane = new StaticPane(0, 5, 9, 1);

        navigationPane.addItem(new GuiItem(itemManager.createItemstack("LEFT_BUTTON_PLACEHOLDER", 1), e -> {
            PaginatedPane currentPane = ((PaginatedPane) mainPane.getPanes(mainPane.getPage()));
            if (currentPane.getPage() > 0)
                currentPane.setPage(currentPane.getPage() - 1);
            this.update();
        }), 0, 0);

        navigationPane.addItem(new GuiItem(itemManager.createItemstack("RIGHT_BUTTON_PLACEHOLDER", 1), e -> {
            PaginatedPane currentPane = ((PaginatedPane) mainPane.getPanes(mainPane.getPage()));
            if (currentPane.getPage() < currentPane.getPages() - 1)
                currentPane.setPage(currentPane.getPage() + 1);
            this.update();
        }), 8, 0);

        navigationPane.addItem(new GuiItem(new ItemStack(Material.REDSTONE), e -> {
            mainPane.setPage(0);
            this.update();
        }), 2, 0);
        navigationPane.addItem(new GuiItem(new ItemStack(Material.STICK), e -> {
            mainPane.setPage(1);
            this.update();
        }), 3, 0);
        navigationPane.addItem(new GuiItem(new ItemStack(Material.IRON_SWORD), e -> {
            mainPane.setPage(2);
            this.update();
        }), 4, 0);
        navigationPane.addItem(new GuiItem(new ItemStack(Material.IRON_CHESTPLATE), e -> {
            mainPane.setPage(3);
            this.update();
        }), 5, 0);

        return navigationPane;
    }
}
