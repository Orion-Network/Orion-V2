package orion.mmorpg.models.navigation;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.profiling.metrics.MetricCategory;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.*;
import orion.mmorpg.models.NPC;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NpcPathFinder extends PathFinder {
    private static final float FUDGING = 1.5F;
    private final Node[] neighbors = new Node[32];
    private final int maxVisitedNodes;
    private final NpcNodeEvaluator nodeEvaluator;
    private static final boolean DEBUG = false;
    private final BinaryHeap openSet = new BinaryHeap();

    public NpcPathFinder(NpcNodeEvaluator var0, int var1) {
        super(var0, var1);
        this.nodeEvaluator = var0;
        this.maxVisitedNodes = var1;
    }

    @Nullable
    public Path findPath(PathNavigationRegion var0, NPC var1, Set<BlockPos> var2, float var3, int var4, float var5) {
        this.openSet.clear();
        this.nodeEvaluator.prepare(var0, var1);
        Node var6 = this.nodeEvaluator.getStart();
        Map<Target, BlockPos> var7 = (Map)var2.stream().collect(Collectors.toMap((var0x) -> this.nodeEvaluator.getGoal((double)var0x.getX(), (double)var0x.getY(), (double)var0x.getZ()), Function.identity()));
        Path var8 = this.findPath(var0.getProfiler(), var6, var7, var3, var4, var5);
        this.nodeEvaluator.done();
        return var8;
    }

    @Nullable
    public Path findPath(PathNavigationRegion var0, Mob var1, Set<BlockPos> var2, float var3, int var4, float var5) {
        this.openSet.clear();
        this.nodeEvaluator.prepare(var0, var1);
        Node var6 = this.nodeEvaluator.getStart();
        Map<Target, BlockPos> var7 = (Map)var2.stream().collect(Collectors.toMap((var0x) -> this.nodeEvaluator.getGoal((double)var0x.getX(), (double)var0x.getY(), (double)var0x.getZ()), Function.identity()));
        Path var8 = this.findPath(var0.getProfiler(), var6, var7, var3, var4, var5);
        this.nodeEvaluator.done();
        return var8;
    }

    @Nullable
    private Path findPath(ProfilerFiller var0, Node var1, Map<Target, BlockPos> var2, float var3, int var4, float var5) {
        var0.push("find_path");
        var0.markForCharting(MetricCategory.PATH_FINDING);
        Set<Target> var6 = var2.keySet();
        var1.g = 0.0F;
        var1.h = this.getBestH(var1, var6);
        var1.f = var1.h;
        this.openSet.clear();
        this.openSet.insert(var1);
        Set<Node> var7 = ImmutableSet.of();
        int var8 = 0;
        Set<Target> var9 = Sets.newHashSetWithExpectedSize(var6.size());
        int var10 = (int)((float)this.maxVisitedNodes * var5);

        while(!this.openSet.isEmpty()) {
            ++var8;
            if (var8 >= var10) {
                break;
            }

            Node var11 = this.openSet.pop();
            var11.closed = true;
            Iterator var13 = var6.iterator();

            while(var13.hasNext()) {
                Target var130 = (Target)var13.next();
                if (var11.distanceManhattan(var130) <= (float)var4) {
                    var130.setReached();
                    var9.add(var130);
                }
            }

            if (!var9.isEmpty()) {
                break;
            }

            if (!(var11.distanceTo(var1) >= var3)) {
                int var12 = this.nodeEvaluator.getNeighbors(this.neighbors, var11);

                for(int var130 = 0; var130 < var12; ++var130) {
                    Node var14 = this.neighbors[var130];
                    float var15 = var11.distanceTo(var14);
                    var14.walkedDistance = var11.walkedDistance + var15;
                    float var16 = var11.g + var15 + var14.costMalus;
                    if (var14.walkedDistance < var3 && (!var14.inOpenSet() || var16 < var14.g)) {
                        var14.cameFrom = var11;
                        var14.g = var16;
                        var14.h = this.getBestH(var14, var6) * 1.5F;
                        if (var14.inOpenSet()) {
                            this.openSet.changeCost(var14, var14.g + var14.h);
                        } else {
                            var14.f = var14.g + var14.h;
                            this.openSet.insert(var14);
                        }
                    }
                }
            }
        }

        Optional<Path> var11 = !var9.isEmpty() ? var9.stream().map((var1x) -> {
            return this.reconstructPath(var1x.getBestNode(), (BlockPos)var2.get(var1x), true);
        }).min(Comparator.comparingInt(Path::getNodeCount)) : var6.stream().map((var1x) -> {
            return this.reconstructPath(var1x.getBestNode(), (BlockPos)var2.get(var1x), false);
        }).min(Comparator.comparingDouble(Path::getDistToTarget).thenComparingInt(Path::getNodeCount));
        var0.pop();
        if (!var11.isPresent()) {
            return null;
        } else {
            Path var12 = (Path)var11.get();
            return var12;
        }
    }

    private float getBestH(Node var0, Set<Target> var1) {
        float var2 = Float.MAX_VALUE;

        float var5;
        for(Iterator var4 = var1.iterator(); var4.hasNext(); var2 = Math.min(var5, var2)) {
            Target var40 = (Target)var4.next();
            var5 = var0.distanceTo(var40);
            var40.updateBest(var5, var0);
        }

        return var2;
    }

    private Path reconstructPath(Node var0, BlockPos var1, boolean var2) {
        List<Node> var3 = Lists.newArrayList();
        Node var4 = var0;
        var3.add(0, var0);

        while(var4.cameFrom != null) {
            var4 = var4.cameFrom;
            var3.add(0, var4);
        }

        return new Path(var3, var1, var2);
    }
}
