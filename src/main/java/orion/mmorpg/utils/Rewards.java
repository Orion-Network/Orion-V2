package orion.mmorpg.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Random;

public class Rewards extends ArrayList<Rewards.Reward> {
    double exp = 0;
    int itemNumber = 1;
    boolean isRandom = true;
    boolean dropBadLuckDust = false;

    public ArrayList<ItemStack> generateRewards() {
        ArrayList<ItemStack> rewards = new ArrayList<>();
        if(this.isEmpty()) return rewards;
        if(isRandom) {
            LinkedHashMap<Reward, Integer> doubles = new LinkedHashMap<>();
            for (int i = 0; i < itemNumber; i++) {
                Random rand = new Random();
                Reward r = this.get(rand.nextInt(this.size()));
                if(doubles.containsKey(r)) {
                    if(doubles.get(r) < r.getChanceToGetMultiple()) {
                        rewards.add(r.getItem());
                        doubles.replace(r, (doubles.get(r)+1));
                    } else {
                        if(dropBadLuckDust)
                            rewards.add(badLuckDust());
                    }
                } else {
                    rewards.add(r.getItem());
                    doubles.put(r, 1);
                }
            }
            return rewards;
        } else {
            return (ArrayList<ItemStack>) Collections.singletonList(this.get(0).getItem());
        }
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public boolean isRandom() {
        return isRandom;
    }

    public void setRandom(boolean random) {
        isRandom = random;
    }

    public boolean isDropBadLuckDust() {
        return dropBadLuckDust;
    }

    public void setDropBadLuckDust(boolean dropBadLuckDust) {
        this.dropBadLuckDust = dropBadLuckDust;
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
        int chanceToGetMultiple;

        public Reward(ItemStack item, int chance, int chanceToGetMultiple) {
            this.item = item;
            this.chance = chance;
            this.chanceToGetMultiple = chanceToGetMultiple;
        }

        public ItemStack getItem() {
            return item;
        }

        public int getChance() {
            return chance;
        }

        public int getChanceToGetMultiple() {
            return chanceToGetMultiple;
        }
    }

    public ItemStack badLuckDust() {
        ItemStack badLuck = new ItemStack(Material.GUNPOWDER);
        ItemMeta meta = badLuck.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Poudre de malchance");
        badLuck.setItemMeta(meta);
        return badLuck;
    }

}
