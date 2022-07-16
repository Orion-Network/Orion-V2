package orion.mmorpg.players.abilities.spells;

import de.slikey.effectlib.effect.SphereEffect;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.utils.Element;
import orion.mmorpg.utils.Knowledge;

public class DarkRoom extends Spell {
    public DarkRoom() {
        super("DARK_ROOM", 10, Element.BLACK, 180*1000);
    }

    @Override
    public void launch(Mmorpg mmorpg, Location location, Knowledge knowledge) {
        SphereEffect sphereEffect = new SphereEffect(Mmorpg.effectManager);
        sphereEffect.setLocation(location);
        sphereEffect.particle = Particle.REDSTONE;
        sphereEffect.color = Color.BLACK;
        sphereEffect.duration = 20 * 1000;
        sphereEffect.radius = 10;
        sphereEffect.particleCount = 50;

        sphereEffect.start();
        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                i++;
                location.getWorld().getNearbyEntities(location, 10, 10, 10, e -> e instanceof Player).forEach(p -> ((Player) p).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 3, false, false, false)));
                if (i == 15) cancel();
            }
        }.runTaskTimer(mmorpg, 0l, 20l);
    }
}
