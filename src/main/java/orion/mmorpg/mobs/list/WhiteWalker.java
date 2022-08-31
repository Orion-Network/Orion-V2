package orion.mmorpg.mobs.list;

import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.mobs.samples.SampleHostileMob;
import orion.mmorpg.utils.Stats;

public class WhiteWalker extends SampleHostileMob {
    public WhiteWalker(String uuid, Location location) {
        super("Marcheur Blanc", 15, EntityTypes.aB, uuid, location, new Stats.Builder().withVitality(200).withStrength(20).withDefence(7).withSpeed(4).build());
        this.setSlot(EnumItemSlot.f, CraftItemStack.asNMSCopy(new ItemStack(Material.DIAMOND_HELMET)));
        this.setSlot(EnumItemSlot.e, CraftItemStack.asNMSCopy(new ItemStack(Material.DIAMOND_CHESTPLATE)));
        this.setSlot(EnumItemSlot.d, CraftItemStack.asNMSCopy(new ItemStack(Material.DIAMOND_LEGGINGS)));
        this.setSlot(EnumItemSlot.c, CraftItemStack.asNMSCopy(new ItemStack(Material.DIAMOND_BOOTS)));
        this.setSlot(EnumItemSlot.a, CraftItemStack.asNMSCopy(new ItemStack(Material.DIAMOND_SWORD)));
        this.setSlot(EnumItemSlot.b, CraftItemStack.asNMSCopy(new ItemStack(Material.SHIELD)));

        //Rewards.Reward r = new Rewards.Reward(ItemList.WHITE_WALKER_SOUL.getProcessedItem(1), 1);
        //this.getRewards().add(r);
    }
}
