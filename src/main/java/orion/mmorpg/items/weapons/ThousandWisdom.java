package orion.mmorpg.items.weapons;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicMeleeWeapon;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.Arrays;
import java.util.List;

public class ThousandWisdom extends BasicMeleeWeapon {
    public ThousandWisdom() {
        super(Material.NETHERITE_SWORD, "Thousands Wisdom", "THOUSANDS_WISDOM", 0,
                Arrays.asList("Une épée imprégnée de magie", "si seulement elle pouvait", "lancer des sorts"),
                Rarity.MYTHIC, new Stats.Modifiers.Builder().add(Stats.StatsInfo.INTELLIGENCE, 100)
                        .add(Stats.StatsInfo.LUCK, 7).add(Stats.StatsInfo.VITALITY, 50).build(),
                0);
    }
}
