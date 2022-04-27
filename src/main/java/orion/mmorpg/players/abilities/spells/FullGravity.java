package orion.mmorpg.players.abilities.spells;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.effects.ZoneSpellEffect;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.utils.Element;

public class FullGravity extends Spell{
    public FullGravity() {
        super("FULL_GRAVITY", 15, Element.BLACK, 60 * 1000);
    }

    @Override
    public void launch(Mmorpg mmorpg, Location location) {
        ZoneSpellEffect zoneSpellEffect = new ZoneSpellEffect(Mmorpg.effectManager);
        zoneSpellEffect.particle = Particle.SMOKE_NORMAL;
        zoneSpellEffect.duration = 15 * 1000;
        zoneSpellEffect.setLocation(location);
        zoneSpellEffect.load(ZoneSpellEffect.BaseImage.PENTAGRAM);
        zoneSpellEffect.start();
        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                i++;
                location.getWorld().getNearbyEntities(location, 10, 10, 10, e -> e instanceof Player).forEach(p -> ((Player) p).addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 128, false, false, false)));
                if (i == 15) cancel();
            }
        }.runTaskTimer(mmorpg, 0l, 20l);

    }
}
