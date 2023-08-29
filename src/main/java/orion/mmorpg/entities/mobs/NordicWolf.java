package orion.mmorpg.entities.mobs;

import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.Material;
import orion.mmorpg.entities.samples.AbstractHostile;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Rewards;
import orion.mmorpg.utils.Stats;

public class NordicWolf extends AbstractHostile {
    public NordicWolf(String uuid, Location spawnLocation) {
        super(EntityType.WOLF, uuid, "Loup Nordique", spawnLocation, new Stats.Builder().setBase(Stats.StatsInfo.VITALITY, 150).setBase(Stats.StatsInfo.STRENGTH, 17).setBase(Stats.StatsInfo.DEFENCE, 4).setBase(Stats.StatsInfo.SPEED, 5).build(), 13);
        this.getRewards().add(new Rewards.Reward(BasicItem.generateRessourceItem("wolf_fur", Material.LEATHER, "Fourrure de loup", 0, null, Rarity.COMMON), 70));
        this.getRewards().add(new Rewards.Reward(BasicItem.generateRessourceItem("wolf_bone", Material.BONE, "Os de loup", 0, null, Rarity.COMMON), 30));

        Rewards.Reward money = new Rewards.Reward(null, 100);
        money.getMoneys().put("gold", 20L);
        this.getRewards().add(money);
    }
}
