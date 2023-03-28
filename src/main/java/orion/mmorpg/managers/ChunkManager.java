package orion.mmorpg.managers;

import org.bukkit.Chunk;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.entities.samples.AbstractMob;

import java.util.LinkedHashMap;
import java.util.Set;

public class ChunkManager extends AbstractManager {
    private LinkedHashMap<ChunkIdentifier, Set<AbstractMob.MobData>> chunkMobs = new LinkedHashMap<>();


    public ChunkManager(Mmorpg plugin) {
        super(plugin);
    }

    public LinkedHashMap<ChunkIdentifier, Set<AbstractMob.MobData>> getChunkMobs() {
        return chunkMobs;
    }

    public boolean containChunk(Chunk chunk) {
        for(ChunkIdentifier chunkIdentifier : chunkMobs.keySet()) {
            if(chunkIdentifier.equals(chunk)) {
                return true;
            }
        }
        return false;
    }

    public Set<AbstractMob.MobData> getChunk(Chunk chunk) {
        for (ChunkIdentifier chunkIdentifier : chunkMobs.keySet()) {
            if (chunkIdentifier.equals(chunk)) {
                return chunkMobs.get(chunkIdentifier);
            }
        }
        return null;
    }

    public Set<AbstractMob.MobData> getChunk(ChunkIdentifier chunk) {
        return chunkMobs.get(chunk);
    }


    public static class ChunkIdentifier {
        private int x;
        private int z;

        public ChunkIdentifier(int x, int z) {
            this.x = x;
            this.z = z;
        }

        public int getX() {
            return x;
        }

        public int getZ() {
            return z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null) return false;
            if(getClass() == o.getClass()) {
                ChunkIdentifier that = (ChunkIdentifier) o;
                return x == that.x &&
                        z == that.z;
            } else if(o instanceof Chunk) {
                Chunk chunk = (Chunk) o;
                return x == chunk.getX() && z == chunk.getZ();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return 31 * x + z;
        }
    }
}
