package orion.mmorpg.world;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.items.tools.ToolType;
import orion.mmorpg.players.jobs.JobType;

public class Ressource {
    @Getter
    Location location;
    @Getter
    Material material;
    @Getter
    long respawnTime;
    @Getter
    JobType jobType;
    @Getter
    int jobLevel;

    @Getter
    ToolType toolType;
    @Getter
    int toolPower;
    @Getter
    int jobExp;
    @Getter
    ItemStack drop;

    @Getter @Setter boolean isSpawned = false;

    public Ressource(Location location, Material  material, long respawnTime, JobType jobType, int jobLevel, ToolType toolType, int toolPower, int jobExp, ItemStack drop) {
        this.location = location;
        this.material = material;
        this.respawnTime = respawnTime;
        this.jobType = jobType;
        this.jobLevel = jobLevel;
        this.toolType = toolType;
        this.toolPower = toolPower;
        this.jobExp = jobExp;
        this.drop = drop;
    }

    public void spawn() {
        location.getBlock().setType(material);
        isSpawned = true;
    }
}
