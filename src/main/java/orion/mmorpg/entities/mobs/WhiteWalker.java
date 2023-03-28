package orion.mmorpg.entities.mobs;

import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.entities.samples.AbstractHostile;
import orion.mmorpg.utils.Stats;

public class WhiteWalker extends AbstractHostile {

    public WhiteWalker(EntityTypes<? extends EntityCreature> entitytypes, String uuid, String name, Location spawnLocation, Stats stats, int level) {
        super(EntityTypes.aB, uuid, "Marcheur Blanc", spawnLocation, new Stats.Builder().withVitality(200).withStrength(20).withDefence(7).withSpeed(4).build(), 15);

        this.setSlot(EnumItemSlot.f, CraftItemStack.asNMSCopy(new ItemStack(Material.DIAMOND_HELMET)));
        this.setSlot(EnumItemSlot.e, CraftItemStack.asNMSCopy(new ItemStack(Material.DIAMOND_CHESTPLATE)));
        this.setSlot(EnumItemSlot.d, CraftItemStack.asNMSCopy(new ItemStack(Material.DIAMOND_LEGGINGS)));
        this.setSlot(EnumItemSlot.c, CraftItemStack.asNMSCopy(new ItemStack(Material.DIAMOND_BOOTS)));
        this.setSlot(EnumItemSlot.a, CraftItemStack.asNMSCopy(new ItemStack(Material.DIAMOND_SWORD)));
        this.setSlot(EnumItemSlot.b, CraftItemStack.asNMSCopy(new ItemStack(Material.SHIELD)));
    }
}
