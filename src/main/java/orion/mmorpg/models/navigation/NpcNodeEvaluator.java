package orion.mmorpg.models.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.Target;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import orion.mmorpg.models.NPC;

public abstract class NpcNodeEvaluator extends NodeEvaluator {
    protected NPC mob;
    protected final Int2ObjectMap<Node> c = new Int2ObjectOpenHashMap();
    protected int entityWidth;
    protected int entityHeight;
    protected int entityDepth;
    protected boolean canPassDoors;
    protected boolean canOpenDoors;
    protected boolean canFloat;

    public void prepare(PathNavigationRegion var0, NPC var1) {
        this.level = var0;
        this.mob = var1;
        this.c.clear();
        this.entityWidth = Mth.floor(var1.getBbWidth() + 1.0F);
        this.entityHeight = Mth.floor(var1.getBbHeight() + 1.0F);
        this.entityDepth = Mth.floor(var1.getBbWidth() + 1.0F);
    }

    @Override
    public void prepare(PathNavigationRegion var0, Mob var1) {
        this.level = var0;
        this.c.clear();
        this.entityWidth = Mth.floor(var1.getBbWidth() + 1.0F);
        this.entityHeight = Mth.floor(var1.getBbHeight() + 1.0F);
        this.entityDepth = Mth.floor(var1.getBbWidth() + 1.0F);
    }

    @Override
    public void done() {
        this.level = null;
        this.mob = null;
    }

    @Override
    protected Node getNode(BlockPos var0) {
        return this.getNode(var0.getX(), var0.getY(), var0.getZ());
    }

    @Override
    protected Node getNode(int var0, int var1, int var2) {
        return (Node)this.c.computeIfAbsent(Node.createHash(var0, var1, var2), (var3) -> new Node(var0, var1, var2));
    }

    public void setCanPassDoors(boolean var0) {
        this.canPassDoors = var0;
    }

    public void setCanOpenDoors(boolean var0) {
        this.canOpenDoors = var0;
    }

    public void setCanFloat(boolean var0) {
        this.canFloat = var0;
    }

    public boolean canPassDoors() {
        return this.canPassDoors;
    }

    public boolean canOpenDoors() {
        return this.canOpenDoors;
    }

    public boolean canFloat() {
        return this.canFloat;
    }
}
