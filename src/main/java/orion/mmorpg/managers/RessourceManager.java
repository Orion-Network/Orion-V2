package orion.mmorpg.managers;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.world.Ressource;

import java.util.*;

public class RessourceManager extends AbstractManager implements Runnable {

    @Getter
    HashMap<Location, Ressource> ressources;
    HashMap<Chunk, RessourceBatch> ressourceBatches = new HashMap<>();
    public RessourceManager(Mmorpg plugin) {
        super(plugin);
        this.ressources = plugin.getMongoClient().loadRessources();

        Bukkit.getScheduler().runTaskTimer(plugin, this, 0, 20);

        ressources.forEach((location, ressource) -> {
            ressource.spawn();
        });
    }

    @Override
    public void run() {
        ressourceBatches.forEach((chunk, ressourceBatch) -> {
            if(chunk.isLoaded()) {
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

    public boolean breakRessource(Location location) {
        Ressource ressource = ressources.get(location);
        if(ressource != null) {
            ressourceBatches.get(location.getChunk()).addRessource(ressource);
            ressources.get(location).setSpawned(false);
            return true;
        }
        return false;
    }

    public void addRessource(Ressource ressource) {
        Chunk chunk = ressource.getLocation().getChunk();
        if(!ressourceBatches.containsKey(chunk)) {
            ressourceBatches.put(chunk, new RessourceBatch());
        }
        ressourceBatches.get(chunk).addRessource(ressource);
        ressources.put(ressource.getLocation(), ressource);
    }

    public RessourceBatch getRessourceBatch(Chunk chunk) {
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
