package orion.mmorpg.entities.mobs;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.entities.samples.AbstractHostile;
import orion.mmorpg.utils.Stats;

public class WhiteWalker extends AbstractHostile {

    public WhiteWalker(EntityType<? extends PathfinderMob> entitytypes, String uuid, String name, Location spawnLocation, Stats stats, int level) {
        super(EntityType.SKELETON, uuid, "Marcheur Blanc", spawnLocation, new Stats.Builder().setBase(Stats.StatsInfo.VITALITY, 200).setBase(Stats.StatsInfo.STRENGTH, 20).setBase(Stats.StatsInfo.DEFENCE, 7).setBase(Stats.StatsInfo.SPEED, 4).build(), 15);

        this.setItemSlot(EquipmentSlot.HEAD, CraftItemStack.asNMSCopy(new ItemStack(Material.DIAMOND_HELMET)));
        this.setItemSlot(EquipmentSlot.CHEST, CraftItemStack.asNMSCopy(new ItemStack(Material.DIAMOND_CHESTPLATE)));
        this.setItemSlot(EquipmentSlot.LEGS, CraftItemStack.asNMSCopy(new ItemStack(Material.DIAMOND_LEGGINGS)));
        this.setItemSlot(EquipmentSlot.FEET, CraftItemStack.asNMSCopy(new ItemStack(Material.DIAMOND_BOOTS)));
        this.setItemSlot(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(new ItemStack(Material.DIAMOND_SWORD)));
        this.setItemSlot(EquipmentSlot.OFFHAND, CraftItemStack.asNMSCopy(new ItemStack(Material.SHIELD)));
    }
}
