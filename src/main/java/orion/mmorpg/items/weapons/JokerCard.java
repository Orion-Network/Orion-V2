package orion.mmorpg.items.weapons;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import orion.mmorpg.items.sample.BasicMeleeWeapon;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;
import orion.mmorpg.utils.Utils;

import java.util.Random;

public class JokerCard extends BasicMeleeWeapon {
    final String[] card = {"1","2", "3", "4", "5", "6", "7", "8", "9", "10", "Valet", "Reine", "Roi", "Joker"};
    public JokerCard() {
        super(Material.IRON_SWORD, "Carte Joker", "JOKER_CARD", 1, "Une carte avec ce grande capacités", Rarity.RARE, new Stats(15, 10, 0, 0, 0, 0, 0, 0, 0), 7);
    }

    @Override
    public void onHitWithItem(EntityDamageByEntityEvent e) {
        int r = new Random().nextInt(14) + 1;
        float multiplier = (1 + ((float)r/10));
        Utils.sendInfoMessage(e.getDamager(), "bDamage : " + e.getDamage() +  " Multiplier : " + multiplier + " tDamage : " + e.getDamage() * multiplier);
        e.setDamage(e.getDamage() * multiplier);
        ((Player) e.getDamager()).spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(card[r-1]));
        super.onHitWithItem(e);
    }
}
