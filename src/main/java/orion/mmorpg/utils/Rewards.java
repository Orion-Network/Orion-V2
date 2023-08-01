package orion.mmorpg.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.jobs.JobType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class Rewards extends ArrayList<Rewards.Reward> {
    double exp = 0;

    public ArrayList<ItemStack> award(OrionPlayer player) {
        ArrayList<ItemStack> items = new ArrayList<>();
        player.addExperience(Bukkit.getPlayer(player.getUniqueId()), exp);
        for(Reward reward : generateRewards(player)) {
            if(reward.getItem() != null)
                items.add(reward.getItem());
            for(JobType job : reward.getJobExp().keySet()) {
                Player p = Bukkit.getPlayer(player.getUniqueId());
                Utils.sendTooltipInfoMessage(p, "Vous avez gagné " + reward.getJobExp().get(job) + " points d'expérience en " + job.name());
                player.getJobs().getJob(job).addExp(reward.getJobExp().get(job));
            }
            for(String currency : reward.getMoneys().keySet()) {
                player.getCurrencies().add(currency, reward.getMoneys().get(currency));
                Utils.sendInfoMessage(Bukkit.getPlayer(player.getUniqueId()), "Vous avez gagné " + reward.getMoneys().get(currency) + " " + currency);
            }
        }
        return items;
    }

    public ArrayList<Reward> generateRewards(OrionPlayer orionPlayer) {
        int luck = (int) orionPlayer.getStats().get(Stats.StatsInfo.LUCK);
        ArrayList<Reward> rewards = new ArrayList<>();
        Random random = new Random();
        for(Reward reward : this) {
            if(reward.getRequiredLevel() > orionPlayer.getLevel()) {
                continue;
            }
            boolean canGet = true;
            for(JobType job : reward.getRequiredJobLevel().keySet()) {
                if(orionPlayer.getJobs().getJob(job) == null || orionPlayer.getJobs().getJob(job).getLevel() < reward.getRequiredJobLevel().get(job)) {
                    canGet = false;
                    break;
                }
            }
            if(canGet && random.nextInt(reward.maxChance + 1) <= reward.getChance() * (luck == 0 ? 1 : (1 + luck/Stats.StatsInfo.LUCK.getCap()))) {
                rewards.add(reward);
            }
        }
        return rewards;
    }

    public double getExp() {
        return exp;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }

    public void addExp(double exp) {
        this.exp += exp;
    }

    public static class Reward {
        ItemStack item;
        @Getter@Setter
        int exp = 0;
        int chance;
        int maxChance = 100;
        @Getter@Setter
        int requiredLevel = 0;
        @Getter
        HashMap<JobType, Integer> requiredJobLevel = new HashMap<>();
        @Getter
        HashMap<JobType, Integer> jobExp = new HashMap<>();
        @Getter
        HashMap<String, Long> moneys = new HashMap<>();

        public Reward(ItemStack item, int chance) {
            this.item = item;
            this.chance = chance;
        }

        public ItemStack getItem() {
            return item;
        }

        public int getChance() {
            return chance;
        }

        public int getMaxChance() {
            return maxChance;
        }

        public void setMaxChance(int maxChance) {
            this.maxChance = maxChance;
        }

        public void addRequiredJobExp(JobType job, int level) {
            if(requiredJobLevel.containsKey(job)) {
                requiredJobLevel.put(job, requiredJobLevel.get(job) + level);
            } else {
                requiredJobLevel.put(job, level);
            }
        }
    }

}
