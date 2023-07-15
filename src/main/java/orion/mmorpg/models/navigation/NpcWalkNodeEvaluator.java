package orion.mmorpg.models.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.Target;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import orion.mmorpg.models.NPC;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Iterator;

public class NpcWalkNodeEvaluator extends NpcNodeEvaluator {
    public static final double SPACE_BETWEEN_WALL_POSTS = 0.5;
    protected float oldWaterCost;
    private final Long2ObjectMap<BlockPathTypes> l = new Long2ObjectOpenHashMap();
    private final Object2BooleanMap<AABB> m = new Object2BooleanOpenHashMap();

    public void prepare(PathNavigationRegion var0, Mob var1) {
        super.prepare(var0, var1);
        this.oldWaterCost = var1.getPathfindingMalus(BlockPathTypes.WATER);
    }

    public void done() {
        this.mob.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
        this.l.clear();
        this.m.clear();
        super.done();
    }

    public Node getStart() {
        BlockPos.MutableBlockPos var1 = new BlockPos.MutableBlockPos();
        int var0 = this.mob.getBlockY();
        BlockState var2 = this.level.getBlockState(var1.set(this.mob.getX(), (double)var0, this.mob.getZ()));
        BlockPos var3;
        if (!this.mob.canStandOnFluid(var2.getFluidState().getType())) {
            if (this.canFloat() && this.mob.isInWater()) {
                while(true) {
                    if (!var2.is(Blocks.WATER) && var2.getFluidState() != Fluids.WATER.getSource(false)) {
                        --var0;
                        break;
                    }

                    ++var0;
                    var2 = this.level.getBlockState(var1.set(this.mob.getX(), (double)var0, this.mob.getZ()));
                }
            } else if (this.mob.isOnGround()) {
                var0 = Mth.floor(this.mob.getY() + 0.5);
            } else {
                for(var3 = this.mob.blockPosition(); (this.level.getBlockState(var3).isAir() || this.level.getBlockState(var3).isPathfindable(this.level, var3, PathComputationType.LAND)) && var3.getY() > this.mob.level.getMinBuildHeight(); var3 = var3.down()) {
                }

                var0 = var3.up().getY();
            }
        } else {
            while(true) {
                if (!this.mob.canStandOnFluid(var2.getFluidState().getType())) {
                    --var0;
                    break;
                }

                ++var0;
                var2 = this.level.getBlockState(var1.set(this.mob.getX(), (double)var0, this.mob.getZ()));
            }
        }

        var3 = this.mob.blockPosition();
        BlockPathTypes var4 = this.getCachedBlockType(this.mob, var3.getX(), var0, var3.getZ());
        if (this.mob.getPathfindingMalus(var4) < 0.0F) {
            AABB var5 = this.mob.getBoundingBox();
            if (this.hasPositiveMalus(var1.set(var5.minX, (double)var0, var5.minZ)) || this.hasPositiveMalus(var1.set(var5.minX, (double)var0, var5.maxZ)) || this.hasPositiveMalus(var1.set(var5.maxX, (double)var0, var5.minZ)) || this.hasPositiveMalus(var1.set(var5.maxX, (double)var0, var5.maxZ))) {
                Node var6 = this.getNode(var1);
                var6.type = this.getBlockPathType(this.mob, var6.asBlockPos());
                var6.costMalus = this.mob.getPathfindingMalus(var6.type);
                return var6;
            }
        }

        Node var5 = this.getNode(var3.getX(), var0, var3.getZ());
        var5.type = this.getBlockPathType(this.mob, var5.asBlockPos());
        var5.costMalus = this.mob.getPathfindingMalus(var5.type);
        return var5;
    }

    private boolean hasPositiveMalus(BlockPos var0) {
        BlockPathTypes var1 = this.getBlockPathType(this.mob, var0);
        return this.mob.getPathfindingMalus(var1) >= 0.0F;
    }

    public Target getGoal(double var0, double var2, double var4) {
        return new Target(this.getNode(Mth.floor(var0), Mth.floor(var2), Mth.floor(var4)));
    }

    public int getNeighbors(Node[] var0, Node var1) {
        int var2 = 0;
        int var3 = 0;
        BlockPathTypes var4 = this.getCachedBlockType(this.mob, var1.x, var1.y + 1, var1.z);
        BlockPathTypes var5 = this.getCachedBlockType(this.mob, var1.x, var1.y, var1.z);
        if (this.mob.getPathfindingMalus(var4) >= 0.0F && var5 != BlockPathTypes.STICKY_HONEY) {
            var3 = Mth.floor(Math.max(1.0F, this.mob.maxUpStep));
        }

        double var6 = this.getFloorLevel(new BlockPos(var1.x, var1.y, var1.z));
        Node var8 = this.findAcceptedNode(var1.x, var1.y, var1.z + 1, var3, var6, Direction.SOUTH, var5);
        if (this.isNeighborValid(var8, var1)) {
            var0[var2++] = var8;
        }

        Node var9 = this.findAcceptedNode(var1.x - 1, var1.y, var1.z, var3, var6, Direction.WEST, var5);
        if (this.isNeighborValid(var9, var1)) {
            var0[var2++] = var9;
        }

        Node var10 = this.findAcceptedNode(var1.x + 1, var1.y, var1.z, var3, var6, Direction.EAST, var5);
        if (this.isNeighborValid(var10, var1)) {
            var0[var2++] = var10;
        }

        Node var11 = this.findAcceptedNode(var1.x, var1.y, var1.z - 1, var3, var6, Direction.NORTH, var5);
        if (this.isNeighborValid(var11, var1)) {
            var0[var2++] = var11;
        }

        Node var12 = this.findAcceptedNode(var1.x - 1, var1.y, var1.z - 1, var3, var6, Direction.NORTH, var5);
        if (this.isDiagonalValid(var1, var9, var11, var12)) {
            var0[var2++] = var12;
        }

        Node var13 = this.findAcceptedNode(var1.x + 1, var1.y, var1.z - 1, var3, var6, Direction.NORTH, var5);
        if (this.isDiagonalValid(var1, var10, var11, var13)) {
            var0[var2++] = var13;
        }

        Node var14 = this.findAcceptedNode(var1.x - 1, var1.y, var1.z + 1, var3, var6, Direction.SOUTH, var5);
        if (this.isDiagonalValid(var1, var9, var8, var14)) {
            var0[var2++] = var14;
        }

        Node var15 = this.findAcceptedNode(var1.x + 1, var1.y, var1.z + 1, var3, var6, Direction.SOUTH, var5);
        if (this.isDiagonalValid(var1, var10, var8, var15)) {
            var0[var2++] = var15;
        }

        return var2;
    }

    protected boolean isNeighborValid(@Nullable Node var0, Node var1) {
        return var0 != null && !var0.closed && (var0.costMalus >= 0.0F || var1.costMalus < 0.0F);
    }

    protected boolean isDiagonalValid(Node var0, @Nullable Node var1, @Nullable Node var2, @Nullable Node var3) {
        if (var3 != null && var2 != null && var1 != null) {
            if (var3.closed) {
                return false;
            } else if (var2.y <= var0.y && var1.y <= var0.y) {
                if (var1.type != BlockPathTypes.WALKABLE_DOOR && var2.type != BlockPathTypes.WALKABLE_DOOR && var3.type != BlockPathTypes.WALKABLE_DOOR) {
                    boolean var4 = var2.type == BlockPathTypes.FENCE && var1.type == BlockPathTypes.FENCE && (double)this.mob.getBbWidth() < 0.5;
                    return var3.costMalus >= 0.0F && (var2.y < var0.y || var2.costMalus >= 0.0F || var4) && (var1.y < var0.y || var1.costMalus >= 0.0F || var4);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean canReachWithoutCollision(Node var0) {
        Vec3 var1 = new Vec3((double)var0.x - this.mob.getX(), (double)var0.y - this.mob.getY(), (double)var0.z - this.mob.getZ());
        AABB var2 = this.mob.getBoundingBox();
        int var3 = Mth.ceil(var1.length() / var2.getSize());
        var1 = var1.scale((double)(1.0F / (float)var3));

        for(int var4 = 1; var4 <= var3; ++var4) {
            var2 = var2.move(var1);
            if (this.hasCollisions(var2)) {
                return false;
            }
        }

        return true;
    }

    protected double getFloorLevel(BlockPos var0) {
        return getFloorLevel(this.level, var0);
    }

    public static double getFloorLevel(BlockGetter var0, BlockPos var1) {
        BlockPos var2 = var1.down();
        VoxelShape var3 = var0.getBlockState(var2).getCollisionShape(var0, var2);
        return (double)var2.getY() + (var3.isEmpty() ? 0.0 : var3.max(Direction.Axis.Y));
    }

    protected boolean isAmphibious() {
        return false;
    }

    @Nullable
    protected Node findAcceptedNode(int var0, int var1, int var2, int var3, double var4, Direction var6, BlockPathTypes var7) {
        Node var8 = null;
        BlockPos.MutableBlockPos var9 = new BlockPos.MutableBlockPos();
        double var10 = this.getFloorLevel(var9.set(var0, var1, var2));
        if (var10 - var4 > 1.125) {
            return null;
        } else {
            BlockPathTypes var12 = this.getCachedBlockType(this.mob, var0, var1, var2);
            float var13 = this.mob.getPathfindingMalus(var12);
            double var14 = (double)this.mob.getBbWidth() / 2.0;
            if (var13 >= 0.0F) {
                var8 = this.getNode(var0, var1, var2);
                var8.type = var12;
                var8.costMalus = Math.max(var8.costMalus, var13);
            }

            if (var7 == BlockPathTypes.FENCE && var8 != null && var8.costMalus >= 0.0F && !this.canReachWithoutCollision(var8)) {
                var8 = null;
            }

            if (var12 == BlockPathTypes.WALKABLE || this.isAmphibious() && var12 == BlockPathTypes.WATER) {
                return var8;
            } else {
                if ((var8 == null || var8.costMalus < 0.0F) && var3 > 0 && var12 != BlockPathTypes.FENCE && var12 != BlockPathTypes.UNPASSABLE_RAIL && var12 != BlockPathTypes.TRAPDOOR && var12 != BlockPathTypes.POWDER_SNOW) {
                    var8 = this.findAcceptedNode(var0, var1 + 1, var2, var3 - 1, var4, var6, var7);
                    if (var8 != null && (var8.type == BlockPathTypes.OPEN || var8.type == BlockPathTypes.WALKABLE) && this.mob.getBbWidth() < 1.0F) {
                        double var16 = (double)(var0 - var6.getStepX()) + 0.5;
                        double var18 = (double)(var2 - var6.getStepZ()) + 0.5;
                        AABB var20 = new AABB(var16 - var14, getFloorLevel(this.level, var9.set(var16, (double)(var1 + 1), var18)) + 0.001, var18 - var14, var16 + var14, (double)this.mob.getBbHeight() + getFloorLevel(this.level, var9.set((double)var8.x, (double)var8.y, (double)var8.z)) - 0.002, var18 + var14);
                        if (this.hasCollisions(var20)) {
                            var8 = null;
                        }
                    }
                }

                if (!this.isAmphibious() && var12 == BlockPathTypes.WATER && !this.canFloat()) {
                    if (this.getCachedBlockType(this.mob, var0, var1 - 1, var2) != BlockPathTypes.WATER) {
                        return var8;
                    }

                    while(var1 > this.mob.level.getMinBuildHeight()) {
                        --var1;
                        var12 = this.getCachedBlockType(this.mob, var0, var1, var2);
                        if (var12 != BlockPathTypes.WATER) {
                            return var8;
                        }

                        var8 = this.getNode(var0, var1, var2);
                        var8.type = var12;
                        var8.costMalus = Math.max(var8.costMalus, this.mob.getPathfindingMalus(var12));
                    }
                }

                if (var12 == BlockPathTypes.OPEN) {
                    int var16 = 0;
                    int var17 = var1;

                    while(var12 == BlockPathTypes.OPEN) {
                        --var1;
                        Node var18;
                        if (var1 < this.mob.level.getMinBuildHeight()) {
                            var18 = this.getNode(var0, var17, var2);
                            var18.type = BlockPathTypes.BLOCKED;
                            var18.costMalus = -1.0F;
                            return var18;
                        }

                        if (var16++ >= this.mob.getMaxFallDistance()) {
                            var18 = this.getNode(var0, var1, var2);
                            var18.type = BlockPathTypes.BLOCKED;
                            var18.costMalus = -1.0F;
                            return var18;
                        }

                        var12 = this.getCachedBlockType(this.mob, var0, var1, var2);
                        var13 = this.mob.getPathfindingMalus(var12);
                        if (var12 != BlockPathTypes.OPEN && var13 >= 0.0F) {
                            var8 = this.getNode(var0, var1, var2);
                            var8.type = var12;
                            var8.costMalus = Math.max(var8.costMalus, var13);
                            break;
                        }

                        if (var13 < 0.0F) {
                            var18 = this.getNode(var0, var1, var2);
                            var18.type = BlockPathTypes.BLOCKED;
                            var18.costMalus = -1.0F;
                            return var18;
                        }
                    }
                }

                if (var12 == BlockPathTypes.FENCE) {
                    var8 = this.getNode(var0, var1, var2);
                    var8.closed = true;
                    var8.type = var12;
                    var8.costMalus = var12.getMalus();
                }

                return var8;
            }
        }
    }

    private boolean hasCollisions(AABB var0) {
        return (Boolean)this.m.computeIfAbsent(var0, (var1) -> {
            return !this.level.noCollision(this.mob, var0);
        });
    }

    public BlockPathTypes getBlockPathType(BlockGetter var0, int var1, int var2, int var3, Mob var4, int var5, int var6, int var7, boolean var8, boolean var9) {
        EnumSet<BlockPathTypes> var10 = EnumSet.noneOf(BlockPathTypes.class);
        BlockPathTypes var11 = BlockPathTypes.BLOCKED;
        BlockPos var12 = var4.blockPosition();
        var11 = this.getBlockPathTypes(var0, var1, var2, var3, var5, var6, var7, var8, var9, var10, var11, var12);
        if (var10.contains(BlockPathTypes.FENCE)) {
            return BlockPathTypes.FENCE;
        } else if (var10.contains(BlockPathTypes.UNPASSABLE_RAIL)) {
            return BlockPathTypes.UNPASSABLE_RAIL;
        } else {
            BlockPathTypes var13 = BlockPathTypes.BLOCKED;
            Iterator var15 = var10.iterator();

            while(var15.hasNext()) {
                BlockPathTypes var150 = (BlockPathTypes)var15.next();
                if (var4.getPathfindingMalus(var150) < 0.0F) {
                    return var150;
                }

                if (var4.getPathfindingMalus(var150) >= var4.getPathfindingMalus(var13)) {
                    var13 = var150;
                }
            }

            if (var11 == BlockPathTypes.OPEN && var4.getPathfindingMalus(var13) == 0.0F && var5 <= 1) {
                return BlockPathTypes.OPEN;
            } else {
                return var13;
            }
        }
    }

    public BlockPathTypes getBlockPathTypes(BlockGetter var0, int var1, int var2, int var3, int var4, int var5, int var6, boolean var7, boolean var8, EnumSet<BlockPathTypes> var9, BlockPathTypes var10, BlockPos var11) {
        for(int var12 = 0; var12 < var4; ++var12) {
            for(int var13 = 0; var13 < var5; ++var13) {
                for(int var14 = 0; var14 < var6; ++var14) {
                    int var15 = var12 + var1;
                    int var16 = var13 + var2;
                    int var17 = var14 + var3;
                    BlockPathTypes var18 = this.getBlockPathType(var0, var15, var16, var17);
                    var18 = this.evaluateBlockPathType(var0, var7, var8, var11, var18);
                    if (var12 == 0 && var13 == 0 && var14 == 0) {
                        var10 = var18;
                    }

                    var9.add(var18);
                }
            }
        }

        return var10;
    }

    protected BlockPathTypes evaluateBlockPathType(BlockGetter var0, boolean var1, boolean var2, BlockPos var3, BlockPathTypes var4) {
        if (var4 == BlockPathTypes.DOOR_WOOD_CLOSED && var1 && var2) {
            var4 = BlockPathTypes.WALKABLE_DOOR;
        }

        if (var4 == BlockPathTypes.DOOR_OPEN && !var2) {
            var4 = BlockPathTypes.BLOCKED;
        }

        if (var4 == BlockPathTypes.RAIL && !(var0.getBlockState(var3).getBlock() instanceof BaseRailBlock) && !(var0.getBlockState(var3.down()).getBlock() instanceof BaseRailBlock)) {
            var4 = BlockPathTypes.UNPASSABLE_RAIL;
        }

        if (var4 == BlockPathTypes.LEAVES) {
            var4 = BlockPathTypes.BLOCKED;
        }

        return var4;
    }

    private BlockPathTypes getBlockPathType(Mob var0, BlockPos var1) {
        return this.getCachedBlockType(var0, var1.getX(), var1.getY(), var1.getZ());
    }

    private BlockPathTypes getBlockPathType(NPC var0, BlockPos var1) {
        return getCachedBlockType(var0, var1.getX(), var1.getY(), var1.getZ());
    }

    public BlockPathTypes getBlockPathType(BlockGetter var0, int var1, int var2, int var3, NPC var4, int var5, int var6, int var7, boolean var8, boolean var9) {
        EnumSet<BlockPathTypes> var10 = EnumSet.noneOf(BlockPathTypes.class);
        BlockPathTypes var11 = BlockPathTypes.BLOCKED;
        BlockPos var12 = var4.blockPosition();
        var11 = this.getBlockPathTypes(var0, var1, var2, var3, var5, var6, var7, var8, var9, var10, var11, var12);
        if (var10.contains(BlockPathTypes.FENCE)) {
            return BlockPathTypes.FENCE;
        } else if (var10.contains(BlockPathTypes.UNPASSABLE_RAIL)) {
            return BlockPathTypes.UNPASSABLE_RAIL;
        } else {
            BlockPathTypes var13 = BlockPathTypes.BLOCKED;
            Iterator var15 = var10.iterator();

            while(var15.hasNext()) {
                BlockPathTypes var150 = (BlockPathTypes)var15.next();
                if (var4.getPathfindingMalus(var150) < 0.0F) {
                    return var150;
                }

                if (var4.getPathfindingMalus(var150) >= var4.getPathfindingMalus(var13)) {
                    var13 = var150;
                }
            }

            if (var11 == BlockPathTypes.OPEN && var4.getPathfindingMalus(var13) == 0.0F && var5 <= 1) {
                return BlockPathTypes.OPEN;
            } else {
                return var13;
            }
        }
    }

    protected BlockPathTypes getCachedBlockType(NPC var0, int var1, int var2, int var3) {
        return this.l.computeIfAbsent(BlockPos.asLong(var1, var2, var3),
                var4 -> getBlockPathType(this.level, var1, var2, var3, var0, this.entityWidth, this.entityHeight,
                        this.entityDepth, canOpenDoors(), canPassDoors()));
    }

    protected BlockPathTypes getCachedBlockType(Mob var0, int var1, int var2, int var3) {
        return (BlockPathTypes)this.l.computeIfAbsent(BlockPos.asLong(var1, var2, var3), (var4) -> {
            return this.getBlockPathType(this.level, var1, var2, var3, var0, this.entityWidth, this.entityHeight, this.entityDepth, this.canOpenDoors(), this.canPassDoors());
        });
    }

    public BlockPathTypes getBlockPathType(BlockGetter var0, int var1, int var2, int var3) {
        return getBlockPathTypeStatic(var0, new BlockPos.MutableBlockPos(var1, var2, var3));
    }

    public static BlockPathTypes getBlockPathTypeStatic(BlockGetter var0, BlockPos.MutableBlockPos var1) {
        int var2 = var1.getX();
        int var3 = var1.getY();
        int var4 = var1.getZ();
        BlockPathTypes var5 = getBlockPathTypeRaw(var0, var1);
        if (var5 == BlockPathTypes.OPEN && var3 >= var0.getMinBuildHeight() + 1) {
            BlockPathTypes var6 = getBlockPathTypeRaw(var0, var1.set(var2, var3 - 1, var4));
            var5 = var6 != BlockPathTypes.WALKABLE && var6 != BlockPathTypes.OPEN && var6 != BlockPathTypes.WATER && var6 != BlockPathTypes.LAVA ? BlockPathTypes.WALKABLE : BlockPathTypes.OPEN;
            if (var6 == BlockPathTypes.DAMAGE_FIRE) {
                var5 = BlockPathTypes.DAMAGE_FIRE;
            }

            if (var6 == BlockPathTypes.DAMAGE_CACTUS) {
                var5 = BlockPathTypes.DAMAGE_CACTUS;
            }

            if (var6 == BlockPathTypes.DAMAGE_OTHER) {
                var5 = BlockPathTypes.DAMAGE_OTHER;
            }

            if (var6 == BlockPathTypes.STICKY_HONEY) {
                var5 = BlockPathTypes.STICKY_HONEY;
            }
        }

        if (var5 == BlockPathTypes.WALKABLE) {
            var5 = checkNeighbourBlocks(var0, var1.set(var2, var3, var4), var5);
        }

        return var5;
    }

    public static BlockPathTypes checkNeighbourBlocks(BlockGetter var0, BlockPos.MutableBlockPos var1, BlockPathTypes var2) {
        int var3 = var1.getX();
        int var4 = var1.getY();
        int var5 = var1.getZ();

        for(int var6 = -1; var6 <= 1; ++var6) {
            for(int var7 = -1; var7 <= 1; ++var7) {
                for(int var8 = -1; var8 <= 1; ++var8) {
                    if (var6 != 0 || var8 != 0) {
                        var1.set(var3 + var6, var4 + var7, var5 + var8);
                        BlockState var9 = var0.getBlockState(var1);
                        if (var9.is(Blocks.CACTUS)) {
                            return BlockPathTypes.DANGER_CACTUS;
                        }

                        if (var9.is(Blocks.SWEET_BERRY_BUSH)) {
                            return BlockPathTypes.DANGER_OTHER;
                        }

                        if (isBurningBlock(var9)) {
                            return BlockPathTypes.DANGER_FIRE;
                        }

                        if (var0.getFluidState(var1).is(FluidTags.WATER)) {
                            return BlockPathTypes.WATER_BORDER;
                        }
                    }
                }
            }
        }

        return var2;
    }

    protected static BlockPathTypes getBlockPathTypeRaw(BlockGetter var0, BlockPos var1) {
        BlockState var2 = var0.getBlockState(var1);
        Block var3 = var2.getBlock();
        Material var4 = var2.getMaterial();
        if (var2.isAir()) {
            return BlockPathTypes.OPEN;
        } else if (!var2.is(BlockTags.TRAPDOORS) && !var2.is(Blocks.LILY_PAD) && !var2.is(Blocks.BIG_DRIPLEAF)) {
            if (var2.is(Blocks.POWDER_SNOW)) {
                return BlockPathTypes.POWDER_SNOW;
            } else if (var2.is(Blocks.CACTUS)) {
                return BlockPathTypes.DAMAGE_CACTUS;
            } else if (var2.is(Blocks.SWEET_BERRY_BUSH)) {
                return BlockPathTypes.DAMAGE_OTHER;
            } else if (var2.is(Blocks.HONEY_BLOCK)) {
                return BlockPathTypes.STICKY_HONEY;
            } else if (var2.is(Blocks.COCOA)) {
                return BlockPathTypes.COCOA;
            } else {
                FluidState var5 = var0.getFluidState(var1);
                if (var5.is(FluidTags.LAVA)) {
                    return BlockPathTypes.LAVA;
                } else if (isBurningBlock(var2)) {
                    return BlockPathTypes.DAMAGE_FIRE;
                } else if (DoorBlock.isWoodenDoor(var2) && !(Boolean)var2.getValue(DoorBlock.OPEN)) {
                    return BlockPathTypes.DOOR_WOOD_CLOSED;
                } else if (var3 instanceof DoorBlock && var4 == Material.METAL && !(Boolean)var2.getValue(DoorBlock.OPEN)) {
                    return BlockPathTypes.DOOR_IRON_CLOSED;
                } else if (var3 instanceof DoorBlock && (Boolean)var2.getValue(DoorBlock.OPEN)) {
                    return BlockPathTypes.DOOR_OPEN;
                } else if (var3 instanceof BaseRailBlock) {
                    return BlockPathTypes.RAIL;
                } else if (var3 instanceof LeavesBlock) {
                    return BlockPathTypes.LEAVES;
                } else if (var2.is(BlockTags.FENCES) || var2.is(BlockTags.WALLS) || var3 instanceof FenceGateBlock && !(Boolean)var2.getValue(FenceGateBlock.OPEN)) {
                    return BlockPathTypes.FENCE;
                } else if (!var2.isPathfindable(var0, var1, PathComputationType.LAND)) {
                    return BlockPathTypes.BLOCKED;
                } else {
                    return var5.is(FluidTags.WATER) ? BlockPathTypes.WATER : BlockPathTypes.OPEN;
                }
            }
        } else {
            return BlockPathTypes.TRAPDOOR;
        }
    }

    public static boolean isBurningBlock(BlockState var0) {
        return var0.is(BlockTags.FIRE) || var0.is(Blocks.LAVA) || var0.is(Blocks.MAGMA_BLOCK) || CampfireBlock.isLitCampfire(var0) || var0.is(Blocks.LAVA_CAULDRON);
    }
}
