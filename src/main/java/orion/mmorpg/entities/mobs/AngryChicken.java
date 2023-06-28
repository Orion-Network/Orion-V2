package orion.mmorpg.entities.mobs;

import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import org.bukkit.Material;
import orion.mmorpg.entities.samples.AbstractHostile;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Rewards;
import orion.mmorpg.utils.Stats;

public class AngryChicken extends AbstractHostile {

    public AngryChicken(String uuid, Location spawnLocation) {
        super(EntityTypes.l, uuid, "Poulet énervé", spawnLocation, new Stats.Builder().setBase(Stats.StatsInfo.VITALITY, 20).setBase(Stats.StatsInfo.STRENGTH, 4).setBase(Stats.StatsInfo.SPEED, 4).build(), 3);
        this.getRewards().add(new Rewards.Reward(BasicItem.generateRessourceItem(Material.FEATHER, "Plume trouée", 0, null, Rarity.COMMON).generateItem(null, 1), 60));
    }
}
