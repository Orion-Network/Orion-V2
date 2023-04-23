package orion.mmorpg.managers;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Utils;
import orion.mmorpg.world.Ressource;

import java.util.*;

public class RessourceManager extends AbstractManager implements Runnable {

    @Getter
    HashMap<Location, Ressource> ressources;
    HashMap<Utils.ChunkCoords, RessourceBatch> ressourceBatches;
    public RessourceManager(Mmorpg plugin) {
        super(plugin);
        ressourceBatches = new HashMap<>();
        this.ressources = plugin.getMongoClient().loadRessources();

        Bukkit.getScheduler().runTaskTimer(plugin, this, 0, 20);

        ressources.forEach((location, ressource) -> {
            ressource.setSpawned(ressource.getLocation().getBlock().getType().equals(ressource.getMaterial()));
            addRessourceToBatch(ressource);
        });
    }

    @Override
    public void run() {

        ressourceBatches.forEach((chunk, ressourceBatch) -> {
            if(chunk.getWorld().getChunkAt(chunk.getX(), chunk.getZ()).isLoaded()) {
                RessourceBatch batch = ressourceBatches.get(chunk);
                Ressource ressource = batch.getRessource();
                if(ressource != null) {
                    ressource.spawn();
                }
            }
        });
    }

    public boolean isRessource(Location location) {
        return ressources.containsKey(location);
    }

    public boolean breakRessource(OrionPlayer player, Location location) {
        Ressource ressource = ressources.get(location);
        if(ressource != null) {
            ressourceBatches.get(new Utils.ChunkCoords(location.getChunk())).addRessource(ressource);
            ressources.get(location).setSpawned(false);
            player.getJobs().getJob(ressource.getJobType()).addExp(ressource.getJobExp());
            return true;
        }
        return false;
    }

    protected void addRessourceToBatch(Ressource ressource) {
        Chunk chunk = Objects.requireNonNull(ressource.getLocation().getWorld()).getChunkAt(ressource.getLocation());
        Utils.ChunkCoords chunkCoords = new Utils.ChunkCoords(chunk);
        if(!ressourceBatches.containsKey(chunkCoords)) {
            System.out.println("Creating new batch for chunk " + chunk.toString());
            ressourceBatches.put(chunkCoords, new RessourceBatch());
        }
        ressourceBatches.get(chunkCoords).addRessource(ressource);
    }

    public void addRessource(Ressource ressource) {
        Chunk chunk = ressource.getLocation().getWorld().getChunkAt(ressource.getLocation());
        Utils.ChunkCoords chunkCoords = new Utils.ChunkCoords(chunk);
        if(!ressourceBatches.containsKey(chunkCoords)) {
            ressourceBatches.put(chunkCoords, new RessourceBatch());
        }
        ressourceBatches.get(chunkCoords).addRessource(ressource);
        ressources.put(ressource.getLocation(), ressource);

        Mmorpg.INSTANCE.getMongoClient().saveRessource(ressource);
    }

    public void removeRessource(Ressource ressource) {
        ressources.remove(ressource.getLocation());
        Mmorpg.INSTANCE.getMongoClient().deleteRessource(ressource);
    }

    public RessourceBatch getRessourceBatch(Utils.ChunkCoords chunk) {
        return ressourceBatches.get(chunk);
    }

    public Ressource getRessource(Location location) {
        return ressources.get(location);
    }

    public static class RessourceBatch {
        PriorityQueue<AbstractMap.SimpleEntry<Long, Ressource>> ressources = new PriorityQueue<>(Comparator.comparingLong(AbstractMap.SimpleEntry::getKey));

        public void addRessource(Ressource ressource) {
            ressources.add(new AbstractMap.SimpleEntry<>(System.currentTimeMillis() + ressource.getRespawnTime(), ressource));
        }

        public Ressource getRessource() {
            if(!ressources.isEmpty()) {
                AbstractMap.SimpleEntry<Long, Ressource> entry = ressources.peek();
                if(entry.getKey() <= System.currentTimeMillis()) {
                    ressources.remove(entry);
                    return entry.getValue();
                }
            }
            return null;
        }
    }
}
