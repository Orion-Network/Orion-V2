package orion.mmorpg.items.weapons;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.items.sample.BasicMeleeWeapon;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DragonSlayer extends BasicMeleeWeapon {

    public DragonSlayer() {
        super(Material.NETHERITE_SWORD, "Dragon Slayer", "DRAGON_SLAYER", 0,
                Arrays.asList("Elle était trop grande pour être qualifiée d'épée.",
                        "Massive, épaisse, lourde et bien trop rugueuse.", "En fait, c'était un tas de fer brut."),
                Rarity.LEGENDARY,
                new Stats.Modifiers.Builder().multiply(Stats.StatsInfo.ATTACK_SPEED, 0.75f)
                        .multiply(Stats.StatsInfo.CRITICAL_DAMAGE, 1.5f).add(Stats.StatsInfo.STRENGTH, 75).build(),
                200);
    }
}
