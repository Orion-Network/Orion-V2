package orion.mmorpg.players.abilities;

import de.slikey.effectlib.EffectType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Element;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Utils;

import java.util.ArrayList;

public abstract class Spell {

    private final String internal_id;
    private final int level;
    private final Element element;
    private final long cooldown;

    public boolean asynchronous = true;
    public int delay = 0;
    public int period = 1;
    private EffectType type = EffectType.INSTANT;

    public boolean canceled = false;

    public Spell(String internal_id, int level, Element element, long cooldown) {
        this.internal_id = internal_id;
        this.level = level;
        this.element = element;
        this.cooldown = cooldown;
    }

    public boolean beforeLaunch(Mmorpg mmorpg, Player player, OrionPlayer orionPlayer, boolean needSpell) {
        if(needSpell) {
            if(!orionPlayer.getSpells().containsKey(internal_id)) {
                Utils.sendInfoMessage(player, "Nous ne connaissez pas ce sort.");
                return false;
            }
        }
        if(orionPlayer.getLevel() < level) {
            Utils.sendInfoMessage(player, "Votre niveau est trop bas.");
            return false;
        }
        if(orionPlayer.getCooldownSpells().containsKey(internal_id))
            if(orionPlayer.getCooldownSpells().get(internal_id) > System.currentTimeMillis()) {
                Utils.sendInfoMessage(player, "Le sort est en cooldown");
                return false;
            }
        launch(mmorpg, player, orionPlayer);
        return true;
    }

    public void launch(Mmorpg mmorpg, Player player, OrionPlayer orionPlayer) {
        orionPlayer.getCooldownSpells().put(internal_id, System.currentTimeMillis() + cooldown);
    }

    public String getInternal_id() {
        return internal_id;
    }

    public int getLevel() {
        return level;
    }

    public Element getElement() {
        return element;
    }

    public long getCooldown() {
        return cooldown;
    }

    public boolean isAsynchronous() {
        return asynchronous;
    }

    public int getDelay() {
        return delay;
    }

    public int getPeriod() {
        return period;
    }

    public EffectType getType() {
        return type;
    }

    public boolean isCanceled() {
        return canceled;
    }
}
