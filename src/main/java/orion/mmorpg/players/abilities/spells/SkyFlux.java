package orion.mmorpg.players.abilities.spells;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.SphereEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.utils.Element;
import orion.mmorpg.utils.Knowledge;

public class SkyFlux extends Spell {

    public SkyFlux() {
        super("SKY_FLUX", 15, Element.EARTH, 0);
    }

    @Override
    public void launch(Mmorpg mmorpg, Location Location, Knowledge knowledge) {
        if(getCaster() == null || !(getCaster() instanceof Player)) return;
        Player player = (Player) getCaster();
        RayTraceResult rayTraceResult = player.getWorld().rayTraceEntities(player.getEyeLocation().add(player.getEyeLocation().getDirection().normalize()), player.getEyeLocation().getDirection().normalize(), 10, 2);
        if (rayTraceResult.getHitEntity() == player) return;
        Effect lineEffect = new TestEffect(Mmorpg.effectManager, player.getEyeLocation().getDirection().normalize());
        lineEffect.setLocation(player.getEyeLocation());
        lineEffect.setTargetEntity(rayTraceResult.getHitEntity());
        lineEffect.start();
    }

    class TestEffect extends SphereEffect {
        Vector v;

        public TestEffect(EffectManager effectManager, Vector v) {
            super(effectManager);
            this.v = v;
            this.particle = Particle.FLAME;
        }

        @Override
        public void onRun() {
            super.onRun();
            setLocation(getLocation().add(v));
            if (getTargetEntity().getLocation().distance(getLocation()) <= 1.5) {
                getTargetEntity().setVelocity(new Vector(0, 5, 0));
                this.cancel();
            }
        }
    }
}
