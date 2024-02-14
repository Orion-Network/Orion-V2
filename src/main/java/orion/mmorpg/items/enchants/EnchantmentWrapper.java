package orion.mmorpg.items.enchants;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EnchantmentWrapper extends Enchantment {
    private final String name;
    @Setter
    private int startLevel, maxLevel;
    @Setter
    private EnchantmentTarget target = EnchantmentTarget.ALL;
    @Setter
    private boolean treasure = false, cursed = false;

    @Getter
    private ArrayList<Enchantment> conflicts = new ArrayList<>();

    public EnchantmentWrapper(@NotNull NamespacedKey key, String name, int maxLevel) {
        super(key);
        this.name = name;
        this.maxLevel = maxLevel;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public int getStartLevel() {
        return startLevel;
    }

    @NotNull
    @Override
    public EnchantmentTarget getItemTarget() {
        return target;
    }

    @Override
    public boolean isTreasure() {
        return treasure;
    }

    @Override
    public boolean isCursed() {
        return cursed;
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment enchantment) {
        return conflicts.contains(enchantment);
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack itemStack) {
        return target.includes(itemStack);
    }

    public static class EnchantmentBuilder {
        private EnchantmentWrapper enchantment;

        public EnchantmentBuilder(EnchantmentWrapper enchantment) {
            this.enchantment = enchantment;
        }

        public EnchantmentBuilder(@NotNull NamespacedKey key, String name, int maxLevel) {
            this.enchantment = new EnchantmentWrapper(key, name, maxLevel);
        }

        public EnchantmentBuilder setStartLevel(int startLevel) {
            enchantment.setStartLevel(startLevel);
            return this;
        }

        public EnchantmentBuilder setMaxLevel(int maxLevel) {
            enchantment.setMaxLevel(maxLevel);
            return this;
        }

        public EnchantmentBuilder setTarget(EnchantmentTarget target) {
            enchantment.setTarget(target);
            return this;
        }

        public EnchantmentBuilder setTreasure(boolean treasure) {
            enchantment.setTreasure(treasure);
            return this;
        }

        public EnchantmentBuilder setCursed(boolean cursed) {
            enchantment.setCursed(cursed);
            return this;
        }

        public EnchantmentBuilder addConflict(Enchantment enchantment) {
            this.enchantment.getConflicts().add(enchantment);
            return this;
        }

        public EnchantmentWrapper build() {
            return enchantment;
        }
    }
}
