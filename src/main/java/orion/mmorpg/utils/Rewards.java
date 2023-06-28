package orion.mmorpg.utils;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class Rewards extends ArrayList<Rewards.Reward> {
    double exp = 0;

    public ArrayList<ItemStack> generateRewards() {
        return generateRewards(0);
    }

    public ArrayList<ItemStack> generateRewards(float luck) {
        ArrayList<ItemStack> rewards = new ArrayList<>();
        Random random = new Random();
        if(this.isEmpty()) return rewards;
        for(Reward reward : this) {
            if(random.nextInt(reward.maxChance + 1) <= reward.getChance() * (luck == 0 ? 1 : (1 + luck/Stats.StatsInfo.LUCK.getCap()))) {
                rewards.add(reward.getItem());
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

    public static class Reward {
        ItemStack item;
        int chance;
        int maxChance = 100;

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
    }

}
