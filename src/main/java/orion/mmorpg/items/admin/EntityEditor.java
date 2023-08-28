package orion.mmorpg.items.admin;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.entities.samples.AbstractEntityBase;
import orion.mmorpg.entities.samples.AbstractMob;
import orion.mmorpg.items.sample.BasicUsableItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Utils;

import java.util.Collections;
import java.util.List;

/*
Item need to open Entity Editor when shift right clicked,
If clicked in air, we will set a entity type, spawn or set and we can place entity at each right click
If clicked on entity, we will open a gui to edit entity, delete, edit, etc...
 */
public class EntityEditor extends BasicUsableItem {
    public EntityEditor() {
        super(Material.BRUSH, "Entity Editor", "ENTITY_EDITOR", 0, Collections.singletonList("Permet d'éditer les entitytés avec un clique droit"), Rarity.UNOBTAINABLE);
    }

    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        //Get if entity in front of player
        LivingEntity entity = Utils.getTargetInFront(e.getPlayer(), 5);
        if(entity == null) {
            e.getPlayer().sendMessage("§cAucune entité trouvée");
            return;
        }

        Utils.sendInfoMessage(e.getPlayer(), "Suppresion de l'entité");
        e.setCancelled(true);
        if(entity instanceof AbstractMob mob) {
            Mmorpg.INSTANCE.getMobManager().removeMob(mob);
            mob.getOverheadEntity().remove();
        }
        entity.remove();
    }
}
