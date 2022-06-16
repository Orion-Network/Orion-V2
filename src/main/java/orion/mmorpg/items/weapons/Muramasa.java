package orion.mmorpg.items.weapons;

import orion.mmorpg.config.AssociationConfig;
import de.slikey.effectlib.Effect;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.effects.SlashEffect;
import orion.mmorpg.items.sample.BasicMeleeWeapon;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;
import orion.mmorpg.utils.Utils;

public class Muramasa extends BasicMeleeWeapon {
    public Muramasa() {
        super(Material.NETHERITE_SWORD, "Muramasa", "MURAMASA", AssociationConfig.getModel("weapon/sword/legacy_sword"), "Une épée légendaire dégageant une aura maléfique.", Rarity.SUPERRARE, new Stats.Builder().withStrength(7).withSpeed(5).withCritical_chance(20).withCritical_damage(10).build(), 15);
    }

    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        super.onUseItem(mmorpg, data, e);
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) return;
        castAttack(e.getPlayer(), data);
    }

    //TODO : Sword kill entities two times
    @Override
    public void onHitWithItem(EntityDamageByEntityEvent e) {
        super.onHitWithItem(e);
        castAttack((Player) e.getDamager(), Mmorpg.INSTANCE.getPlayerManager().getPlayer(e.getDamager().getUniqueId()));
    }

    private void castAttack(Player player, OrionPlayer data) {
        if(player.getAttackCooldown() != 1) return;
        Effect slashEffect = new SlashEffect(Mmorpg.effectManager);
        slashEffect.setLocation(player.getLocation());
        slashEffect.setTargetLocation(player.getLocation().clone().add(10,1,10));
        slashEffect.start();
        player.getLocation().getWorld().getNearbyEntities(player.getLocation().add(player.getEyeLocation().getDirection().normalize().multiply(4)), 2, 2, 2, ent -> ent instanceof LivingEntity).forEach(entity -> {
            if(entity.isInvulnerable() || entity == player) return;
            ((LivingEntity) entity).damage(Utils.calculateDamage(data.getStats(), this.getDAMAGE()));
            data.updateMobHealthBar(((LivingEntity) entity).getHealth(), ((LivingEntity) entity).getMaxHealth(), entity.getName());
        });
    }
}
