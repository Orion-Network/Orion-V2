package orion.mmorpg.items.armors;

import de.slikey.effectlib.effect.DiscoBallEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.sample.BasicArmorItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.races.Race;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;
import orion.mmorpg.utils.Utils;

public class CelestianSet {
    public static class CelestianCrown extends BasicArmorItem {

        public CelestianCrown() {
            super(Material.GOLDEN_HELMET, "Couronne Céleste", "CELESTIAN_CROWN", 1, "Une couronne donnée au premier célestien", Rarity.HERO, ArmorType.HELMET, new Stats(0, 0, 0, 0, 0, 0, 0, 0, 0));
        }

        @Override
        public boolean onEquip(Player p, OrionPlayer data, ItemStack item) {
            if (data.getRace() != Race.CELESTIAN) {
                Utils.sendInfoMessage(p, "Vous devez être célestien pour équiper la couronne");
                return false;
            }
            return super.onEquip(p, data, item);
        }

        @Override
        public void onCosmeticEquip(Player p, OrionPlayer data, ItemStack item) {
            DiscoBallEffect effect = new DiscoBallEffect(Mmorpg.effectManager);
            effect.offset = new Vector(0, 1.5, 0);
            effect.disappearWithOriginEntity = true;
            effect.infinite();
            effect.setEntity(p);
            effect.start();
            data.getEffectHashMap().put(this.INTERNAL_ID, effect);
        }

        @Override
        public void onCosmeticUnequip(Player p, OrionPlayer data, ItemStack item) {
            if (!data.getEffectHashMap().containsKey(this.INTERNAL_ID)) return;
            data.getEffectHashMap().get(this.INTERNAL_ID).cancel();
            data.getEffectHashMap().remove(this.INTERNAL_ID);
        }
    }

    public static class CelestianWings extends BasicArmorItem {
        public CelestianWings() {
            super(Material.ELYTRA, "Ailes Célestes", "CELESTIAN_WINGS", 0, null, Rarity.COMMON, ArmorType.WINGS,
                    new Stats(0, 0, 0, 0, 5, 0, 2, 0, 0));
        }
    }
}
