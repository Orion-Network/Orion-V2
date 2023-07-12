package orion.mmorpg.entities.pets;

import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import orion.mmorpg.entities.Pets;
import orion.mmorpg.entities.samples.AbstractPet;

public class FunFunGu extends AbstractPet {

    public FunFunGu(String uuid, Location spawnLocation, Player player) {
        super(EntityType.PUFFERFISH, uuid, "FunFunGu", spawnLocation, player, Pets.FUNFUNGU);
    }
}
