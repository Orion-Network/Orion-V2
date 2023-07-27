package orion.mmorpg.models.navigation;

import com.google.common.collect.ImmutableSet;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.Vec3;
import orion.mmorpg.models.NPC;
import orion.mmorpg.models.NPCUtils;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NpcPathNavigation extends PathNavigation {
    private static final int MAX_TIME_RECOMPUTE = 20;
    protected final NPC mob;
    protected final Level level;
    @Nullable
    protected Path path;
    protected double speedModifier;
    protected int tick;
    protected int lastStuckCheck;
    protected Vec3 lastStuckCheckPos;
    protected Vec3i timeoutCachedNode;
    protected long timeoutTimer;
    protected long lastTimeoutCheck;
    protected double timeoutLimit;
    protected float maxDistanceToWaypoint;
    protected boolean hasDelayedRecomputation;
    protected long timeLastRecompute;
    protected NpcWalkNodeEvaluator nodeEvaluator;
    private BlockPos targetPos;
    private int reachRange;
    private float maxVisitedNodesMultiplier;
    private final NpcPathFinder pathFinder;
    private boolean isStuck;

    public NpcPathNavigation(NPC var0, Level var1) {
        super(NPCUtils.getDummyInsentient(var0, var1), var1);
        this.lastStuckCheckPos = Vec3.ZERO;
        this.timeoutCachedNode = Vec3i.ZERO;
        this.maxDistanceToWaypoint = 0.5F;
        this.maxVisitedNodesMultiplier = 1.0F;
        this.mob = var0;
        this.level = var1;
        this.nodeEvaluator = new NpcWalkNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        //int var2 = Mth.floor(var0.getAttributeValue(Attributes.FOLLOW_RANGE) * 16.0);
        this.pathFinder = new NpcPathFinder(this.nodeEvaluator, 24);
    }

    public void resetMaxVisitedNodesMultiplier() {
        this.maxVisitedNodesMultiplier = 1.0F;
    }

    public void setMaxVisitedNodesMultiplier(float var0) {
        this.maxVisitedNodesMultiplier = var0;
    }

    public BlockPos getTargetPos() {
        return this.targetPos;
    }

    @Override
    protected PathFinder createPathFinder(int i) {
        return null;
    }

    public void setSpeedModifier(double var0) {
        this.speedModifier = var0;
    }

    public boolean hasDelayedRecomputation() {
        return this.hasDelayedRecomputation;
    }

    public void recomputePath() {
        if (this.level.getGameTime() - this.timeLastRecompute > 20L) {
            if (this.targetPos != null) {
                this.path = null;
                this.path = this.createPath(this.targetPos, this.reachRange);
                this.timeLastRecompute = this.level.getGameTime();
                this.hasDelayedRecomputation = false;
            }
        } else {
            this.hasDelayedRecomputation = true;
        }

    }

    @Nullable
    public Path createPath(Stream<BlockPos> var0, int var1) {
        return this.createPath((Set)var0.collect(Collectors.toSet()), 8, false, var1);
    }

    @Nullable
    public Path createPath(Set<BlockPos> var0, int var1) {
        return this.createPath(var0, 8, false, var1);
    }

    @Nullable
    public Path createPath(BlockPos var0, int var1) {
        return this.createPath(ImmutableSet.of(var0), 8, false, var1);
    }

    @Nullable
    public Path createPath(BlockPos var0, int var1, int var2) {
        return this.createPath(ImmutableSet.of(var0), 8, false, var1, (float)var2);
    }

    @Nullable
    public Path createPath(Entity var0, int var1) {
        return this.createPath(ImmutableSet.of(var0.blockPosition()), 16, true, var1);
    }

    @Nullable
    protected Path createPath(Set<BlockPos> var0, int var1, boolean var2, int var3) {
        return this.createPath(var0, var1, var2, var3, (float)24);
    }

    @Nullable
    protected Path createPath(Set<BlockPos> var0, int var1, boolean var2, int var3, float var4) {
        if (var0.isEmpty()) {
            return null;
        } else if (this.mob.getY() < (double)this.level.getMinBuildHeight()) {
            return null;
        } else if (!this.canUpdatePath()) {
            return null;
        } else if (this.path != null && !this.path.isDone() && var0.contains(this.targetPos)) {
            return this.path;
        } else {
            this.level.getProfiler().push("pathfind");
            BlockPos var5 = var2 ? this.mob.blockPosition().north() : this.mob.blockPosition();
            int var6 = (int)(var4 + (float)var1);
            PathNavigationRegion var7 = new PathNavigationRegion(this.level, var5.offset(-var6, -var6, -var6), var5.offset(var6, var6, var6));
            Path var8 = this.pathFinder.findPath(var7, this.mob, var0, var4, var3, this.maxVisitedNodesMultiplier);
            this.level.getProfiler().pop();
            if (var8 != null && var8.getTarget() != null) {
                this.targetPos = var8.getTarget();
                this.reachRange = var3;
                this.resetStuckTimeout();
            }

            return var8;
        }
    }

    public boolean moveTo(double var0, double var2, double var4, double var6) {
        return this.moveTo(this.createPath(var0, var2, var4, 1), var6);
    }

    public boolean moveTo(Entity var0, double var1) {
        Path var3 = this.createPath((Entity)var0, 1);
        return var3 != null && this.moveTo(var3, var1);
    }

    public boolean moveTo(@Nullable Path var0, double var1) {
        if (var0 == null) {
            this.path = null;
            return false;
        } else {
            if (!var0.sameAs(this.path)) {
                this.path = var0;
            }

            if (this.isDone()) {
                return false;
            } else {
                this.trimPath();
                if (this.path.getNodeCount() <= 0) {
                    return false;
                } else {
                    this.speedModifier = var1;
                    Vec3 var3 = this.getTempMobPos();
                    this.lastStuckCheck = this.tick;
                    this.lastStuckCheckPos = var3;
                    return true;
                }
            }
        }
    }

    @Nullable
    public Path getPath() {
        return this.path;
    }

    public void tick() {
        ++this.tick;
        if (this.hasDelayedRecomputation) {
            this.recomputePath();
        }

        if (!this.isDone()) {
            Vec3 var0;
            if (this.canUpdatePath()) {
                this.followThePath();
            } else if (this.path != null && !this.path.isDone()) {
                var0 = this.getTempMobPos();
                Vec3 var1 = this.path.getNextEntityPos(this.mob);
                if (var0.y > var1.y && !this.mob.onGround() && Mth.floor(var0.x) == Mth.floor(var1.x) && Mth.floor(var0.z) == Mth.floor(var1.z)) {
                    this.path.advance();
                }
            }

            //DebugPackets.sendPathFindingPacket(this.level, this.mob, this.path, this.maxDistanceToWaypoint);
            if (!this.isDone()) {
                var0 = this.path.getNextEntityPos(this.mob);
                BlockPos var1 = new BlockPos(new Vec3i((int) var0.x, (int) var0.y, (int) var0.z));
                this.mob.getMoveControl().setWantedPosition(var0.x, this.level.getBlockState(var1.below()).isAir() ? var0.y : WalkNodeEvaluator.getFloorLevel(this.level, var1), var0.z, this.speedModifier);
            }
        }
    }

    protected void followThePath() {
        Vec3 var0 = this.getTempMobPos();
        this.maxDistanceToWaypoint = this.mob.getBbWidth() > 0.75F ? this.mob.getBbWidth() / 2.0F : 0.75F - this.mob.getBbWidth() / 2.0F;
        Vec3i var1 = this.path.getNextNodePos();
        double var2 = Math.abs(this.mob.getX() - ((double)var1.getX() + 0.5));
        double var4 = Math.abs(this.mob.getY() - (double)var1.getY());
        double var6 = Math.abs(this.mob.getZ() - ((double)var1.getZ() + 0.5));
        boolean var8 = var2 < (double)this.maxDistanceToWaypoint && var6 < (double)this.maxDistanceToWaypoint && var4 < 1.0;
        if (var8 || this.mob.canCutCorner(this.path.getNextNode().type) && this.shouldTargetNextNodeInDirection(var0)) {
            this.path.advance();
        }

        this.doStuckDetection(var0);
    }

    private boolean shouldTargetNextNodeInDirection(Vec3 var0) {
        if (this.path.getNextNodeIndex() + 1 >= this.path.getNodeCount()) {
            return false;
        } else {
            Vec3 var1 = Vec3.atBottomCenterOf(this.path.getNextNodePos());
            if (!var0.closerThan(var1, 2.0)) {
                return false;
            } else {
                Vec3 var2 = Vec3.atBottomCenterOf(this.path.getNodePos(this.path.getNextNodeIndex() + 1));
                Vec3 var3 = var2.subtract(var1);
                Vec3 var4 = var0.subtract(var1);
                return var3.dot(var4) > 0.0;
            }
        }
    }

    protected void doStuckDetection(Vec3 var0) {
        if (this.tick - this.lastStuckCheck > 100) {
            if (var0.distanceToSqr(this.lastStuckCheckPos) < 2.25) {
                this.isStuck = true;
                this.stop();
            } else {
                this.isStuck = false;
            }

            this.lastStuckCheck = this.tick;
            this.lastStuckCheckPos = var0;
        }

        if (this.path != null && !this.path.isDone()) {
            Vec3i var1 = this.path.getNextNodePos();
            if (var1.equals(this.timeoutCachedNode)) {
                this.timeoutTimer += Util.getMillis() - this.lastTimeoutCheck;
            } else {
                this.timeoutCachedNode = var1;
                double var2 = var0.distanceTo(Vec3.atBottomCenterOf(this.timeoutCachedNode));
                this.timeoutLimit = this.mob.getSpeed() > 0.0F ? var2 / (double)this.mob.getSpeed() * 1000.0 : 0.0;
            }

            if (this.timeoutLimit > 0.0 && (double)this.timeoutTimer > this.timeoutLimit * 3.0) {
                this.timeoutPath();
            }

            this.lastTimeoutCheck = Util.getMillis();
        }

    }

    private void timeoutPath() {
        this.resetStuckTimeout();
        this.stop();
    }

    private void resetStuckTimeout() {
        this.timeoutCachedNode = Vec3i.ZERO;
        this.timeoutTimer = 0L;
        this.timeoutLimit = 0.0;
        this.isStuck = false;
    }

    public boolean isDone() {
        return this.path == null || this.path.isDone();
    }

    public boolean isInProgress() {
        return !this.isDone();
    }

    public void stop() {
        this.path = null;
    }

    protected Vec3 getTempMobPos() {
        return new Vec3(this.mob.getX(), (double)this.getSurfaceY(), this.mob.getZ());
    }

    private int getSurfaceY() {
        if (this.mob.isInWater() && this.canFloat()) {
            int var0 = this.mob.getBlockY();
            BlockState var1 = this.level.getBlockState(new BlockPos(this.mob.getBlockX(),  var0, this.mob.getBlockZ()));
            int var2 = 0;

            do {
                if (!var1.is(Blocks.WATER)) {
                    return var0;
                }

                ++var0;
                var1 = this.level.getBlockState(new BlockPos(this.mob.getBlockX(), var0, this.mob.getBlockZ()));
                ++var2;
            } while(var2 <= 16);

            return this.mob.getBlockY();
        } else {
            return Mth.floor(this.mob.getY() + 0.5);
        }
    }

    protected boolean canUpdatePath() {
        return this.mob.onGround() || this.isInLiquid() || this.mob.isPassenger();
    }

    protected boolean isInLiquid() {
        return this.mob.isInWaterOrBubble() || this.mob.isInLava();
    }

    protected void trimPath() {
        if (this.path != null) {
            for(int var0 = 0; var0 < this.path.getNodeCount(); ++var0) {
                Node var1 = this.path.getNode(var0);
                Node var2 = var0 + 1 < this.path.getNodeCount() ? this.path.getNode(var0 + 1) : null;
                BlockState var3 = this.level.getBlockState(new BlockPos(var1.x, var1.y, var1.z));
                if (var3.is(BlockTags.CAULDRONS)) {
                    this.path.replaceNode(var0, var1.cloneAndMove(var1.x, var1.y + 1, var1.z));
                    if (var2 != null && var1.y >= var2.y) {
                        this.path.replaceNode(var0 + 1, var1.cloneAndMove(var2.x, var1.y + 1, var2.z));
                    }
                }
            }

        }
    }
    protected boolean canMoveDirectly(Vec3 var0, Vec3 var1, int var2, int var3, int var4) {
        int var5 = Mth.floor(var0.x);
        int var6 = Mth.floor(var0.z);
        double var7 = var1.x - var0.x;
        double var9 = var1.z - var0.z;
        double var11 = var7 * var7 + var9 * var9;
        if (var11 < 1.0E-8) {
            return false;
        } else {
            double var13 = 1.0 / Math.sqrt(var11);
            var7 *= var13;
            var9 *= var13;
            var2 += 2;
            var4 += 2;
            if (!this.canWalkOn(var5, Mth.floor(var0.y), var6, var2, var3, var4, var0, var7, var9)) {
                return false;
            } else {
                var2 -= 2;
                var4 -= 2;
                double var15 = 1.0 / Math.abs(var7);
                double var17 = 1.0 / Math.abs(var9);
                double var19 = (double)var5 - var0.x;
                double var21 = (double)var6 - var0.z;
                if (var7 >= 0.0) {
                    ++var19;
                }

                if (var9 >= 0.0) {
                    ++var21;
                }

                var19 /= var7;
                var21 /= var9;
                int var23 = var7 < 0.0 ? -1 : 1;
                int var24 = var9 < 0.0 ? -1 : 1;
                int var25 = Mth.floor(var1.x);
                int var26 = Mth.floor(var1.z);
                int var27 = var25 - var5;
                int var28 = var26 - var6;

                do {
                    if (var27 * var23 <= 0 && var28 * var24 <= 0) {
                        return true;
                    }

                    if (var19 < var21) {
                        var19 += var15;
                        var5 += var23;
                        var27 = var25 - var5;
                    } else {
                        var21 += var17;
                        var6 += var24;
                        var28 = var26 - var6;
                    }
                } while(this.canWalkOn(var5, Mth.floor(var0.y), var6, var2, var3, var4, var0, var7, var9));

                return false;
            }
        }
    }

    private boolean canWalkOn(int var0, int var1, int var2, int var3, int var4, int var5, Vec3 var6, double var7, double var9) {
        int var11 = var0 - var3 / 2;
        int var12 = var2 - var5 / 2;
        if (!this.canWalkAbove(var11, var1, var12, var3, var4, var5, var6, var7, var9)) {
            return false;
        } else {
            for(int var13 = var11; var13 < var11 + var3; ++var13) {
                for(int var14 = var12; var14 < var12 + var5; ++var14) {
                    double var15 = (double)var13 + 0.5 - var6.x;
                    double var17 = (double)var14 + 0.5 - var6.z;
                    if (!(var15 * var7 + var17 * var9 < 0.0)) {
                        BlockPathTypes var19 = this.nodeEvaluator.getBlockPathType(this.level, var13, var1 - 1, var14, this.mob, var3, var4, var5, true, true);
                        if (!this.hasValidPathType(var19)) {
                            return false;
                        }

                        var19 = this.nodeEvaluator.getBlockPathType(this.level, var13, var1, var14, this.mob, var3, var4, var5, true, true);
                        float var20 = this.mob.getPathfindingMalus(var19);
                        if (var20 < 0.0F || var20 >= 8.0F) {
                            return false;
                        }

                        if (var19 == BlockPathTypes.DAMAGE_FIRE || var19 == BlockPathTypes.DANGER_FIRE || var19 == BlockPathTypes.DAMAGE_OTHER) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    protected boolean hasValidPathType(BlockPathTypes var0) {
        if (var0 == BlockPathTypes.WATER) {
            return false;
        } else if (var0 == BlockPathTypes.LAVA) {
            return false;
        } else {
            return var0 != BlockPathTypes.OPEN;
        }
    }

    private boolean canWalkAbove(int var0, int var1, int var2, int var3, int var4, int var5, Vec3 var6, double var7, double var9) {
        Iterator var12 = BlockPos.betweenClosed(new BlockPos(var0, var1, var2), new BlockPos(var0 + var3 - 1, var1 + var4 - 1, var2 + var5 - 1)).iterator();

        BlockPos var120;
        double var13;
        double var15;
        do {
            if (!var12.hasNext()) {
                return true;
            }

            var120 = (BlockPos)var12.next();
            var13 = (double)var120.getX() + 0.5 - var6.x;
            var15 = (double)var120.getZ() + 0.5 - var6.z;
        } while(var13 * var7 + var15 * var9 < 0.0 || this.level.getBlockState(var120).isPathfindable(this.level, var120, PathComputationType.LAND));

        return false;
    }

    public boolean isStableDestination(BlockPos var0) {
        BlockPos var1 = var0.below();
        return this.level.getBlockState(var1).isSolidRender(this.level, var1);
    }

    public NodeEvaluator getNodeEvaluator() {
        return this.nodeEvaluator;
    }

    public void setCanFloat(boolean var0) {
        this.nodeEvaluator.setCanFloat(var0);
    }

    public boolean canFloat() {
        return this.nodeEvaluator.canFloat();
    }

    public void recomputePath(BlockPos var0) {
        if (this.path != null && !this.path.isDone() && this.path.getNodeCount() != 0) {
            Node var1 = this.path.getEndNode();
            Vec3i var2 = new Vec3i((int) ((var1.x + this.mob.getX()) / 2.0),  (int)(((var1.y + this.mob.getY()) / 2.0)), (int)((var1.z + this.mob.getZ()) / 2.0));
            if (var0.closerThan(var2, (double)(this.path.getNodeCount() - this.path.getNextNodeIndex()))) {
                this.recomputePath();
            }

        }
    }

    public float getMaxDistanceToWaypoint() {
        return this.maxDistanceToWaypoint;
    }

    public boolean isStuck() {
        return this.isStuck;
    }
}
