package orion.mmorpg.players.races;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Support;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;
import orion.mmorpg.utils.Utils;

import java.util.*;
import java.util.function.BiConsumer;

public enum Race {
    HUMAN("Humain", new Stats(1, 1, 1, 1, 1, 1, 1, 1, 1), BasicItem.generateTemplateBasicItem(Material.STONE_SWORD, "Humain", 0, "La race la plus populaire de ce monde malgré leurs faible magie", Rarity.COMMON).generateItem(null, 1), new String[]{"HEAL"}, null, null,
            new LinkedHashMap<Integer, String>() {
                {
                    put(5, "BOOSTED_MODE");
                }
            },(p, op) -> {
        p.getInventory().addItem(ItemList.MINECRAFT_WOODEN_SWORD.getProcessedItem(1), ItemList.STARTING_CHESTPLATE.getProcessedItem(1));
    }),
    CELESTIAL("Célestien", new Stats(1, 1, 1, 1, 1, 1, 1, 1, 1), BasicItem.generateTemplateBasicItem(Material.ELYTRA, "Celestian", 0, "Une race celeste ayant la capacitée de voler", Rarity.RARE).generateItem(null, 1), null, Arrays.asList(spec.ELYTRA), null, null, null);

    public String name;
    public Stats stats;
    public ItemStack itemStack;
    public String[] spellList;
    public ArrayList<spec> species;
    public Support support = null;
    public Map<Integer, String> learningSpells;
    BiConsumer<Player, OrionPlayer> onRaceSelected;

    Race(String name, Stats stats, ItemStack itemStack, @Nullable String[] spellList, @Nullable List<spec> species, @Nullable Support support, @Nullable Map<Integer, String> learningSpells, @Nullable BiConsumer<Player, OrionPlayer> onRaceSelected) {
        this.name = name;
        this.stats = stats;
        this.itemStack = itemStack;
        if (spellList != null)
            this.spellList = spellList;
        else
            this.spellList = new String[0];
        this.species = new ArrayList<>();
        if (species != null)
            this.species.addAll(species);
        this.support = support;

        if(learningSpells != null)
            this.learningSpells = learningSpells;

        if (onRaceSelected != null)
            this.onRaceSelected = onRaceSelected;
        else
            this.onRaceSelected = (p, op) -> {
                Utils.sendDebugMessage(p, "Race selected !");
            };
    }

    public String getName() {
        return name;
    }

    public Stats getStats() {
        return stats;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String[] getSpellList() {
        return spellList;
    }

    public ArrayList<spec> getSpecies() {
        return species;
    }

    public Support getSupport() {
        return support;
    }

    public Map<Integer, String> getLearningSpells() {
        return learningSpells;
    }

    public BiConsumer<Player, OrionPlayer> getOnRaceSelected() {
        return onRaceSelected;
    }

    public enum spec {
        NOARMOR, ELYTRA
    }
}
