package orion.mmorpg.mythicsmob;

import io.lumine.mythic.bukkit.events.MythicDropLoadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.Mmorpg;

public class MythicMobListener implements Listener {
    private final Mmorpg mmorpg;

    public MythicMobListener(Mmorpg mmorpg) {
        this.mmorpg = mmorpg;
    }

    @EventHandler
    public void onMythicDrop(MythicDropLoadEvent event) {
        if(mmorpg.getItemManager().isItem(event.getDropName())) {
            ItemStack itemStack = mmorpg.getItemManager().createItemstack(event.getDropName(), 1);
            event.register(new MythicMobManager.ItemStackToMythicMobDrop(itemStack));
        }
    }
}
