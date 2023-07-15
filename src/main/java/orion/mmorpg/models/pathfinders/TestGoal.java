package orion.mmorpg.models.pathfinders;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Zombie;
import org.bukkit.Bukkit;
import orion.mmorpg.models.NPC;

public class TestGoal extends Goal {
    NPC npc;

    public TestGoal(NPC npc) {
        this.npc = npc;
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public void start() {
        npc.getNavigation().moveTo(397, 22, 905, 2);
    }

    @Override
    public boolean canContinueToUse() {
        return !npc.getNavigation().isDone();
    }

    @Override
    public void stop() {
        npc.getNavigation().stop();
    }
}
