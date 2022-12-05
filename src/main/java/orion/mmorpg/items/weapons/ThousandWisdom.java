package orion.mmorpg.items.weapons;

import org.bukkit.Material;
import orion.mmorpg.items.sample.BasicMeleeWeapon;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.Arrays;
import java.util.List;

public class ThousandWisdom extends BasicMeleeWeapon {
    public ThousandWisdom() {
        super(Material.NETHERITE_SWORD, "Thousands Wisdom", "THOUSANDS_WISDOM", 0, Arrays.asList("Une épée imprégnée de magie","si seulement elle pouvait","lancer des sorts"), Rarity.MYTHIC,
                new Stats.Builder().withIntel(100).withLuck(7).withVitality(50).build(), 0);
    }
}
