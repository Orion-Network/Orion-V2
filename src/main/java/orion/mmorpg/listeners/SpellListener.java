package orion.mmorpg.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import orion.mmorpg.events.BeforeSpellCastEvent;
import orion.mmorpg.utils.Utils;
import orion.mmorpg.utils.WorldGuardUtils;

public class SpellListener implements Listener {

    @EventHandler
    public void beforeSpellCast(BeforeSpellCastEvent event) {
        Entity caster = event.getSpell().getCaster();
        if (!WorldGuardUtils.isFlagAllowed(caster.getLocation(), WorldGuardUtils.Flag.NO_SPELLS)) {
            event.getSpell().setCanceled(true);
            Utils.sendInfoMessage(caster, "Vous ne pouvez pas envoyer de sorts dans cette zone.");
        }
    }

}
