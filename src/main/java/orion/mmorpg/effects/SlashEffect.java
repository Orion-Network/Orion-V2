package orion.mmorpg.effects;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.VectorUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public class SlashEffect extends Effect {
    public Particle particle = Particle.SOUL;
    public boolean orient = false;
    public double xRotation, yRotation, zRotation = 0;
    public float radius = 7;
    public int particles = 100;
    protected float step = 0;
    public int particlesPerIterations = 10;

    public SlashEffect(EffectManager effectManager) {
        super(effectManager);
        this.type = EffectType.REPEATING;
        this.period = 1;
        this.iterations = 10;
    }

    @Override
    protected void reset() {
        this.step = 0;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        for(int i = 0; i < particlesPerIterations; i++) {
            double t = (Math.PI / particles) * step;
            double x = (Math.sin(t)*radius)/2;
            double z = Math.cos(t)*radius/2;
            System.out.println(t);
            //display(particle, location.clone().add(x/2, y/2, 0));
            for(float j = 0; j < 1; j+=0.1) {
                float size = step / 100; // 0 - 0.9
                size = size > 0.4 ? (float) (0.9 - size) : size;
                Vector v = new Vector(x + (x*j*size), 1, z + (z*j*size));
                v = VectorUtils.rotateVector(v, location.getYaw(), 0);

                display(particle, location.clone().add(v));
            }

            step++;
        }

    }
}
