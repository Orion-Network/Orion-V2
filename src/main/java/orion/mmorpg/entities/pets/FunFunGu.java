package orion.mmorpg.entities.pets;

import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import orion.mmorpg.entities.samples.AbstractPet;

public class FunFunGu extends AbstractPet {

    public FunFunGu(String uuid, Location spawnLocation, Player player) {
        super(EntityTypes.at, uuid, "FunFunGu", spawnLocation, player, Material.PUFFERFISH, "A funny pufferfish that will follow you everywhere");
    }
}
