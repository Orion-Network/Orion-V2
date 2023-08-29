package orion.mmorpg.items.weapons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.entities.samples.AbstractMob;
import orion.mmorpg.items.sample.BasicMeleeWeapon;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.Arrays;
import java.util.List;

public class SplitSoulKatana extends BasicMeleeWeapon {
    public SplitSoulKatana(ItemStack item) {
        super(item);
    }

    public SplitSoulKatana() {
        super(Material.NETHERITE_SWORD, "Katana diviseur d'ame", "SPLIT_SOUL_KATANA", 0, Arrays.asList("Un katana si puissant,", "qu'il peut diviser l'ame", "de ses cibles"), Rarity.MYTHIC,
                new Stats.Modifiers.Builder().add(Stats.StatsInfo.STRENGTH, 50).add(Stats.StatsInfo.INTELLIGENCE, 75).add(Stats.StatsInfo.ATTACK_SPEED, 30).build(), 20);
    }

    @Override
    public void onHitWithItem(EntityDamageByEntityEvent e) {
        Stats stats = null;
        if(e.getEntity() instanceof AbstractMob mob)
            stats = mob.getStats();
        else if(e.getEntity() instanceof Player player)
            stats = OrionPlayer.getOrionPlayer(player).getStats();
        if(stats != null)
            e.setDamage(e.getDamage() * (1 + stats.get(Stats.StatsInfo.INTELLIGENCE) / 10));
        super.onHitWithItem(e);
    }
}
