package orion.mmorpg.players.abilities.supports;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.events.OnSpellSense;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.SpecialsSupport;
import orion.mmorpg.players.abilities.Spell;
import orion.mmorpg.players.abilities.Support;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Utils;

import java.lang.reflect.InvocationTargetException;

public class Haki extends Support implements SpecialsSupport {
    private boolean isHakiOn = false;
    public Haki() {
        super("Haki", Rarity.LEGENDARY, "Permet de manipuler votre esprit.");
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {

    }

    @Override
    public void onLaunchSpell(Player player, OrionPlayer stats, Spell s) {

    }

    @Override
    public void onHit(EntityDamageByEntityEvent event, OrionPlayer stats) {
        if(!isHakiOn) return;
        event.setDamage(event.getDamage()*1.2);
    }

    @Override
    public void onShootArrow(EntityShootBowEvent event) {

    }

    @Override
    public void onHitByEntity(EntityDamageByEntityEvent entityEvent) {
        if(!isHakiOn) return;
        entityEvent.setDamage(entityEvent.getDamage()*0.8);
    }

    @Override
    public void onSpellSense(OnSpellSense e) {

    }

    @Override
    public void onTick() {

    }

    @Override
    public void onDie() {

    }

    @Override
    public void onSwitchSlot(Player player, OrionPlayer stats, PlayerItemHeldEvent event) {
        super.onSwitchSlot(player, stats, event);
    }

    @Override
    public void onSupportEnd(Player p, OrionPlayer stats) {

    }

    @Override
    public void getFirstSpecial(Player player, OrionPlayer orionPlayer) {
        player.getNearbyEntities(10*orionPlayer.getLevel(), 10*orionPlayer.getLevel(), 10*orionPlayer.getLevel()).forEach(entity -> {
            if(entity instanceof LivingEntity) {
                PacketContainer fakeGlowing = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
                fakeGlowing.getIntegers().write(0,entity.getEntityId());
                WrappedDataWatcher watcher = new WrappedDataWatcher();
                WrappedDataWatcher.Serializer serialize = WrappedDataWatcher.Registry.get(Byte.class);
                watcher.setEntity(player);
                watcher.setObject(0, serialize, (byte) (0x40));
                fakeGlowing.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
                try {
                    ProtocolLibrary.getProtocolManager().sendServerPacket(player, fakeGlowing);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });

        Utils.sendInfoMessage(player, "Vous avez découvert un nouveau sort : " + getName());
    }

    @Override
    public void getSecondSpecial(Player player, OrionPlayer orionPlayer) {
        this.isHakiOn = true;
        Utils.sendInfoMessage(player, "Vous avez " + (isHakiOn ? "activé" : "désactivé") + " vote Haki de l'armement");
        return;
    }

    @Override
    public void getThirdSpecial(Player player, OrionPlayer orionPlayer) {
        if(orionPlayer.getLevel() < 100 && orionPlayer.getStats().getPower() < 400) {
            Utils.sendInfoMessage(player, "Vous ne remplissez pas les conditions pour utiliser le haki des rois.");
            return;
        }
        if(!Utils.canPvp(player))
            return;
        player.getNearbyEntities(10*orionPlayer.getLevel(), 10*orionPlayer.getLevel(), 10*orionPlayer.getLevel()).forEach(entity -> {
            if(!(entity instanceof LivingEntity)) return;
            if(entity instanceof Player) {
                Player target = (Player) entity;
                OrionPlayer targetPlayer = Mmorpg.INSTANCE.getPlayerManager().getPlayer(target.getUniqueId());
                if(targetPlayer.getLevel() < orionPlayer.getLevel() - orionPlayer.getLevel()/2) {
                    if(Utils.canPvp(target)) {
                        targetPlayer.respawn();
                    }
                }
            } else {
                ((LivingEntity) entity).damage(((LivingEntity) entity).getHealth() + 1);
            }

        });
        Utils.sendInfoMessage(player, "Vous avez découvert un nouveau sort 3 : " + getName());
    }
}
