package orion.mmorpg.entities.mobs;

import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.Material;
import orion.mmorpg.entities.samples.AbstractPassive;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.utils.ItemUtils;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Rewards;
import orion.mmorpg.utils.Stats;

public class CalmSheep extends AbstractPassive {
    public CalmSheep(String uuid, Location spawnLocation) {
        super(EntityType.SHEEP, uuid, "Mouton Calme", spawnLocation, new Stats.Builder().setBase(Stats.StatsInfo.VITALITY, 20).setBase(Stats.StatsInfo.SPEED, 4).build(), 1);
        this.getRewards().add(new Rewards.Reward(BasicItem.generateRessourceItem("damaged_wool", Material.WHITE_WOOL, "Laine abimée", 0, null, Rarity.COMMON).generateItem(null, 2), 60));
        this.getRewards().add(new Rewards.Reward(BasicItem.generateRessourceItem("mutton_meat", Material.MUTTON, "Viande de mouton", 0, null, Rarity.COMMON).generateItem(null, 1), 25));
        this.getRewards().add(new Rewards.Reward(ItemUtils.generateItemStack("SHEEP_CHESTPLATE", 1), 5));
        this.getRewards().add(new Rewards.Reward(ItemUtils.generateItemStack("SHEEP_LEGGINGS", 1), 5));
        this.getRewards().add(new Rewards.Reward(ItemUtils.generateItemStack("SHEEP_LEGGINGS", 1), 5));
    }
}
