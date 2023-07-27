package orion.mmorpg.mythicsmob;

import io.lumine.mythic.bukkit.events.MythicDropLoadEvent;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import io.lumine.mythic.bukkit.events.MythicMobLootDropEvent;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.ItemList;

public class MythicMobListener implements Listener {
    private final Mmorpg mmorpg;

    public MythicMobListener(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @EventHandler
    public void onMythicDrop(MythicDropLoadEvent event) {
        if(EnumUtils.isValidEnum(ItemList.class, event.getDropName())) {
            ItemList itemList = ItemList.valueOf(event.getDropName());
            ItemStack itemStack = itemList.generateItemStack(1);
            event.register(new MythicMobManager.ItemStackToMythicMobDrop(itemStack));
        }
    }
}
