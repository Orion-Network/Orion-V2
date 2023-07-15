package orion.mmorpg.entities.pets;

import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import orion.mmorpg.entities.Pets;
import orion.mmorpg.entities.samples.AbstractPet;

public class FunnyWolf extends AbstractPet {
    public FunnyWolf(String uuid, Location spawnLocation, Player player) {
        super(EntityType.WOLF, uuid, "Lil wolf", spawnLocation, player, Pets.FUNNYWOLF);
    }
}
