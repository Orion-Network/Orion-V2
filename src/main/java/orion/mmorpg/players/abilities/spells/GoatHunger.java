package orion.mmorpg.players.abilities.spells;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.AtomEffect;
import de.slikey.effectlib.effect.CircleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.utils.Element;
import orion.mmorpg.utils.Knowledge;

public class GoatHunger extends Spell {

    public GoatHunger() {
        super("GOAT_HUNGER", 0, Element.BLACK, 0, TargetType.TARGET);
    }

    @Override
    public void launch(Mmorpg mmorpg, Location location, Knowledge knowledge) {
        Effect goatHungerEffect = new GoatHungerEffect(Mmorpg.effectManager);
        RayTraceResult rayTraceResult = getCaster().getWorld().rayTraceEntities(
                ((Player) getCaster()).getEyeLocation()
                        .add(((Player) getCaster()).getEyeLocation().getDirection().normalize()),
                ((Player) getCaster()).getEyeLocation().getDirection().normalize(), 10,
                (e) -> (e instanceof LivingEntity && !e.isInvulnerable()));
        if (rayTraceResult.getHitEntity() == null || rayTraceResult.getHitEntity() == getCaster())
            return;
        goatHungerEffect.setLocation(((Player) getCaster()).getEyeLocation());
        goatHungerEffect.setTargetEntity(rayTraceResult.getHitEntity());
        goatHungerEffect.start();
    }

    class GoatHungerEffect extends CircleEffect {

        public GoatHungerEffect(EffectManager effectManager) {
            super(effectManager);
            this.radius = 1;
            this.iterations = 100;
            this.wholeCircle = true;
            this.particle = Particle.SOUL;
        }

        @Override
        public void onRun() {
            super.onRun();
            Location direction = target.getLocation().subtract(origin.getLocation());
            if (this.getLocation().distance(getTargetEntity().getLocation()) > 2) {
                setLocation(getLocation().add(direction.toVector().normalize()));
            } else {
                this.cancel();
                if (getTargetEntity() instanceof Player) {
                    ((Player) getTargetEntity()).setWalkSpeed(0);
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Mmorpg.getPlugin(Mmorpg.class),
                            () -> Mmorpg.INSTANCE.getPlayerManager().getPlayer(getTargetEntity().getUniqueId())
                                    .respawn(),
                            5 * 1000);
                } else {
                    ((LivingEntity) getTargetEntity()).setAI(false);
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Mmorpg.getPlugin(Mmorpg.class), () -> {
                        ((LivingEntity) getTargetEntity()).setAI(true);
                        getTargetEntity().remove();
                    }, 5 * 20);
                }
                AtomEffect effect = new AtomEffect(Mmorpg.effectManager);
                effect.setEntity(getTargetEntity());
                effect.disappearWithOriginEntity = true;
                effect.duration = 5 * 1000;
                effect.particleNucleus = Particle.SOUL;
                effect.radiusNucleus = 0.5F;
                effect.particleOrbital = Particle.SOUL_FIRE_FLAME;
                effect.start();
            }
        }
    }
}
