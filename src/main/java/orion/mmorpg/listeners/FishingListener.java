package orion.mmorpg.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.entities.Mobs;
import orion.mmorpg.entities.samples.AbstractMob;
import orion.mmorpg.items.misc.Gift;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.jobs.JobType;
import orion.mmorpg.utils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class FishingListener implements Listener {
    Mmorpg plugin;
    Rewards rewards = new Rewards();
    HashMap<Utils.Pair<Integer, Long>, Mobs> seaCreatures = new HashMap<>();

    public FishingListener(Mmorpg plugin) {
        this.plugin = plugin;
        Rewards.Reward reward = new Rewards.Reward(BasicItem.generateRessourceItem("bronze_ingot", Material.COPPER_INGOT, "Lingot de bronze", 0, null, Rarity.COMMON), 75);
        reward.getJobExp().put(JobType.FISHING, 10);
        reward.addRequiredJobExp(JobType.FISHING, 5);
        rewards.add(reward);

        Rewards.Reward money_1 = new Rewards.Reward(null, 50);
        money_1.getMoneys().put("gold", 10L);
        rewards.add(money_1);

        Rewards.Reward reward2 = new Rewards.Reward(Mmorpg.INSTANCE.getItemManager().createItemstack("BRONZE_COIN", 1), 100);
        reward2.getJobExp().put(JobType.FISHING, 5);
        rewards.add(reward2);

        seaCreatures.put(new Utils.Pair<>(1, 1000L), Mobs.SEA_ZOMBIE);
    }

    @EventHandler
    public void onPlayerFishing(PlayerFishEvent event) {
        OrionPlayer player = plugin.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
        if(!event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(Namespaces.tool_type, PersistentDataType.STRING)) {
            Utils.sendErrorMessage(event.getPlayer(), "Vous devez tenir une canne à pêche !");
            event.setCancelled(true);
            return;
        }
        if(event.getState() == PlayerFishEvent.State.FISHING) {
            double maxWait = 600*Math.pow(0.99, player.getStats().get(Stats.StatsInfo.FISHING_SPEED)/3);
            maxWait = Math.max(maxWait, 20);
            event.getHook().setMinWaitTime((int)maxWait/2);
            event.getHook().setMaxWaitTime((int)maxWait);
        } else if(event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            ((Item)event.getCaught()).setItemStack(new Gift().generateGift("NULL_ITEM", "SERVER"));

            int random = Utils.getRandomInt(1, 100);
            if(random <= player.getStats().get(Stats.StatsInfo.SEA_CREATURE_CHANCE)) {
                Utils.sendInfoMessage(event.getPlayer(), "Vous avez attrapé un monstre marin !");
                final AbstractMob mob = getRandomSeaCreature(player).getMob(UUID.randomUUID().toString(), event.getHook().getLocation());
                mob.setRespawn(false);
                mob.spawn();
                Vector vector = event.getPlayer().getLocation().subtract(event.getHook().getLocation()).add(0,2,0).toVector().normalize().multiply(1.2);
                mob.getBukkitEntity().setVelocity(vector);
                return;
            }

            Utils.sendInfoMessage(event.getPlayer(), "Vous avez attrapé un objet !");
            ArrayList<Rewards.Reward> rewards = this.rewards.generateRewards(player);
            Rewards.Reward reward = rewards.get(Utils.getRandomInt(0, rewards.size() - 1));
            event.getPlayer().getInventory().addItem(reward.getItem());
            for(JobType jobType : reward.getJobExp().keySet()) {
                player.getJobs().getJob(jobType).addExp(reward.getJobExp().get(jobType));
                Utils.sendTooltipInfoMessage(event.getPlayer(), "Vous avez gagné " + reward.getJobExp().get(jobType) + " points d'expérience en " + jobType.name());
            }
            player.addExperience(event.getPlayer(), this.rewards.getExp() + reward.getExp());
        }
    }

    private Mobs getRandomSeaCreature(OrionPlayer player) {
        //If fishing level lower than Pair Integer add to possibleCreature
        HashMap<Long, Mobs> possibleCreatures = new HashMap<>();
                seaCreatures.forEach((pair, mob) -> {
                    if(pair.getKey() <= player.getJobs().getJob(JobType.FISHING).getLevel()) {
                        possibleCreatures.put(pair.getValue(), mob);
                    }
                });
                //get a random SeaCreature weighted with Long
                long totalWeight = 0;
                for(long weight : possibleCreatures.keySet()) {
                    totalWeight += weight;
                }
                long random = Utils.getRandomLong(0, totalWeight);
                for(long weight : possibleCreatures.keySet()) {
                    if(random <= weight) {
                        return possibleCreatures.get(weight);
                    }
                    random -= weight;
                }
        return null;
    }
}
