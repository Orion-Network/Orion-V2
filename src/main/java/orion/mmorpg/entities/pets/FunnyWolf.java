package orion.mmorpg.entities.pets;

import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import orion.mmorpg.entities.Pets;
import orion.mmorpg.entities.samples.AbstractPet;

public class FunnyWolf extends AbstractPet {
    public FunnyWolf(String uuid, Location spawnLocation, Player player) {
        super(EntityTypes.bc, uuid, "Lil wolf", spawnLocation, player, Pets.FUNNYWOLF);
    }
}
