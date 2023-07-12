package orion.mmorpg.models;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import orion.mmorpg.entities.pathfinders.NpcSayHello;

public class NPC extends ServerPlayer {

    GoalSelector goalSelector;
    GoalSelector targetSelector;

    public NPC(MinecraftServer minecraftserver, ServerLevel worldserver, GameProfile gameprofile) {
        super(minecraftserver, worldserver, gameprofile);
        this.connection = new ServerGamePacketListenerImpl(minecraftserver, new Connection(PacketFlow.CLIENTBOUND), this);

        this.goalSelector = new GoalSelector(worldserver.getProfilerSupplier());
        this.targetSelector = new GoalSelector(worldserver.getProfilerSupplier());

        generatePathfinder();
    }


    protected void generatePathfinder() {
        this.goalSelector.addGoal(0, new NpcSayHello(this));
    }

    @Override
    public void tick() {
        super.tick();
        doTick();
    }



    @Override
    public void doTick() {
        super.doTick();
        this.level.getProfiler().push("brain");
        this.targetSelector.tick();
        this.level.getProfiler().pop();
        this.level.getProfiler().push("goalSelector");
        this.goalSelector.tick();
        this.level.getProfiler().pop();
    }


}
