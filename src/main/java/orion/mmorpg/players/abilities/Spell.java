package orion.mmorpg.players.abilities;

import de.slikey.effectlib.EffectType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.events.OnSpellSense;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Element;
import orion.mmorpg.utils.Knowledge;
import orion.mmorpg.utils.Utils;

public abstract class Spell {

    private final String internal_id;
    private final int level;
    private final Element element;
    private final long cooldown;

    public boolean asynchronous = true;
    public int delay = 0;
    public int period = 1;
    public boolean canceled = false;
    private final EffectType type = EffectType.INSTANT;

    private Entity caster;
    private OrionPlayer orionCaster;
    private Entity target;
    private OrionPlayer orionTarget;
    private Knowledge knowledge;

    public Spell(String internal_id, int level, Element element, long cooldown) {
        this.internal_id = internal_id;
        this.level = level;
        this.element = element;
        this.cooldown = cooldown;
    }

    public boolean beforeLaunch(Mmorpg mmorpg, Player player, OrionPlayer orionPlayer, boolean needSpell) {
        this.caster = player;
        this.orionCaster = orionPlayer;
        this.canceled = false;
        Knowledge knowledge;
        if (needSpell) {
            if (!orionPlayer.getKnownSpells().containsKey(this.getClass())) {
                Utils.sendInfoMessage(player, "Nous ne connaissez pas ce sort.");
                return false;
            }
            knowledge = orionPlayer.getKnownSpells().get(this.getClass());
            if(knowledge.getKnowledgeState().equals(Knowledge.KnowledgeState.UNKNOWN)) {
                Utils.sendInfoMessage(player, "Nous ne connaissons pas ce sort.");
                return false;
            } else if(knowledge.getKnowledgeState().equals(Knowledge.KnowledgeState.LEARN)) {
                Utils.sendInfoMessage(player, "Vous devez continuer d'apprendre ce sort.");
                return false;
            }
        } else {
            knowledge = new Knowledge(100);
        }
        if (orionPlayer.getLevel() < level) {
            Utils.sendInfoMessage(player, "Votre niveau est trop bas.");
            return false;
        }
        if (orionPlayer.getCooldownSpells().containsKey(this.getClass()))
            if (orionPlayer.getCooldownSpells().get(this.getClass()) > System.currentTimeMillis()) {
                Utils.sendInfoMessage(player, "Le sort est en cooldown");
                return false;
            }
        player.getNearbyEntities(5 * 10, 5 * 10, 5 * 10).forEach(e -> {
            if (e instanceof Player)
                Bukkit.getPluginManager().callEvent(new OnSpellSense(false, player, (Player) e, SpellList.valueOf(internal_id), this, OnSpellSense.Target.GLOBAL));
        });
        if (this.isCanceled()) return false;
        Utils.sendDebugMessage(player, "Sort " + this.internal_id + " !");
        orionPlayer.onLaunchSpell(player, orionPlayer, this);
        orionPlayer.getCooldownSpells().put(this.getClass(), System.currentTimeMillis() + cooldown);
        launch(mmorpg, caster.getLocation(), knowledge);
        orionPlayer.getKnownSpells().get(this.getClass()).addKnowledge((100 - getLevel() < 0 ? 1 : 100 - getLevel()));
        return true;
    }
    public void launch(Mmorpg mmorpg, Entity caster, Knowledge knowledge) {
        this.setCaster(caster);
        launch(mmorpg, caster.getLocation(), knowledge);
    }
    //TODO Change to a boolean to check if spell has been launched
    public abstract void launch(Mmorpg mmorpg, Location location, Knowledge knowledge);

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

    public Entity getCaster() {
        return caster;
    }

    public void setCaster(Entity caster) {
        this.caster = caster;
        if(caster instanceof Player)
            this.orionCaster = Mmorpg.INSTANCE.getPlayerManager().getPlayer(caster.getUniqueId());
    }

    public OrionPlayer getOrionCaster() {
        return orionCaster;
    }

    public void setOrionCaster(OrionPlayer orionCaster) {
        this.orionCaster = orionCaster;
    }

    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        this.target = target;
        if(target instanceof Player)
            setOrionTarget(Mmorpg.INSTANCE.getPlayerManager().getPlayer(target.getUniqueId()));
    }

    public OrionPlayer getOrionTarget() {
        return orionTarget;
    }

    public void setOrionTarget(OrionPlayer orionTarget) {
        this.orionTarget = orionTarget;
    }

    public Knowledge getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(Knowledge knowledge) {
        this.knowledge = knowledge;
    }


}
