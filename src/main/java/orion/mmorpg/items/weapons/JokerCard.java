package orion.mmorpg.items.weapons;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import orion.mmorpg.config.AssociationConfig;
import orion.mmorpg.items.sample.BasicMeleeWeapon;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Stats;

import java.util.Collections;
import java.util.Random;

public class JokerCard extends BasicMeleeWeapon {
    final String[] card = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Valet", "Reine", "Roi", "Joker"};

    public JokerCard() {
        super(Material.IRON_SWORD, "Carte Joker", "JOKER_CARD", AssociationConfig.getModel("weapon/card/01"), Collections.singletonList("Une arme intrigante multipliant ses dégâts selon la carte tirée"), Rarity.RARE, new Stats(15, 10, 0, 0, 0, 0, 0, 0, 0), 7);
    }

    @Override
    public void onHitWithItem(EntityDamageByEntityEvent e) {
        int r = new Random().nextInt(14) + 1;
        float multiplier = (1 + ((float) r / 10));
        e.setDamage(e.getDamage() * multiplier);
        ((Player) e.getDamager()).spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(card[r - 1]));
        super.onHitWithItem(e);
    }
}
