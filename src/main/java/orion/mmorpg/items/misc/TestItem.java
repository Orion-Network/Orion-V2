package orion.mmorpg.items.misc;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.list.ProjectSoul;
import orion.mmorpg.utils.Rarity;

public class TestItem extends BasicItem {
    Player p;
    public TestItem() {
        super(Material.NETHER_STAR, "Test Item", "TEST_ITEM", 0, null, Rarity.UNOBTAINABLE);
    }

    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        if(data.getSupport() == null || data.getSupport().getClass() != ProjectSoul.class) return;
        if(e.getPlayer().isSneaking()) {
            if(p != null)
                e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(p.getName()));
            else
                e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Aucun âme lié"));
        } else {
            Location loc = e.getPlayer().getLocation();
            if(p != null && p != e.getPlayer()) {
                e.getPlayer().teleport(p);
                p.teleport(loc);
            }
            p = e.getPlayer();
        }

        super.onUseItem(mmorpg, data, e);
    }
}
