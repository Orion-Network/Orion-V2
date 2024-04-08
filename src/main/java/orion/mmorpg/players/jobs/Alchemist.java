package orion.mmorpg.players.jobs;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import orion.mmorpg.items.sample.BasicPotion;
import orion.mmorpg.items.sample.BasicIngredient;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.effects.Invincibility;
import orion.mmorpg.utils.ItemUtils;
import orion.mmorpg.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static orion.mmorpg.utils.Rarity.*;

public class Alchemist extends AbstractJob {

    static {
        ItemUtils.registerItem("INVISIBILITY_EYE", InvisibilityEye.class);
    }

    public Alchemist() {
        super(JobType.ALCHEMIST);
    }

    public Alchemist(int level, int experience) {
        super(JobType.ALCHEMIST, level, experience);
    }

    public ChestGui getBrewingPane() {
        ChestGui gui = new ChestGui(1, "Menu d'infusion");
        gui.setOnBottomClick(e -> {
            if (e.isShiftClick()) {
                e.setCancelled(true);
            }
        });

        HashMap<Integer, BasicIngredient> ingredients = new HashMap<>();

        StaticPane ingredientPane = new StaticPane(0, 0, 7, 1);
        StaticPane potionPane = new StaticPane(7, 0, 2, 1);
        potionPane.addItem(
                new GuiItem(ItemUtils.generateBasicItem("RIGHT_BUTTON_PLACEHOLDER").generateItem(null, 1), e -> {
                    e.setCancelled(true);
                    if (ingredients.isEmpty()) {
                        Utils.sendInfoMessage(e.getWhoClicked(), "Aucun ingrédient");
                        return;
                    }

                    if (gui.getInventory().getItem(8) == null
                            || gui.getInventory().getItem(8).getType() == Material.AIR) {
                        Utils.sendInfoMessage(e.getWhoClicked(), "Aucune potion sélectionnée");
                        return;
                    }

                    BasicPotion potion = (BasicPotion) ItemUtils.getItemFromItemstack(gui.getInventory().getItem(8));

                    int experience = 0;
                    for (BasicIngredient ingredient : ingredients.values()) {
                        potion.addEffect(ingredient.getOEffect());
                        experience += getIngredientExperience(ingredient);
                    }

                    // TODO Understand why we can't basic directly BasicItem and we need to generate it
                    e.getWhoClicked().getInventory().addItem(potion.generateItem(potion.generateItem(null, 1), 1));

                    OrionPlayer player = OrionPlayer.getOrionPlayer((Player) e.getWhoClicked());
                    player.getJobs().getJob(JobType.ALCHEMIST).addExp(experience);

                    e.getWhoClicked().closeInventory();
                }), 0, 0);

        ingredientPane.setOnClick(e -> {
            if (e.getCursor() == null || e.getCursor().getType() == Material.AIR) {
                return;
            }
            System.out.println(e.getAction());
            if (e.getAction() != InventoryAction.PLACE_ALL && e.getAction() != InventoryAction.PLACE_SOME
                    && e.getAction() != InventoryAction.PICKUP_ALL || e.getCursor().getAmount() != 1) {
                e.setCancelled(true);
                return;
            }

            if (e.getAction().equals(InventoryAction.PLACE_ALL) || e.getAction().equals(InventoryAction.PLACE_SOME)) {
                BasicItem item = ItemUtils.getItemFromItemstack(e.getCursor());
                if (!(item instanceof BasicIngredient)) {
                    Utils.sendInfoMessage(e.getWhoClicked(), "L'item n'est pas un ingrédient");
                    e.setCancelled(true);
                    return;
                }
                ingredients.put(e.getSlot(), (BasicIngredient) item);
            } else if (e.getAction().equals(InventoryAction.PICKUP_ALL)) {
                ingredients.remove(e.getSlot());
            }
        });

        gui.addPane(ingredientPane);
        gui.addPane(potionPane);
        return gui;
    }

    private int getIngredientExperience(BasicIngredient ingredient) {
        return switch (ingredient.getRARITY()) {
        case COMMON, UNCOMMON -> 1;
        case RARE -> 2;
        case SUPERRARE -> 3;
        case HERO -> 5;
        case LEGENDARY -> 8;
        case MYTHIC -> 13;
        default -> 0;
        };
    }

    public static class InvisibilityEye extends BasicIngredient {
        public InvisibilityEye() {
            super(Material.ENDER_EYE, "Invisibility Eye", "INVISIBILITY_EYE", 0,
                    new ArrayList<>(List.of("An eye that can be used to brew invisibility potions")), RARE,
                    Invincibility.class);
        }
    }
}
