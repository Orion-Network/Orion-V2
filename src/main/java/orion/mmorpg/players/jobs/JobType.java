package orion.mmorpg.players.jobs;

import lombok.Getter;
import org.bukkit.Material;

public enum JobType {
    SMITHING(Smithing.class, Material.IRON_AXE, "⚒"), ALCHEMIST(Alchemist.class, Material.BREWING_STAND, "🧪"),
    MINING(Mining.class, Material.IRON_PICKAXE, "⛏"), FISHING(Fishing.class, Material.FISHING_ROD, "🎣");

    @Getter
    Class<? extends AbstractJob> jobClass;
    @Getter
    Material material;
    @Getter
    String icon;

    JobType(Class<? extends AbstractJob> jobClass, Material material, String icon) {
        this.jobClass = jobClass;
        this.material = material;
        this.icon = icon;
    }
}
