package orion.mmorpg.players.races;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import orion.mmorpg.items.armors.StartingSet;
import orion.mmorpg.items.minecraft.WoodenSword;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.Support;
import orion.mmorpg.players.abilities.spells.Heal;
import orion.mmorpg.players.abilities.spells.Surpassement;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;
import orion.mmorpg.utils.Utils;

import java.util.*;
import java.util.function.BiConsumer;

@Getter
public enum Race {
    HUMAN("Humain", new Stats.Builder().build(),
            BasicItem
                    .generateTemplateBasicItem(Material.STONE_SWORD, "Humain", 0,
                            Collections.singletonList(
                                    "La race la plus populaire de ce monde malgré leurs faible magie"),
                            Rarity.COMMON)
                    .generateItem(null, 1),
            new ArrayList<>(Collections.singleton(Heal.class)), null, null, new LinkedHashMap<>() {
                {
                    put(5, Surpassement.class);
                }
            }, (p, op) -> {
                p.getInventory().addItem(new WoodenSword().generateItem(null, 1),
                        new StartingSet.StartingChestplate().generateItem(null, 1));
            }),
    CELESTIAL("Célestien", new Stats.Builder().build(),
            BasicItem
                    .generateTemplateBasicItem(Material.ELYTRA, "Celestian", 0,
                            Collections.singletonList("Une race celeste ayant la capacitée de voler"), Rarity.RARE)
                    .generateItem(null, 1),
            null, Arrays.asList(Spec.ELYTRA), null, null, null);

    public String name;
    public Stats stats;
    public ItemStack itemStack;
    public ArrayList<Class<? extends Spell>> spellList;
    public ArrayList<Spec> species;
    public Support support = null;
    public Map<Integer, Class<? extends Spell>> learningSpells;
    BiConsumer<Player, OrionPlayer> onRaceSelected;

    Race(String name, Stats stats, ItemStack itemStack, @Nullable ArrayList<Class<? extends Spell>> spellList,
            @Nullable List<Spec> species, @Nullable Support support,
            @Nullable Map<Integer, Class<? extends Spell>> learningSpells,
            @Nullable BiConsumer<Player, OrionPlayer> onRaceSelected) {
        this.name = name;
        this.stats = stats;
        this.itemStack = itemStack;
        if (spellList != null)
            this.spellList = spellList;
        else
            this.spellList = new ArrayList<>();
        this.species = new ArrayList<>();
        if (species != null)
            this.species.addAll(species);
        this.support = support;

        if (learningSpells != null)
            this.learningSpells = learningSpells;

        if (onRaceSelected != null)
            this.onRaceSelected = onRaceSelected;
        else
            this.onRaceSelected = (p, op) -> {
                Utils.sendDebugMessage(p, "Race selected !");
            };
    }

    public enum Spec {
        NOARMOR, ELYTRA
    }
}
