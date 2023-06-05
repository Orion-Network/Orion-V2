package orion.mmorpg.effects;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.effect.AnimatedBallEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class FishEffect extends AnimatedBallEffect {

    public FishEffect(EffectManager effectManager, Player player) {
        super(effectManager);
        this.type = EffectType.REPEATING; // Type de l'effet
        particle = Particle.REDSTONE; // Utilisation de la particule REDSTONE pour représenter le poisson
        period = 1; // Période de l'effet (intervalle entre les mises à jour)
        iterations = 100; // Nombre total d'itérations de l'effet
        size = 1.0f; // Taille des particules
        speed = 0.2f; // Vitesse de déplacement des particules

        // Calcul des dimensions du poisson
        float width = 2.0f;
        float height = 1.5f;
        float length = width / 2.0f;

        // Position de départ du poisson
        Location origin = player.getLocation().add(0, 1, 0);

        // Boucle pour générer les particules représentant le poisson
        for (float x = -width; x <= width; x += 0.2f) {
            for (float y = -height; y <= height; y += 0.2f) {
                float z = (float) Math.sin(x / width * Math.PI) * length;
                Location location = origin.clone().add(x, y, z);
                display(particle, location);
            }
        }
    }
}