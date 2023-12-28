package orion.mmorpg.items.sample;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.SphereEffect;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.ExcludeRegister;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;
import orion.mmorpg.utils.Utils;

import java.util.List;

@ExcludeRegister
public class BasicMagicWeapon extends BasicMeleeWeapon {

    public Particle particle = Particle.GLOW;

    public BasicMagicWeapon(ItemStack item) {
        super(item);
    }

    public BasicMagicWeapon(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, List<String> LORE, Rarity RARITY, Stats.Modifiers MODIFIERS, float DAMAGE) {
        super(MATERIAL, NAME, INTERNAL_ID, MODEL_ID, LORE, RARITY, MODIFIERS, DAMAGE);
    }

    public BasicMagicWeapon(Material MATERIAL, String NAME, String INTERNAL_ID, int MODEL_ID, List<String> LORE, Rarity RARITY, String PERMISSION, Stats.Modifiers MODIFIERS, float DAMAGE) {
        super(MATERIAL, NAME, INTERNAL_ID, MODEL_ID, LORE, RARITY, PERMISSION, MODIFIERS, DAMAGE);
    }

    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        super.onUseItem(mmorpg, data, e);
        Player player = e.getPlayer();
        if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            castSpell(player);
        }
    }

    @Override
    public void onHitWithItem(EntityDamageByEntityEvent e) {
        super.onHitWithItem(e);
        //castSpell((Player) e.getDamager());
    }

    private void castSpell(Player caster) {
        if (caster.getAttackCooldown() != 1) {
            return;
        }
        OrionPlayer data = Mmorpg.INSTANCE.getPlayerManager().getPlayer(caster.getUniqueId());
        Vector dir = caster.getEyeLocation().getDirection().normalize();
        AttackEffect effect = new AttackEffect(Mmorpg.effectManager, dir.multiply(0.5), caster, data, getDAMAGE(), particle);
        effect.setLocation(caster.getEyeLocation());
        effect.start();
    }

    private class AttackEffect extends SphereEffect {
        Vector v;
        Player caster;
        OrionPlayer casterData;
        float damage;

        public AttackEffect(EffectManager effectManager, Vector v, Player caster, OrionPlayer casterData, float damage, Particle particle) {
            super(effectManager);
            this.v = v;
            this.particle = particle;
            this.caster = caster;
            this.casterData = casterData;
            this.damage = damage;
            this.iterations = 15;
            this.particles = 10;
            this.asynchronous = false;
        }

        @Override
        public void onRun() {
            super.onRun();
            setLocation(getLocation().add(v));
            getLocation().getWorld().getNearbyEntities(getLocation(), 1, 1, 1, e -> ((e instanceof Damageable) && (e != caster))).forEach(e -> {
                float totalDamage = Utils.calculateMagicDamage(casterData.getStats(), damage);
                ((LivingEntity) e).damage(totalDamage, caster);
                e.setVelocity(e.getVelocity().add(e.getLocation().toVector().subtract(getLocation().toVector()).normalize().multiply(1)));
                if (!(e instanceof Player))
                    casterData.updateMobHealthBar(((LivingEntity) e).getHealth() - totalDamage, ((LivingEntity) e).getMaxHealth(), e.getName());
                else {
                    OrionPlayer target = Mmorpg.INSTANCE.getPlayerManager().getPlayer(e.getUniqueId());
                    casterData.updateMobHealthBar(target.getHealth(), target.getStats().get(Stats.StatsInfo.VITALITY), e.getName());
                }
                this.cancel();
            });
        }
    }
}
