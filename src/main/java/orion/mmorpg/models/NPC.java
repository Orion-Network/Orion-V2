package orion.mmorpg.models;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import lombok.Getter;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import orion.mmorpg.models.controllers.NpcJumpControl;
import orion.mmorpg.models.controllers.NpcMoveControl;
import orion.mmorpg.models.navigation.NpcPathNavigation;
import orion.mmorpg.models.pathfinders.TestGoal;

import java.util.Map;

public class NPC extends ServerPlayer {

    GoalSelector goalSelector;
    GoalSelector targetSelector;

    @Getter
    private final NpcJumpControl jumpControl;
    @Getter
    private final NpcMoveControl moveControl;
    @Getter
    NpcPathNavigation navigation;
    private final Map<BlockPathTypes, Float> pathfindingMalus;

    public NPC(MinecraftServer minecraftserver, ServerLevel worldserver, GameProfile gameprofile) {
        super(minecraftserver, worldserver, gameprofile);
        this.connection = new ServerGamePacketListenerImpl(minecraftserver, new Connection(PacketFlow.CLIENTBOUND), this);

        this.moveControl = new NpcMoveControl(this);
        this.jumpControl = new NpcJumpControl(this);

        this.pathfindingMalus = Maps.newEnumMap(BlockPathTypes.class);
        this.navigation = this.createNavigation(worldserver);

        this.goalSelector = new GoalSelector(worldserver.getProfilerSupplier());
        this.targetSelector = new GoalSelector(worldserver.getProfilerSupplier());

        generatePathfinder();
    }

    private NpcPathNavigation createNavigation(ServerLevel worldserver) {
        return new NpcPathNavigation(this, worldserver);
    }


    protected void generatePathfinder() {
        this.goalSelector.addGoal(0, new TestGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        doTick();
    }

    @Override
    public void doTick() {
        super.doTick();
        this.level().getProfiler().push("navigation");
        this.navigation.tick();
        this.level().getProfiler().pop();
        this.level().getProfiler().push("move");
        this.moveControl.tick();
        this.level().getProfiler().popPush("jump");
        this.jumpControl.tick();
        this.level().getProfiler().push("targetSelector");
        this.targetSelector.tick();
        this.level().getProfiler().pop();
        this.level().getProfiler().push("goalSelector");
        this.goalSelector.tick();
        this.level().getProfiler().pop();
    }


    public void setXxa(float f) {
        this.xxa = f;
    }

    public void setZza(float f) {
        this.zza = f;
    }

    protected boolean shouldPassengersInheritMalus() {
        return false;
    }


    public float getPathfindingMalus(BlockPathTypes pathtype) {
        NPC entityinsentient;
        if (this.getVehicle() instanceof NPC && ((NPC)this.getVehicle()).shouldPassengersInheritMalus()) {
            entityinsentient = (NPC) this.getVehicle();
        } else {
            entityinsentient = this;
        }

        Float ofloat = (Float)entityinsentient.pathfindingMalus.get(pathtype);
        return ofloat == null ? pathtype.getMalus() : ofloat;
    }

    public boolean canCutCorner(BlockPathTypes pathtype) {
        return pathtype != BlockPathTypes.DANGER_FIRE && pathtype != BlockPathTypes.DANGER_POWDER_SNOW && pathtype != BlockPathTypes.DANGER_OTHER && pathtype != BlockPathTypes.WALKABLE_DOOR;
    }

    public void setPathfindingMalus(BlockPathTypes pathtype, float f) {
        this.pathfindingMalus.put(pathtype, f);
    }

}
