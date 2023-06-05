package orion.mmorpg.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.effects.FishEffect;
import orion.mmorpg.items.ItemList;
import orion.mmorpg.items.misc.Gift;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.jobs.JobType;
import orion.mmorpg.utils.Namespaces;
import orion.mmorpg.utils.Rewards;
import orion.mmorpg.utils.Utils;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FishingListener implements Listener {
    Mmorpg plugin;
    Rewards rewards = new Rewards();

    public FishingListener(Mmorpg plugin) {
        this.plugin = plugin;
        Rewards.Reward reward = new Rewards.Reward(ItemList.BRONZE_INGOT.generateItemStack(1), 100);
        reward.getJobExp().put(JobType.FISHING, 10);
        reward.addRequiredJobExp(JobType.FISHING, 5);
        rewards.add(reward);

        Rewards.Reward reward2 = new Rewards.Reward(ItemList.BRONZE_COIN.generateItemStack(1), 75);
        reward2.getJobExp().put(JobType.FISHING, 5);
        rewards.add(reward2);
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
            event.getHook().setMinWaitTime(10);
            event.getHook().setMaxWaitTime(20);
        } else if(event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            ((Item)event.getCaught()).setItemStack(new Gift().generateGift("NULL_ITEM", "SERVER"));

            Utils.sendInfoMessage(event.getPlayer(), "Vous avez attrapé un poisson !");
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
}
