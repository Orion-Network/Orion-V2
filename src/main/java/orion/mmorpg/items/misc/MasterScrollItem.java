package orion.mmorpg.items.misc;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import orion.mmorpg.Mmorpg;
import orion.mmorpg.config.AssociationConfig;
import orion.mmorpg.items.sample.BasicConsumableItem;
import orion.mmorpg.players.OrionPlayer;
import orion.mmorpg.players.abilities.SpellList;
import orion.mmorpg.utils.Namespaces;
import orion.mmorpg.utils.Rarity;
import orion.mmorpg.utils.Utils;

import java.util.Collections;

public class MasterScrollItem extends BasicConsumableItem {

    public MasterScrollItem() {
        super(Material.PAPER, "Parchemin", "MASTER_SCROLL", AssociationConfig.getModel("misc/paper/master_scroll"),
                Collections.singletonList("Un parchemin pouvant vous apprendre des sorts"), Rarity.SUPERRARE);
    }

    @Override
    public void onUseItem(Mmorpg mmorpg, OrionPlayer data, PlayerInteractEvent e) {
        String spell = e.getItem().getItemMeta().getPersistentDataContainer().get(Namespaces.spell_id,
                PersistentDataType.STRING);
        if (!checkScrollValidity(e, spell))
            return;
        int knowledge = e.getItem().getItemMeta().getPersistentDataContainer().get(Namespaces.knowledge,
                PersistentDataType.INTEGER);
        Utils.sendInfoMessage(e.getPlayer(),
                "Votre connaissance du sort " + SpellList.valueOf(spell).getName() + " a augmenté de " + knowledge);
        SpellList.learnSpell(data, SpellList.valueOf(spell).getSpellClass(), knowledge);
        super.onUseItem(mmorpg, data, e);
    }

    public ItemStack generateScroll(String spell_id, int knowledge) {
        spell_id = spell_id.toUpperCase();
        ItemStack i = generateItem(null, 1);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(this.RARITY.getChatColor() + this.NAME + " "
                + SpellList.valueOf(spell_id).getRarity().getChatColor() + SpellList.valueOf(spell_id).getName());
        meta.getPersistentDataContainer().set(Namespaces.spell_id, PersistentDataType.STRING, spell_id);
        meta.getPersistentDataContainer().set(Namespaces.knowledge, PersistentDataType.INTEGER, knowledge);
        this.getLORE().add(ChatColor.WHITE + "Experience du sort ajoutée : " + knowledge);
        meta.setLore(this.getLORE());

        i.setItemMeta(meta);
        return i;
    }
}
