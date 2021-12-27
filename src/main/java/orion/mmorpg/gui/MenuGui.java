package orion.mmorpg.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Bukkit;
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
import orion.mmorpg.players.Mode;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.races.Race;
import orion.mmorpg.utils.OrionGson;
import orion.mmorpg.utils.Stats;
import orion.mmorpg.utils.Utils;

public class MenuGui extends ChestGui {
    Player p;
    OrionPlayer orionPlayer;

    public MenuGui(Player p, OrionPlayer orionPlayer) {
        super(4, ChatColor.GREEN + "Menu");
        this.p = p;
        this.orionPlayer = orionPlayer;
        this.setOnTopClick(e -> e.setCancelled(true));

        if(orionPlayer.getRace() != null) {
            this.addPane(getArmorPane());
            this.addPane(getMenuPane());
            this.show(p);
        } else {
            new RaceGui(p, orionPlayer);
        }

        this.setOnBottomClick(e -> {
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();
            if(item.getType() == Material.AIR)
                return;
            PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();

            if(container.has(NamespacedKey.minecraft("type"), PersistentDataType.STRING)) {
                if(container.get(NamespacedKey.minecraft("type"), PersistentDataType.STRING).equalsIgnoreCase("armor")) {
                    Stats stats = Mmorpg.getOrionGson().getGSON().fromJson(container.get(NamespacedKey.minecraft("stats"), PersistentDataType.STRING), Stats.class);
                    if(container.get(NamespacedKey.minecraft("armor_type"), PersistentDataType.STRING).equalsIgnoreCase("HELMET")) {
                        if(orionPlayer.getEquipements()[3] != null) {
                            Utils.sendErrorMessage(p, "Unequip helmet first");
                            return;
                        }

                        orionPlayer.getEquipements()[3] = item;
                        orionPlayer.getStats().combineOrSeparateStats(stats, true);
                        Utils.sendInfoMessage(p, "You equipped an helmet");

                        getArmorPane().removeItem(1,0);
                        p.getInventory().setHelmet(item);
                        getMenuPane();
                        getArmorPane().addItem(new GuiItem(item), 1, 0);

                        e.setCurrentItem(null);
                    } else if(container.get(NamespacedKey.minecraft("armor_type"), PersistentDataType.STRING).equalsIgnoreCase("CHESTPLATE")) {
                        if(orionPlayer.getEquipements()[2] != null) {
                            Utils.sendErrorMessage(p, "Unequip chestplate first");
                            return;
                        }

                        if(item.getType().equals(Material.ELYTRA)) {
                            if (!orionPlayer.getRace().getSpecies().contains(Race.spec.ELYTRA)) {
                                Utils.sendErrorMessage(p, "Votre race ne peut pas equiper d'ailes");
                                return;
                            }
                        }

                        orionPlayer.getEquipements()[2] = item;
                        orionPlayer.getStats().combineOrSeparateStats(stats, true);
                        Utils.sendInfoMessage(p, "You equipped an chestplate");

                        getArmorPane().removeItem(1,1);
                        p.getInventory().setChestplate(item);
                        getMenuPane();
                        getArmorPane().addItem(new GuiItem(item), 1, 1);

                        e.setCurrentItem(null);
                    } else if(container.get(NamespacedKey.minecraft("armor_type"), PersistentDataType.STRING).equalsIgnoreCase("LEGGINGS")) {
                        if(orionPlayer.getEquipements()[1] != null) {
                            Utils.sendErrorMessage(p, "Unequip leggings first");
                            return;
                        }

                        orionPlayer.getEquipements()[1] = item;
                        orionPlayer.getStats().combineOrSeparateStats(stats, true);
                        Utils.sendInfoMessage(p, "You equipped an leggings");

                        getArmorPane().removeItem(1,2);
                        p.getInventory().setLeggings(item);
                        getMenuPane();
                        getArmorPane().addItem(new GuiItem(item), 1, 2);

                        e.setCurrentItem(null);
                    } else if(container.get(NamespacedKey.minecraft("armor_type"), PersistentDataType.STRING).equalsIgnoreCase("BOOTS")) {
                        if(orionPlayer.getEquipements()[0] != null) {
                            Utils.sendErrorMessage(p, "Unequip boots first");
                            return;
                        }

                        orionPlayer.getEquipements()[0] = item;
                        orionPlayer.getStats().combineOrSeparateStats(stats, true);
                        Utils.sendInfoMessage(p, "You equipped a boots");

                        getArmorPane().removeItem(1,3);
                        p.getInventory().setBoots(item);
                        getMenuPane();
                        getArmorPane().addItem(new GuiItem(item), 1, 3);

                        e.setCurrentItem(null);
                    } else if(container.get(NamespacedKey.minecraft("armor_type"), PersistentDataType.STRING).equalsIgnoreCase("WINGS")) {
                        if (!orionPlayer.getRace().getSpecies().contains(Race.spec.ELYTRA)) {
                            Utils.sendErrorMessage(p, "Votre race ne peut pas equiper d'ailes");
                            return;
                        }

                        if(orionPlayer.getEquipements()[2] != null) {
                            Utils.sendErrorMessage(p, "Unequip chestplate first");
                            return;
                        }

                        orionPlayer.getEquipements()[2] = item;
                        orionPlayer.getStats().combineOrSeparateStats(stats, true);
                        Utils.sendInfoMessage(p, "You equipped an Wings");

                        getArmorPane().removeItem(1,1);
                        p.getInventory().setChestplate(item);
                        getMenuPane();
                        getArmorPane().addItem(new GuiItem(item), 1, 1);

                        e.setCurrentItem(null);
                    }
                }
            }
        });
    }

    StaticPane armorPane = new StaticPane(0,0, 3,4);
    GuiItem HELMET_PLACEHOLDER = new GuiItem(ItemList.HELMET_PLACEHOLDER.getProcessedItem(1));
    GuiItem CHESTPLATE_PLACEHOLDER = new GuiItem(ItemList.CHESTPLATE_PLACEHOLDER.getProcessedItem(1));
    GuiItem LEGGINGS_PLACEHOLDER = new GuiItem(ItemList.LEGGINGS_PLACEHOLDER.getProcessedItem(1));
    GuiItem BOOTS_PLACEHOLDER = new GuiItem(ItemList.BOOTS_PLACEHOLDER.getProcessedItem(1));

    public StaticPane getArmorPane() {
        for(int i = 0; i< 4; i++) {
            if(p.getInventory().getArmorContents()[i] == null) {
                if(i == 0)
                    armorPane.addItem(BOOTS_PLACEHOLDER, 1, 3);
                if(i == 1)
                    armorPane.addItem(LEGGINGS_PLACEHOLDER, 1, 2);
                if(i == 2)
                    armorPane.addItem(CHESTPLATE_PLACEHOLDER, 1, 1);
                if(i == 3)
                    armorPane.addItem(HELMET_PLACEHOLDER, 1, 0);
            } else {
                armorPane.addItem(new GuiItem(p.getInventory().getArmorContents()[i].clone()), 1, 3-i);
            }
        }
        armorPane.setOnClick(e -> {
            if(e.getCurrentItem().getType() != Material.AIR) {
                ItemStack item = e.getCurrentItem();
                PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();

                Stats stats = Mmorpg.getOrionGson().getGSON().fromJson(container.get(NamespacedKey.minecraft("stats"), PersistentDataType.STRING), Stats.class);
                if(container.get(NamespacedKey.minecraft("armor_type"), PersistentDataType.STRING).equalsIgnoreCase("HELMET")) {
                    orionPlayer.getEquipements()[3] = null;
                    orionPlayer.getStats().combineOrSeparateStats(stats, false);
                    Utils.sendInfoMessage(p, "You removed an helmet");

                    getArmorPane().removeItem(1, 0);
                    p.getInventory().addItem(item);
                    p.getInventory().setHelmet(null);
                    getMenuPane();
                    getArmorPane().addItem(HELMET_PLACEHOLDER, 1, 0);
                } else if(container.get(NamespacedKey.minecraft("armor_type"), PersistentDataType.STRING).equalsIgnoreCase("CHESTPLATE")) {
                    orionPlayer.getEquipements()[2] = null;
                    orionPlayer.getStats().combineOrSeparateStats(stats, false);
                    Utils.sendInfoMessage(p, "You removed an chestplate");

                    getArmorPane().removeItem(1, 1);
                    p.getInventory().addItem(item);
                    p.getInventory().setChestplate(null);
                    getMenuPane();
                    getArmorPane().addItem(CHESTPLATE_PLACEHOLDER, 1, 1);
                } else if(container.get(NamespacedKey.minecraft("armor_type"), PersistentDataType.STRING).equalsIgnoreCase("LEGGINGS")) {
                    orionPlayer.getEquipements()[1] = null;
                    orionPlayer.getStats().combineOrSeparateStats(stats, false);
                    Utils.sendInfoMessage(p, "You removed an leggings");

                    getArmorPane().removeItem(1, 2);
                    p.getInventory().addItem(item);
                    p.getInventory().setLeggings(null);
                    getMenuPane();
                    getArmorPane().addItem(LEGGINGS_PLACEHOLDER, 1, 2);
                } else if(container.get(NamespacedKey.minecraft("armor_type"), PersistentDataType.STRING).equalsIgnoreCase("BOOTS")) {
                    orionPlayer.getEquipements()[0] = null;
                    orionPlayer.getStats().combineOrSeparateStats(stats, false);
                    Utils.sendInfoMessage(p, "You removed an boots");

                    getArmorPane().removeItem(1, 3);
                    p.getInventory().addItem(item);
                    p.getInventory().setBoots(null);
                    getMenuPane();
                    getArmorPane().addItem(HELMET_PLACEHOLDER, 1, 3);
                } else if(container.get(NamespacedKey.minecraft("armor_type"), PersistentDataType.STRING).equalsIgnoreCase("WINGS")) {
                    orionPlayer.getEquipements()[2] = null;
                    orionPlayer.getStats().combineOrSeparateStats(stats, false);
                    Utils.sendInfoMessage(p, "You removed an wings");

                    getArmorPane().removeItem(1, 1);
                    p.getInventory().addItem(item);
                    p.getInventory().setChestplate(null);
                    getMenuPane();
                    getArmorPane().addItem(CHESTPLATE_PLACEHOLDER, 1, 1);
                }
            }
        });
        this.update();
        return armorPane;
    }

    StaticPane menuPane = new StaticPane(3, 0, 6,4);
    public StaticPane getMenuPane() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        ItemMeta headMeta = head.getItemMeta();
        headMeta.setDisplayName(ChatColor.YELLOW + orionPlayer.getRace().getName() + " " + p.getName());
        headMeta.setLore(orionPlayer.getStats().statsAsLore());
        head.setItemMeta(headMeta);

        menuPane.addItem(new GuiItem(head), 2, 1);
        if(p.isOp()) {
            menuPane.addItem(new GuiItem(new ItemStack(Material.BARRIER), e -> {
                if(orionPlayer.getPlayerMode() == Mode.PLAYER)
                    if(p.hasPermission("builder"))
                        orionPlayer.setPlayerMode(Mode.BUILDER);

                if(orionPlayer.getPlayerMode() == Mode.BUILDER)
                    if(p.hasPermission("admin"))
                        orionPlayer.setPlayerMode(Mode.ADMIN);
                    else
                        orionPlayer.setPlayerMode(Mode.PLAYER);

                if(orionPlayer.getPlayerMode() == Mode.ADMIN)
                    orionPlayer.setPlayerMode(Mode.PLAYER);
                Utils.sendInfoMessage(p, "Vous êtes en mode : " + orionPlayer.getPlayerMode());
            }), 3, 1);
        }

        menuPane.addItem(new GuiItem(ItemList.PORTAL_STONE.getProcessedItem(1), e -> {
            new PortalStoneGui(p, orionPlayer).show(p);
        }), 2,2);

        return menuPane;
    }

    public void show() {
        this.show(p);
    }
}
