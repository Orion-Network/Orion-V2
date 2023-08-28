package orion.mmorpg.entities.mobs;

import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.Material;
import orion.mmorpg.entities.samples.AbstractNeutral;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Rewards;
import orion.mmorpg.utils.Stats;

public class Sheep extends AbstractNeutral {

    public Sheep(String uuid, Location spawnLocation) {
        super(EntityType.SHEEP, uuid, "Mouton", spawnLocation, new Stats.Builder().setBase(Stats.StatsInfo.VITALITY, 30).setBase(Stats.StatsInfo.STRENGTH, 2).setBase(Stats.StatsInfo.SPEED, 4).build(), 2);

        this.getRewards().add(new Rewards.Reward(BasicItem.generateRessourceItem("damaged_meat", Material.WHITE_WOOL, "Laine abimée", 0, null, Rarity.COMMON).generateItem(null, 2), 65));
        this.getRewards().add(new Rewards.Reward(BasicItem.generateRessourceItem("mutton_meat", Material.MUTTON, "Viande de mouton", 0, null, Rarity.COMMON).generateItem(null, 1), 35));
        this.getRewards().add(new Rewards.Reward(ItemList.SHEEP_CHESTPLATE.generateItemStack(1), 8));
        this.getRewards().add(new Rewards.Reward(ItemList.SHEEP_LEGGINGS.generateItemStack(1), 8));
        this.getRewards().add(new Rewards.Reward(ItemList.SHEEP_BOOTS.generateItemStack(1), 8));

        Rewards.Reward reward = new Rewards.Reward(null, 100);
        reward.getMoneys().put("gold", 10L);
        this.getRewards().add(reward);
    }
}
