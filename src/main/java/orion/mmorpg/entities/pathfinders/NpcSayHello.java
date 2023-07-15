package orion.mmorpg.entities.pathfinders;

import net.minecraft.world.entity.ai.goal.Goal;
import org.bukkit.Bukkit;
import orion.mmorpg.models.NPC;

public class NpcSayHello extends Goal {
    NPC npc;
    public NpcSayHello(NPC npc) {
        this.npc = npc;
    }



    int i = 0;

    @Override
    public boolean canUse() {
        Bukkit.broadcastMessage("Hello, I am " + npc.getName());
        return true;
    }

    @Override
    public void start() {

        Bukkit.broadcastMessage("Hello, I am " + npc.getName() + " and I am level ");
        i++;
    }

    @Override
    public boolean canContinueToUse() {
        return i > 5;
    }
}
