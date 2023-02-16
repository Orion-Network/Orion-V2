package orion.mmorpg.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.Equipments;
import orion.mmorpg.players.Mode;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.races.Race;
import orion.mmorpg.utils.Namespaces;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Utils;
import xyz.upperlevel.spigot.book.BookUtil;

import java.util.Collections;
import java.util.LinkedList;

public class MenuGui extends ChestGui {
    // TODO Re write equipping message in french and cosmetic message
    Player p;
    OrionPlayer orionPlayer;
    StaticPane armorPane = new StaticPane(0, 0, 3, 4);
    GuiItem HELMET_PLACEHOLDER = new GuiItem(ItemList.HELMET_PLACEHOLDER.generateItemStack(1));
    GuiItem CHESTPLATE_PLACEHOLDER = new GuiItem(ItemList.CHESTPLATE_PLACEHOLDER.generateItemStack(1));
    GuiItem LEGGINGS_PLACEHOLDER = new GuiItem(ItemList.LEGGINGS_PLACEHOLDER.generateItemStack(1));
    GuiItem BOOTS_PLACEHOLDER = new GuiItem(ItemList.BOOTS_PLACEHOLDER.generateItemStack(1));
    GuiItem RING_PLACEHOLDER = new GuiItem(ItemList.RING_PLACEHOLDER.generateItemStack(1));
    GuiItem BELT_PLACEHOLDER = new GuiItem(ItemList.BELT_PLACEHOLDER.generateItemStack(1));

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

            if (container.has(Namespaces.type, PersistentDataType.STRING)) {
                if (container.get(Namespaces.type, PersistentDataType.STRING).equalsIgnoreCase("armor")) {
                    if (!((BasicArmorItem) ItemList.getItemFromItemstack(e.getCurrentItem())).onEquip(p, orionPlayer, item))
                        return;
                    BasicArmorItem armorItem = (BasicArmorItem) ItemList.getItemFromItemstack(e.getCurrentItem());
                    boolean isEquip = orionPlayer.getEquipements().addEquipment(armorItem);
                    if (container.get(Namespaces.armor_type, PersistentDataType.STRING).equalsIgnoreCase("HELMET")) {
                        if (!isEquip) {
                            Utils.sendErrorMessage(p, "Unequip helmet first");
                            return;
                        }

                        Utils.sendInfoMessage(p, "You equipped an helmet");

                        getArmorPane().removeItem(1, 0);
                        getMenuPane();
                        getArmorPane().addItem(new GuiItem(item), 1, 0);

                        e.setCurrentItem(null);
                    } else if (container.get(Namespaces.armor_type, PersistentDataType.STRING).equalsIgnoreCase("CHESTPLATE")) {
                        if (!isEquip) {
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

                        Utils.sendInfoMessage(p, "You equipped an chestplate");

                        getArmorPane().removeItem(1, 1);
                        getMenuPane();
                        getArmorPane().addItem(new GuiItem(item), 1, 1);

                        e.setCurrentItem(null);
                    } else if (container.get(Namespaces.armor_type, PersistentDataType.STRING).equalsIgnoreCase("LEGGINGS")) {
                        if (!isEquip) {
                            Utils.sendErrorMessage(p, "Unequip leggings first");
                            return;
                        }

                        Utils.sendInfoMessage(p, "You equipped an leggings");

                        getArmorPane().removeItem(1, 2);
                        getMenuPane();
                        getArmorPane().addItem(new GuiItem(item), 1, 2);

                        e.setCurrentItem(null);
                    } else if (container.get(Namespaces.armor_type, PersistentDataType.STRING).equalsIgnoreCase("BOOTS")) {
                        if (!isEquip) {
                            Utils.sendErrorMessage(p, "Unequip boots first");
                            return;
                        }

                        Utils.sendInfoMessage(p, "You equipped a boots");

                        getArmorPane().removeItem(1, 3);
                        getMenuPane();
                        getArmorPane().addItem(new GuiItem(item), 1, 3);

                        e.setCurrentItem(null);
                    } else if (container.get(Namespaces.armor_type, PersistentDataType.STRING).equalsIgnoreCase("WINGS")) {
                        if (!orionPlayer.getRace().getSpecies().contains(Race.spec.ELYTRA)) {
                            Utils.sendErrorMessage(p, "Votre race ne peut pas equiper d'ailes");
                            return;
                        }

                        if (!isEquip) {
                            Utils.sendErrorMessage(p, "Unequip chestplate first");
                            return;
                        }

                        Utils.sendInfoMessage(p, "You equipped an Wings");

                        getArmorPane().removeItem(1, 1);
                        getMenuPane();
                        getArmorPane().addItem(new GuiItem(item), 1, 1);

                        e.setCurrentItem(null);
                    } else if(container.get(Namespaces.armor_type, PersistentDataType.STRING).equalsIgnoreCase("BELT")) {
                        if (!isEquip) {
                            Utils.sendErrorMessage(p, "Unequip Belt first");
                            return;
                        }

                        Utils.sendInfoMessage(p, "You equipped a Belt");

                        getArmorPane().removeItem(2, 2);
                        getMenuPane();
                        getArmorPane().addItem(new GuiItem(item), 2, 2);

                        e.setCurrentItem(null);
                    } else if(container.get(Namespaces.armor_type, PersistentDataType.STRING).equalsIgnoreCase("RING")) {
                        if (!isEquip) {
                            Utils.sendErrorMessage(p, "You need to unequip first or second ring");
                            return;
                        }

                        if (orionPlayer.getEquipements().getEquipmentFromSlot(Equipments.EquipmentSlot.LEFT_RING) == armorItem) {
                            Utils.sendInfoMessage(p, "You equipped a Ring");

                            getArmorPane().removeItem(0, 1);
                            getMenuPane();
                            getArmorPane().addItem(new GuiItem(item), 0, 1);

                            e.setCurrentItem(null);
                        } else if (orionPlayer.getEquipements().getEquipmentFromSlot(Equipments.EquipmentSlot.RIGHT_RING) == armorItem) {
                            Utils.sendInfoMessage(p, "You equipped a Ring");

                            getArmorPane().removeItem(0, 2);
                            getMenuPane();
                            getArmorPane().addItem(new GuiItem(item), 0, 2);

                            e.setCurrentItem(null);
                        }
                    }
                    orionPlayer.updateHealthbar();
                }
            }
        });
    }

    public StaticPane getArmorPane() {
        for (int i = 0; i < 7; i++) {
            if(orionPlayer.getEquipements().getEquipmentFromSlot(Equipments.EquipmentSlot.BOOTS) == null)
                armorPane.addItem(BOOTS_PLACEHOLDER, 1, 3);
            else
                armorPane.addItem(new GuiItem(orionPlayer.getEquipements().getEquipmentFromSlot(Equipments.EquipmentSlot.BOOTS).generateItem(null, 1)), 1, 3);

            if(orionPlayer.getEquipements().getEquipmentFromSlot(Equipments.EquipmentSlot.LEGGINGS) == null)
                armorPane.addItem(LEGGINGS_PLACEHOLDER, 1, 2);
            else
                armorPane.addItem(new GuiItem(orionPlayer.getEquipements().getEquipmentFromSlot(Equipments.EquipmentSlot.LEGGINGS).generateItem(null, 1)), 1, 2);

            if(orionPlayer.getEquipements().getEquipmentFromSlot(Equipments.EquipmentSlot.CHEST_PLATE) == null)
                armorPane.addItem(CHESTPLATE_PLACEHOLDER, 1, 1);
            else
                armorPane.addItem(new GuiItem(orionPlayer.getEquipements().getEquipmentFromSlot(Equipments.EquipmentSlot.CHEST_PLATE).generateItem(null, 1)), 1, 1);

            if(orionPlayer.getEquipements().getEquipmentFromSlot(Equipments.EquipmentSlot.HELMET) == null)
                armorPane.addItem(HELMET_PLACEHOLDER, 1, 0);
            else
                armorPane.addItem(new GuiItem(orionPlayer.getEquipements().getEquipmentFromSlot(Equipments.EquipmentSlot.HELMET).generateItem(null, 1)), 1, 0);

            if(orionPlayer.getEquipements().getEquipmentFromSlot(Equipments.EquipmentSlot.BELT) == null)
                armorPane.addItem(BELT_PLACEHOLDER, 2, 2);
            else
                armorPane.addItem(new GuiItem(orionPlayer.getEquipements().getEquipmentFromSlot(Equipments.EquipmentSlot.BELT).generateItem(null, 1)), 2, 2);

            if(orionPlayer.getEquipements().getEquipmentFromSlot(Equipments.EquipmentSlot.LEFT_RING) == null)
                armorPane.addItem(RING_PLACEHOLDER, 0, 1);
            else
                armorPane.addItem(new GuiItem(orionPlayer.getEquipements().getEquipmentFromSlot(Equipments.EquipmentSlot.LEFT_RING).generateItem(null, 1)), 0, 1);

            if(orionPlayer.getEquipements().getEquipmentFromSlot(Equipments.EquipmentSlot.RIGHT_RING) == null)
                armorPane.addItem(RING_PLACEHOLDER, 0, 2);
            else
                armorPane.addItem(new GuiItem(orionPlayer.getEquipements().getEquipmentFromSlot(Equipments.EquipmentSlot.RIGHT_RING).generateItem(null, 1)), 0, 2);
        }
        armorPane.setOnClick(e -> {
            if (e.getCurrentItem().getType() != Material.AIR && e.getCurrentItem().getType() != Material.CARROT_ON_A_STICK) {
                ItemStack item = e.getCurrentItem();
                PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
                if (!((BasicArmorItem) ItemList.getItemFromItemstack(e.getCurrentItem())).onUnequip(p, orionPlayer, item))
                    return;
                BasicArmorItem armorItem = (BasicArmorItem) ItemList.getItemFromItemstack(e.getCurrentItem());
                if (armorItem.getARMORTYPE().equals(BasicArmorItem.ArmorType.HELMET)) {
                    if(!orionPlayer.getEquipements().removeEquipment(Equipments.EquipmentSlot.HELMET))
                        return;
                    Utils.sendInfoMessage(p, "You removed an helmet");

                    getArmorPane().removeItem(1, 0);
                    getMenuPane();
                    getArmorPane().addItem(HELMET_PLACEHOLDER, 1, 0);
                } else if (armorItem.getARMORTYPE().equals(BasicArmorItem.ArmorType.CHESTPLATE)) {
                    orionPlayer.getEquipements().removeEquipment(Equipments.EquipmentSlot.CHEST_PLATE);
                    Utils.sendInfoMessage(p, "You removed an chestplate");

                    getArmorPane().removeItem(1, 1);
                    getMenuPane();
                    getArmorPane().addItem(CHESTPLATE_PLACEHOLDER, 1, 1);
                } else if (armorItem.getARMORTYPE().equals(BasicArmorItem.ArmorType.LEGGINGS)) {
                    if(!orionPlayer.getEquipements().removeEquipment(Equipments.EquipmentSlot.LEGGINGS))
                        return;
                    Utils.sendInfoMessage(p, "You removed an leggings");

                    getArmorPane().removeItem(1, 2);
                    getMenuPane();
                    getArmorPane().addItem(LEGGINGS_PLACEHOLDER, 1, 2);
                } else if (armorItem.getARMORTYPE().equals(BasicArmorItem.ArmorType.BOOTS)) {
                    orionPlayer.getEquipements().removeEquipment(Equipments.EquipmentSlot.BOOTS);
                    Utils.sendInfoMessage(p, "You removed an boots");

                    getArmorPane().removeItem(1, 3);
                    getMenuPane();
                    getArmorPane().addItem(HELMET_PLACEHOLDER, 1, 3);
                } else if (armorItem.getARMORTYPE().equals(BasicArmorItem.ArmorType.WINGS)) {
                    if(!orionPlayer.getEquipements().removeEquipment(Equipments.EquipmentSlot.CHEST_PLATE))
                        return;
                    Utils.sendInfoMessage(p, "You removed an wings");

                    getArmorPane().removeItem(1, 1);
                    getMenuPane();
                    getArmorPane().addItem(CHESTPLATE_PLACEHOLDER, 1, 1);
                } else if(armorItem.getARMORTYPE().equals(BasicArmorItem.ArmorType.BELT)) {
                    if(!orionPlayer.getEquipements().removeEquipment(Equipments.EquipmentSlot.BELT))
                        return;
                    Utils.sendInfoMessage(p, "You removed an belt");

                    getArmorPane().removeItem(2, 2);
                    getMenuPane();
                    getArmorPane().addItem(BELT_PLACEHOLDER, 2, 2);
                } else if(armorItem.getARMORTYPE().equals(BasicArmorItem.ArmorType.RING)) {
                    int slot = (e.getSlot() == 9 ? 1 : 2);
                    if(slot == 1) {
                        if (!orionPlayer.getEquipements().removeEquipment(Equipments.EquipmentSlot.LEFT_RING))
                            return;
                    } else if (!orionPlayer.getEquipements().removeEquipment(Equipments.EquipmentSlot.RIGHT_RING))
                        return;
                    Utils.sendInfoMessage(p, "You removed a ring");

                    getArmorPane().removeItem(0, slot);
                    getMenuPane();
                    getArmorPane().addItem(RING_PLACEHOLDER, 0, slot);
                }
                orionPlayer.updateHealthbar();
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

        menuPane.addItem(new GuiItem(ItemList.PORTAL_STONE.generateItemStack(1), e -> {
            new PortalStoneGui(p, orionPlayer).show(p);
        }), 2, 1);

        menuPane.addItem(new GuiItem(ItemList.WIKI_BOOK_PLACEHOLDER.generateItemStack(1), e -> {
            new WikiGui(Mmorpg.INSTANCE).show(p);
        }), 1, 1);
        menuPane.addItem(new GuiItem(new ItemStack(Material.ARROW), e -> {
            new QuiverGui(Mmorpg.INSTANCE, p, orionPlayer).show(p);
        }), 3, 1);
        menuPane.addItem(new GuiItem(BasicItem.generateTemplateBasicItem(Material.PAPER, "Menu des sorts", 1, Collections.singletonList("Ouvre le menu des sorts"), Rarity.COMMON).generateItem(null, 1), e -> new SpellGui(p, orionPlayer).show(p)), 2,2);
        menuPane.addItem(new GuiItem(ItemList.SETTINGS_PLACEHOLDER.generateItemStack(1), e -> new SlotGui(Mmorpg.INSTANCE, p, orionPlayer).show(p)), 2,3);

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
