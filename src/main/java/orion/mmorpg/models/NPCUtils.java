package orion.mmorpg.models;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;

public class NPCUtils {
    public static Slime getDummySentient(ServerLevel level) {
        return new Slime(EntityType.SLIME, level);
    }
    public static Mob getDummyInsentient(NPC from, Level world) {
        return new Mob(EntityType.VILLAGER, world) {
        };
    }

}
