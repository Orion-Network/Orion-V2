package orion.mmorpg.players.races;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Support;
import orion.mmorpg.utils.Stats;
import orion.mmorpg.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public enum Race {
    HUMAN("Human", new Stats(1,1,1,1,1,1,1,1,1), ItemList.MINECRAFT_STONE_SWORD.getProcessedItem(1)),
    CELESTIAN("Celestian", new Stats(1,1,1,1,1,1,1,1,1), ItemList.CELESTIAN_WINGS.getProcessedItem(1), null ,Arrays.asList(spec.ELYTRA), null, null)
    ;

    public String name;
    public Stats stats;
    public ItemStack itemStack;
    public String[] spellList;
    public ArrayList<spec> species;
    public Support support = null;
    BiConsumer<Player, OrionPlayer> onRaceSelected;

    Race(String name, Stats stats, ItemStack itemStack) {
        this.name = name;
        this.stats = stats;
        this.itemStack = itemStack;
        this.spellList = new String[0];
        this.species = new ArrayList<>();
        this.onRaceSelected = (p, op) -> {
            Utils.sendDebugMessage(p, "Race selected !");
        };
    }

    Race(String name, Stats stats, ItemStack itemStack, @Nullable String[] spellList, @Nullable List<spec> species, @Nullable Support support, @Nullable BiConsumer<Player, OrionPlayer> onRaceSelected) {
        this.name = name;
        this.stats = stats;
        this.itemStack = itemStack;
        if(spellList != null)
            this.spellList = spellList;
        else
            this.spellList = new String[0];
        this.species = new ArrayList<>();
        this.species.addAll(species);
        this.support = support;
        if(onRaceSelected != null)
            this.onRaceSelected = onRaceSelected;
        else
            this.onRaceSelected = (p, op) -> { Utils.sendDebugMessage(p, "Race selected !"); };
    }

    public enum spec {
        NOARMOR, ELYTRA
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

    public BiConsumer<Player, OrionPlayer> getOnRaceSelected() {
        return onRaceSelected;
    }
}
