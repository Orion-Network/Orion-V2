package orion.mmorpg.items.armors;

import de.slikey.effectlib.effect.DiscoBallEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.races.Race;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;
import orion.mmorpg.utils.Utils;

import java.util.Collections;

public class CelestianSet {
    public static class CelestianCrown extends BasicArmorItem {

        public CelestianCrown() {
            super(Material.GOLDEN_HELMET, "Couronne Céleste", "CELESTIAN_CROWN", 1,
                    Collections.singletonList("Une couronne donnée au premier célestien"), Rarity.HERO,
                    ArmorType.HELMET, new Stats.Modifiers());
        }

        @Override
        public boolean onEquip(Player p, OrionPlayer data) {
            if (data.getRace() != Race.CELESTIAL) {
                Utils.sendInfoMessage(p, "Vous devez être célestien pour équiper la couronne");
                return false;
            }
            return super.onEquip(p, data);
        }

        @Override
        public void onCosmeticEquip(Player p, OrionPlayer data) {
            DiscoBallEffect effect = new DiscoBallEffect(Mmorpg.effectManager);
            effect.offset = new Vector(0, 1.5, 0);
            effect.disappearWithOriginEntity = true;
            effect.infinite();
            effect.setEntity(p);
            effect.start();
            data.getDisplayEffectHashMap().put(this.INTERNAL_ID, effect);
        }

        @Override
        public void onCosmeticUnequip(Player p, OrionPlayer data) {
            if (!data.getDisplayEffectHashMap().containsKey(this.INTERNAL_ID))
                return;
            data.getDisplayEffectHashMap().get(this.INTERNAL_ID).cancel();
            data.getDisplayEffectHashMap().remove(this.INTERNAL_ID);
        }
    }

    public static class CelestianWings extends BasicArmorItem {
        public CelestianWings() {
            super(Material.ELYTRA, "Ailes Célestes", "CELESTIAN_WINGS", 0, null, Rarity.COMMON, ArmorType.WINGS,
                    new Stats.Modifiers.Builder().add(Stats.StatsInfo.VITALITY, 5).add(Stats.StatsInfo.DEFENCE, 2)
                            .build());
        }
    }

    public static class CelestianRing extends BasicArmorItem {
        public CelestianRing() {
            super(Material.BLUE_DYE, "Anneau Céleste", "CELESTIAN_RING", 1, null, Rarity.COMMON, ArmorType.RING,
                    new Stats.Modifiers.Builder().add(Stats.StatsInfo.CRITICAL_CHANCE, 5)
                            .add(Stats.StatsInfo.CRITICAL_DAMAGE, 3).build());
        }
    }

    public static class CelestianBelt extends BasicArmorItem {
        public CelestianBelt() {
            super(Material.MAGENTA_DYE, "Ceinture Céleste", "CELESTIAN_BELT", 3, null, Rarity.COMMON, ArmorType.BELT,
                    new Stats.Modifiers.Builder().add(Stats.StatsInfo.SPEED, 20).build());
        }
    }
}
