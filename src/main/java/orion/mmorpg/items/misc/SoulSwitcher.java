package orion.mmorpg.items.misc;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.items.sample.BasicItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.supports.ProjectSoul;
import orion.mmorpg.utils.Rarity;

public class SoulSwitcher extends BasicItem {
    Player p;

    public SoulSwitcher() {
        super(Material.PHANTOM_MEMBRANE, "Soul Switcher", "SOUL_SWITCHER", 0, "Une prouesse technologique pouvant vous faire changer d'âme.", Rarity.MYTHIC);
    }

    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        if (data.getSupport() == null || data.getSupport().getClass() != ProjectSoul.class) return;
        if (e.getPlayer().isSneaking()) {
            if (p != null)
                e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(p.getName()));
            else
                e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Aucun âme lié"));
        } else {
            Location loc = e.getPlayer().getLocation();
            if (p != null && p != e.getPlayer()) {
                e.getPlayer().teleport(p);
                p.teleport(loc);
            }
            p = e.getPlayer();
        }

        super.onUseItem(mmorpg, data, e);
    }
}
