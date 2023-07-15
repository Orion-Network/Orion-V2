package orion.mmorpg.entities.mobs;

import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.Material;
import orion.mmorpg.entities.samples.AbstractPassive;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Rewards;
import orion.mmorpg.utils.Stats;

public class CalmSheep extends AbstractPassive {
    public CalmSheep(String uuid, Location spawnLocation) {
        super(EntityType.SHEEP, uuid, "Mouton Calme", spawnLocation, new Stats.Builder().setBase(Stats.StatsInfo.VITALITY, 20).setBase(Stats.StatsInfo.SPEED, 4).build(), 1);
        this.getRewards().add(new Rewards.Reward(BasicItem.generateRessourceItem(Material.WHITE_WOOL, "Laine abimée", 0, null, Rarity.COMMON).generateItem(null, 2), 60));
        this.getRewards().add(new Rewards.Reward(BasicItem.generateRessourceItem(Material.MUTTON, "Viande de mouton", 0, null, Rarity.COMMON).generateItem(null, 1), 25));
        this.getRewards().add(new Rewards.Reward(ItemList.SHEEP_CHESTPLATE.generateItem(), 5));
        this.getRewards().add(new Rewards.Reward(ItemList.SHEEP_LEGGINGS.generateItem(), 5));
        this.getRewards().add(new Rewards.Reward(ItemList.SHEEP_BOOTS.generateItem(), 5));
    }
}
