package orion.mmorpg.mythicsmob;

import io.lumine.mythic.api.adapters.AbstractItemStack;
import io.lumine.mythic.api.drops.DropMetadata;
import io.lumine.mythic.api.drops.IItemDrop;
import io.lumine.mythic.bukkit.adapters.BukkitItemStack;
import org.bukkit.inventory.ItemStack;

public class MythicMobManager {


    public void registerMythicMobItems() {

    }

    public static class ItemStackToMythicMobDrop implements IItemDrop {
        ItemStack itemStack;

        public ItemStackToMythicMobDrop(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        @Override
        public AbstractItemStack getDrop(DropMetadata dropMetadata, double v) {
            return new BukkitItemStack(itemStack);
        }
    }
}
