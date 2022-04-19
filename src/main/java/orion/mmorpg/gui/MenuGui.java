package orion.mmorpg.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.players.Mode;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.races.Race;
import orion.mmorpg.utils.Stats;
import orion.mmorpg.utils.Utils;
import xyz.upperlevel.spigot.book.BookUtil;

import java.util.LinkedList;

public class MenuGui extends ChestGui {
    Player p;
    OrionPlayer orionPlayer;
    StaticPane armorPane = new StaticPane(0, 0, 3, 4);
    GuiItem HELMET_PLACEHOLDER = new GuiItem(ItemList.HELMET_PLACEHOLDER.getProcessedItem(1));
    GuiItem CHESTPLATE_PLACEHOLDER = new GuiItem(ItemList.CHESTPLATE_PLACEHOLDER.getProcessedItem(1));
    GuiItem LEGGINGS_PLACEHOLDER = new GuiItem(ItemList.LEGGINGS_PLACEHOLDER.getProcessedItem(1));
    GuiItem BOOTS_PLACEHOLDER = new GuiItem(ItemList.BOOTS_PLACEHOLDER.getProcessedItem(1));
    StaticPane menuPane = new StaticPane(3, 0, 6, 4);

    public MenuGui(Player p, OrionPlayer orionPlayer) {
        super(4, ChatColor.GREEN + "Menu");
        this.p = p;
        this.orionPlayer = orionPlayer;
        this.setOnTopClick(e -> e.setCancelled(true));

        if (orionPlayer.getRace() != null) {
            this.addPane(getArmorPane());
            this.addPane(getMenuPane());
            this.show(p);
        } else {
            new RaceGui(p, orionPlayer);
        }

        this.setOnBottomClick(e -> {
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();
            if (item == null || item.getType() == Material.AIR)
                return;
            PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();

            if (container.has(NamespacedKey.minecraft("type"), PersistentDataType.STRING)) {
                if (container.get(NamespacedKey.minecraft("type"), PersistentDataType.STRING).equalsIgnoreCase("armor")) {
                    if (!((BasicArmorItem) ItemList.getItemFromItemstack(e.getCurrentItem())).onEquip(p, orionPlayer, item))
                        return;
                    Stats stats = Mmorpg.getOrionGson().getGSON().fromJson(container.get(NamespacedKey.minecraft("stats"), PersistentDataType.STRING), Stats.class);
                    if (container.get(NamespacedKey.minecraft("armor_type"), PersistentDataType.STRING).equalsIgnoreCase("HELMET")) {
                        if (orionPlayer.getEquipements()[3] != null) {
                            Utils.sendErrorMessage(p, "Unequip helmet first");
                            return;
                        }

                        orionPlayer.getEquipements()[3] = item;
                        Utils.sendInfoMessage(p, "You equipped an helmet");

                        getArmorPane().removeItem(1, 0);
                        getMenuPane();
                        getArmorPane().addItem(new GuiItem(item), 1, 0);

                        e.setCurrentItem(null);
                    } else if (container.get(NamespacedKey.minecraft("armor_type"), PersistentDataType.STRING).equalsIgnoreCase("CHESTPLATE")) {
                        if (orionPlayer.getEquipements()[2] != null) {
                            Utils.sendErrorMessage(p, "Unequip chestplate first");
                            return;
                        }

                        if (item.getType().equals(Material.ELYTRA) && p.getInventory().getChestplate() == null) {
                            if (!orionPlayer.getRace().getSpecies().contains(Race.spec.ELYTRA)) {
                                Utils.sendErrorMessage(p, "Votre race ne peut pas equiper d'ailes");
                                return;
                            }
                            Utils.sendErrorMessage(p, "Vous ne pouvez pas avoir de plastron cosmétique et des ailes");
                            return;
                        }

                        orionPlayer.getEquipements()[2] = item;
                        Utils.sendInfoMessage(p, "You equipped an chestplate");

                        getArmorPane().removeItem(1, 1);
                        getMenuPane();
                        getArmorPane().addItem(new GuiItem(item), 1, 1);

                        e.setCurrentItem(null);
                    } else if (container.get(NamespacedKey.minecraft("armor_type"), PersistentDataType.STRING).equalsIgnoreCase("LEGGINGS")) {
                        if (orionPlayer.getEquipements()[1] != null) {
                            Utils.sendErrorMessage(p, "Unequip leggings first");
                            return;
                        }

                        orionPlayer.getEquipements()[1] = item;
                        Utils.sendInfoMessage(p, "You equipped an leggings");

                        getArmorPane().removeItem(1, 2);
                        getMenuPane();
                        getArmorPane().addItem(new GuiItem(item), 1, 2);

                        e.setCurrentItem(null);
                    } else if (container.get(NamespacedKey.minecraft("armor_type"), PersistentDataType.STRING).equalsIgnoreCase("BOOTS")) {
                        if (orionPlayer.getEquipements()[0] != null) {
                            Utils.sendErrorMessage(p, "Unequip boots first");
                            return;
                        }

                        orionPlayer.getEquipements()[0] = item;
                        Utils.sendInfoMessage(p, "You equipped a boots");

                        getArmorPane().removeItem(1, 3);
                        getMenuPane();
                        getArmorPane().addItem(new GuiItem(item), 1, 3);

                        e.setCurrentItem(null);
                    } else if (container.get(NamespacedKey.minecraft("armor_type"), PersistentDataType.STRING).equalsIgnoreCase("WINGS")) {
                        if (!orionPlayer.getRace().getSpecies().contains(Race.spec.ELYTRA)) {
                            Utils.sendErrorMessage(p, "Votre race ne peut pas equiper d'ailes");
                            return;
                        }

                        if (orionPlayer.getEquipements()[2] != null) {
                            Utils.sendErrorMessage(p, "Unequip chestplate first");
                            return;
                        }

                        orionPlayer.getEquipements()[2] = item;
                        Utils.sendInfoMessage(p, "You equipped an Wings");

                        getArmorPane().removeItem(1, 1);
                        getMenuPane();
                        getArmorPane().addItem(new GuiItem(item), 1, 1);

                        e.setCurrentItem(null);
                    }
                }
            }
        });
    }

    public StaticPane getArmorPane() {
        for (int i = 0; i < 4; i++) {
            if (orionPlayer.getEquipements()[i] == null) {
                if (i == 0)
                    armorPane.addItem(BOOTS_PLACEHOLDER, 1, 3);
                if (i == 1)
                    armorPane.addItem(LEGGINGS_PLACEHOLDER, 1, 2);
                if (i == 2)
                    armorPane.addItem(CHESTPLATE_PLACEHOLDER, 1, 1);
                if (i == 3)
                    armorPane.addItem(HELMET_PLACEHOLDER, 1, 0);
            } else {
                armorPane.addItem(new GuiItem(orionPlayer.getEquipements()[i].clone()), 1, 3 - i);
            }
        }
        armorPane.setOnClick(e -> {
            if (e.getCurrentItem().getType() != Material.AIR && e.getCurrentItem().getType() != Material.CARROT_ON_A_STICK) {
                ItemStack item = e.getCurrentItem();
                PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
                if (!((BasicArmorItem) ItemList.getItemFromItemstack(e.getCurrentItem())).onUnequip(p, orionPlayer, item))
                    return;
                Stats stats = Mmorpg.getOrionGson().getGSON().fromJson(container.get(NamespacedKey.minecraft("stats"), PersistentDataType.STRING), Stats.class);
                if (container.get(NamespacedKey.minecraft("armor_type"), PersistentDataType.STRING).equalsIgnoreCase("HELMET")) {
                    orionPlayer.getEquipements()[3] = null;
                    Utils.sendInfoMessage(p, "You removed an helmet");

                    getArmorPane().removeItem(1, 0);
                    p.getInventory().addItem(item);
                    getMenuPane();
                    getArmorPane().addItem(HELMET_PLACEHOLDER, 1, 0);
                } else if (container.get(NamespacedKey.minecraft("armor_type"), PersistentDataType.STRING).equalsIgnoreCase("CHESTPLATE")) {
                    orionPlayer.getEquipements()[2] = null;
                    Utils.sendInfoMessage(p, "You removed an chestplate");

                    getArmorPane().removeItem(1, 1);
                    p.getInventory().addItem(item);
                    getMenuPane();
                    getArmorPane().addItem(CHESTPLATE_PLACEHOLDER, 1, 1);
                } else if (container.get(NamespacedKey.minecraft("armor_type"), PersistentDataType.STRING).equalsIgnoreCase("LEGGINGS")) {
                    orionPlayer.getEquipements()[1] = null;
                    Utils.sendInfoMessage(p, "You removed an leggings");

                    getArmorPane().removeItem(1, 2);
                    p.getInventory().addItem(item);
                    getMenuPane();
                    getArmorPane().addItem(LEGGINGS_PLACEHOLDER, 1, 2);
                } else if (container.get(NamespacedKey.minecraft("armor_type"), PersistentDataType.STRING).equalsIgnoreCase("BOOTS")) {
                    orionPlayer.getEquipements()[0] = null;
                    Utils.sendInfoMessage(p, "You removed an boots");

                    getArmorPane().removeItem(1, 3);
                    p.getInventory().addItem(item);
                    getMenuPane();
                    getArmorPane().addItem(HELMET_PLACEHOLDER, 1, 3);
                } else if (container.get(NamespacedKey.minecraft("armor_type"), PersistentDataType.STRING).equalsIgnoreCase("WINGS")) {
                    orionPlayer.getEquipements()[2] = null;
                    Utils.sendInfoMessage(p, "You removed an wings");

                    getArmorPane().removeItem(1, 1);
                    p.getInventory().addItem(item);
                    getMenuPane();
                    getArmorPane().addItem(CHESTPLATE_PLACEHOLDER, 1, 1);
                }
            }
        });
        this.update();
        return armorPane;
    }

    public StaticPane getMenuPane() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        ItemMeta headMeta = head.getItemMeta();
        headMeta.setDisplayName(ChatColor.YELLOW + orionPlayer.getRace().getName() + " " + p.getName());
        LinkedList<String> infos = new LinkedList<>();
        if (orionPlayer.getSupport() != null)
            infos.add(ChatColor.YELLOW + orionPlayer.getSupport().getName());
        infos.addAll(orionPlayer.getStats().statsAsLore());
        infos.add(ChatColor.WHITE + "Appuyez pour ouvrir le menu des stats");
        headMeta.setLore(infos);
        head.setItemMeta(headMeta);

        menuPane.addItem(new GuiItem(head, e->new StatsGui(p, orionPlayer).show(p)), 2, 0);
        if (p.hasPermission("*")) {
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
        }
        ItemStack book = generateGuideBook();
        menuPane.addItem(new GuiItem(book, e-> {
            BookUtil.openPlayer(p, book);
        }), 1,0);

        menuPane.addItem(new GuiItem(ItemList.PORTAL_STONE.getProcessedItem(1), e -> {
            new PortalStoneGui(p, orionPlayer).show(p);
        }), 2, 1);

        menuPane.addItem(new GuiItem(ItemList.WIKI_BOOK_PLACEHOLDER.getProcessedItem(1), e -> {
            new WikiGui(Mmorpg.INSTANCE).show(p);
        }), 1, 1);
        menuPane.addItem(new GuiItem(new ItemStack(Material.ARROW), e -> {
            new QuiverGui(Mmorpg.INSTANCE, p, orionPlayer).show(p);
        }), 3, 1);
        menuPane.addItem(new GuiItem(ItemList.SETTINGS_PLACEHOLDER.getProcessedItem(1), e -> new SlotGui(Mmorpg.INSTANCE, p, orionPlayer).show(p)), 2,2);

        return menuPane;
    }

    public void show() {
        this.show(p);
    }

    private ItemStack generateGuideBook() {
        return BookUtil.writtenBook().author("GameMaster").title(ChatColor.GREEN + "Guide").pages(
                new BookUtil.PageBuilder()
                        .add("Alpha 0.1")
                        .build()
        ).build();
    }
}
